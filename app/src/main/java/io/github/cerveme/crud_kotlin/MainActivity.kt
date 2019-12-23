package io.github.cerveme.crud_kotlin


import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.github.cerveme.crud_kotlin.adapter.PedidoExpListAdapter
import io.github.cerveme.crud_kotlin.comunicacao.Comunicacao
import io.github.cerveme.crud_kotlin.database.DatabaseHelper
import io.github.cerveme.crud_kotlin.model.Pedido
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.FormBody
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {


    private var jsonParser: JsonParser? = null
    private var gson: Gson? = null
    private var pedidoList: ArrayList<Pedido> = ArrayList()
    private var expAdapter: PedidoExpListAdapter? = null
    var com = Comunicacao()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        requisitaPedidosComandaAberta();
        atualizaStatusPedidos()


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        jsonParser = JsonParser()
        gson = Gson()

        //recebe respostas de outras activities se tiver
        val b = intent.extras
        if (b != null) {
            val lista = b.get("pedidoList")
            if (lista != null) {
                pedidoList = (b.get("pedidoList") as ArrayList<Pedido>)

            } else {
                pedidoList = ArrayList()
            }
            expAdapter = PedidoExpListAdapter(this, pedidoList)
            expListViewPedido.setAdapter(expAdapter)

        }

        btnAddPedido.setOnClickListener({ v ->
            val dbHandler = DatabaseHelper(this, null)
            if (dbHandler.hasCodigoCadastrado()) {
                val i = Intent(v.context, ViewPedido::class.java)
                i.putExtra("pedidoList", pedidoList)
                v.context.startActivity(i)
            } else {
                Toast.makeText(this, " Dirija-se ao caixa para pedir o seu código de cliente e o insira nas configurações", Toast.LENGTH_LONG).show()
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val i = Intent(this, ViewConfiguracao::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.layout.menu_main, menu)
        return true
    }

    /**
     * Função que inicia uma thread para verificar o status dos pedidos
     * Só verifica o status de pedidos com status de Enviado e Em Atendimento
     */
    fun atualizaStatusPedidos() {
        val thread = Thread {

            while (true) {
                pedidoList.forEach {
                    if (it.esperandoResposta()) {
                        var resposta = com.verificaStatusPedido(it.pk_pedido_app)

                        //transforma a respota JSon em um objeto do tipo pedido
                        val gson2 = GsonBuilder().setPrettyPrinting().create()
                        var pedido: Pedido = gson2.fromJson(resposta, object : TypeToken<Pedido>() {}.type)

                        it.status = pedido.status
                        println(pedido.status)

                        //a thread não pode acessar as views, apenas a thread principal que pode, por
                        //isso precisamos desse comando especial
                        this@MainActivity.runOnUiThread(java.lang.Runnable {
                            expAdapter = PedidoExpListAdapter(this, pedidoList)
                            expListViewPedido.setAdapter(expAdapter)
                        })
                    }


                }
                //atualiza os status a cada 10 segundos
                Thread.sleep(10000)

            }
        }

        thread.start()
    }

    fun requisitaPedidosComandaAberta() {
        val thread = Thread {
            //parâmetros POST
            val formBuilder = FormBody.Builder()


            try {
                val formBody = formBuilder.build()
                val dbHandler = DatabaseHelper(this, null)
                //remove o código antigo antes de incluir o novo
                val codigo_cliente_app = dbHandler.getCodigoCliente()

                var resposta = com.requisitaPedidosDaComandaAberta(codigo_cliente_app)

                //transforma a respota JSon em um objeto do tipo pedido
                val gson2 = GsonBuilder().setPrettyPrinting().create()
                var pedidos: ArrayList<Pedido> = gson2.fromJson(resposta, object : TypeToken<ArrayList<Pedido>>() {}.type)

                //retorno de mensagem de erro
                if (pedidos.size > 0) {
                    pedidoList = pedidos
                }
            } catch (e: Exception) {
               var i:Int = 0
                //Toast.makeText(this@MainActivity, "Ocorreu um erro na solicitação dos pedidos já enviados anteriormente. Tente novamente. Se o problema persistir, procure o Caixa.", Toast.LENGTH_LONG).show()
            }
        }

        thread.start()
    }


}
