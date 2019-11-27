package io.github.adrianogba.crud_kotlin


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_add_veiculo.*
import okhttp3.FormBody
import okhttp3.HttpUrl


class AddPedido : AppCompatActivity() {







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_veiculo)







        btncadastrar.setOnClickListener {


            postTeste()


}
        }



    


    private val client = okhttp3.OkHttpClient()





    fun postTeste() {
        val url : HttpUrl= HttpUrl.Builder()
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

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            println(response.body!!.string())
        }
    }

}
