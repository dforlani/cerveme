package io.github.cerveme.crud_kotlin


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.cerveme.crud_kotlin.model.Cardapio
import kotlinx.android.synthetic.main.activity_add_veiculo.*
import okhttp3.FormBody
import okhttp3.HttpUrl
import java.util.ArrayList


class ViewPedido : AppCompatActivity() {

    private var client = okhttp3.OkHttpClient()
    private var resultado: String = ""
    private var cardapioList: ArrayList<Cardapio> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_veiculo)

        btnCadastrar.setOnClickListener {
            requisitaCardapio()
        }
    }

    fun requisitaCardapio() {
        val url: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host("192.168.123.106")
                .addPathSegments("cervejaria/web/pedidoapp/cardapio")
                .addQueryParameter("q", "polar bears") //inclui parâmetros GET
                .build()

        //parâmetros POST
        val formBody = FormBody.Builder()
                .add("search", "Jurassic Park")
                .build()

        //montagem da requisição
        val request = okhttp3.Request.Builder()
                .url(url)
                .post(formBody)
                .build()

        //execução da requisição
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful)
                throw java.io.IOException("Unexpected code $response")

            //for ((name, value) in response.headers) {
            //    println("$name: $value")
            //}

            // println(response.body!!.string())

            resultado = response.body!!.string()

        }

        cardapioList.clear()

        //cria o objeto que vai processar a string de cardapio e gerar a List
        val gson2 = GsonBuilder().setPrettyPrinting().create()
        var list: List<Cardapio> = gson2.fromJson(resultado, object : TypeToken<List<Cardapio>>() {}.type)

        cardapioList.addAll(list)

        cardapioList.forEach { cardapio ->
            val tv = TextView(this)
            tv.textSize = 20f
            tv.text = cardapio.denominacao
            cardapioContainer.addView(tv)

        }


    }

}
