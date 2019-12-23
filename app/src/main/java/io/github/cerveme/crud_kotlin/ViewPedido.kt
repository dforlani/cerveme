package io.github.cerveme.crud_kotlin


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.cerveme.crud_kotlin.adapter.CardapioListAdapter
import io.github.cerveme.crud_kotlin.comunicacao.Comunicacao
import io.github.cerveme.crud_kotlin.database.DatabaseHelper
import io.github.cerveme.crud_kotlin.model.Cardapio
import io.github.cerveme.crud_kotlin.model.Pedido
import kotlinx.android.synthetic.main.activity_add_pedido.*
import okhttp3.FormBody
import java.util.*


class ViewPedido() : AppCompatActivity() {


    private var cardapioList: ArrayList<Cardapio> = ArrayList()
    //vai armazenar quais itens que foram solicitados para depois ser associado ao pedido criado
    private var itensPedido: ArrayList<Cardapio> = ArrayList()
    private var pedidoList: ArrayList<Pedido>? = null

    private var adapter: CardapioListAdapter? = null
    private var com = Comunicacao()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pedido)

        //recebe as informações da activity main
        val b = intent.extras

        if (b != null) {
            val lista = b.get("pedidoList")
            if (lista != null) {
                pedidoList = (b.get("pedidoList") as ArrayList<Pedido>)

            } else {
                pedidoList = ArrayList()
            }

            pedidoList = (b.get("pedidoList") as ArrayList<Pedido>)
        }

        requisitaCardapio()

        btnCadastrar.setOnClickListener {
            enviaPedido()
        }


    }


    fun enviaPedido() {
        //parâmetros POST
        val formBuilder = FormBody.Builder()
        var count = 0
        cardapioList.forEach {
            if (it.quantidade.toInt() > 0) {
                itensPedido.add(it)
                formBuilder.add("itens[${it.fk_preco}][fk_preco]", it.fk_preco)
                formBuilder.add("itens[${it.fk_preco}][quantidade]", it.quantidade)
                count++
            }
        }

        if (count > 0) {

            try {

                val formBody = formBuilder.build()
                val dbHandler = DatabaseHelper(this, null)
                //remove o código antigo antes de incluir o novo
                val pk_cliente = dbHandler.getCodigoCliente()

                var resposta = com.enviaPedido(formBody, pk_cliente)

                //transforma a respota JSon em um objeto do tipo pedido
                val gson2 = GsonBuilder().setPrettyPrinting().create()
                var pedido: Pedido = gson2.fromJson(resposta, object : TypeToken<Pedido>() {}.type)

                //retorno de mensagem de erro
                if (pedido.pk_pedido_app == "-1") {
                    Toast.makeText(this@ViewPedido, "Erro: "+pedido.status, Toast.LENGTH_LONG).show()
                } else {
                    pedido.itens = itensPedido
                    //val novaLista:ArrayList<Pedido> = ArrayList<Pedido>()
                    //novaLista.add(pedido)
                    //pedidoList =  novaLista + pedidoList
                    pedidoList!!.add(pedido)

                    if (pedido.status == "Erro") {
                        Toast.makeText(this@ViewPedido, "Problemas na comunicação com o servidor, dirija-se ao caixa.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@ViewPedido, "Pedido enviado com sucesso.", Toast.LENGTH_LONG).show()

                        //envio da pedidoList de volta para a MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("pedidoList", pedidoList)
                        startActivityForResult(intent, Activity.RESULT_OK)

                        //fecha a Activity atual
                        finish()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ViewPedido, "Ocorreu um erro no envio do pedido. Tente novamente. Se o problema persistir, procure o Caixa.", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this@ViewPedido, "Adicione ao menos 1 item para realizar o pedido.", Toast.LENGTH_LONG).show()
        }
    }

    fun requisitaCardapio() {

        try {
            cardapioList.clear()

            //cria o objeto que vai processar a string de cardapio e gerar a List
            val gson2 = GsonBuilder().setPrettyPrinting().create()
            var resposta = com.cardapio()
            var list: List<Cardapio> = gson2.fromJson(resposta, object : TypeToken<List<Cardapio>>() {}.type)

            cardapioList.addAll(list)

            adapter = CardapioListAdapter(this, cardapioList)

            listViewCardapio.adapter = adapter
        } catch (e: Exception) {
            Toast.makeText(this@ViewPedido, "Ocorreu um erro na solicitação do cardápio. Tente novamente. Se o problema persistir, procure o Caixa.", Toast.LENGTH_LONG).show()
            finish()
        }
    }





}
