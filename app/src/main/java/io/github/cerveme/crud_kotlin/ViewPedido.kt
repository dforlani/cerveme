package io.github.cerveme.crud_kotlin


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.cerveme.crud_kotlin.adapter.CardapioListAdapter
import io.github.cerveme.crud_kotlin.comunicacao.Comunicacao
import io.github.cerveme.crud_kotlin.model.Cardapio
import kotlinx.android.synthetic.main.activity_add_pedido.*
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.util.ArrayList


class ViewPedido : AppCompatActivity() {


    private var cardapioList: ArrayList<Cardapio> = ArrayList()
    private var adapter: CardapioListAdapter? = null
    var com = Comunicacao()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pedido)

        btnCadastrar.setOnClickListener {
            //parâmetros POST
            val formBuilder = FormBody.Builder()
            cardapioList.forEach {
                if (it.quantidade.toInt() > 0) {
                    formBuilder.add("itens[${it.pk_preco}][pk_preco]", it.pk_preco)
                    formBuilder.add("itens[${it.pk_preco}][quantidade]", it.quantidade)
                }
            }


            val formBody = formBuilder.build()


            var status = com.enviaPedido(formBody, "703")
            var int: Int = 9


        }



        requisitaCardapio()


    }

    fun requisitaCardapio() {


        cardapioList.clear()

        //cria o objeto que vai processar a string de cardapio e gerar a List
        val gson2 = GsonBuilder().setPrettyPrinting().create()
        var list: List<Cardapio> = gson2.fromJson(com.cardapio(), object : TypeToken<List<Cardapio>>() {}.type)

        cardapioList.addAll(list)

        //adapter =  ArrayAdapter(this, android.R.layout.simple_list_item_1, cardapioList)
        adapter = CardapioListAdapter(this, cardapioList)

        listViewCardapio.adapter = adapter
    }


}
