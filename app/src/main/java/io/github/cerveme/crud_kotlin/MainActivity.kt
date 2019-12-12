package io.github.cerveme.crud_kotlin


import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.github.cerveme.crud_kotlin.adapter.PedidoExpListAdapter
import io.github.cerveme.crud_kotlin.comunicacao.Comunicacao
import io.github.cerveme.crud_kotlin.database.DatabaseHelper
import io.github.cerveme.crud_kotlin.model.Cliente
import io.github.cerveme.crud_kotlin.model.Pedido
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    private var jsonParser: JsonParser? = null
    private var gson: Gson? = null
    private var pedidoList: ArrayList<Pedido> = ArrayList()
    private var expAdapter: PedidoExpListAdapter? = null
    var com = Comunicacao()


    private var pk_cliente = "14"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        atualizaStatusPedidos()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        jsonParser = JsonParser()
        gson = Gson()

        //recebe respostas de outras activities se tiver
        val b = intent.extras
        if (b != null) {
            pedidoList = (b.get("pedidoList") as ArrayList<Pedido>)

            //adapter = PedidoListAdapter(this, pedidoList)
            // listViewPedido.setAdapter(adapter)

            expAdapter = PedidoExpListAdapter(this, pedidoList)
            expListViewPedido.setAdapter(expAdapter)
        }

        inicializaCliente()

        btnAddPedido.setOnClickListener({ v ->
            val i = Intent(v.context, ViewPedido::class.java)
            i.putExtra("pedidoList", pedidoList)
            i.putExtra("pk_cliente", pk_cliente)
            v.context.startActivity(i)
        })


    }

    /**
     * Função que inicia uma thread para verificar o status dos pedidos
     * Só verifica o status de pedidos com status de Enviado e Em Atendimento
     */
    fun atualizaStatusPedidos() {
        val thread = Thread {

            while(true){
                pedidoList.forEach{
                    if(it.esperandoResposta()) {
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

    private fun inicializaCliente(){
        val dbHandler = DatabaseHelper(this, null)
        val user = Cliente(pk_cliente, "Diogo")
        dbHandler.addName(user)
        Toast.makeText(this, user.nome + "Added to database", Toast.LENGTH_LONG).show()
    }


}
