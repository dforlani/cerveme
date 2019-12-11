package io.github.cerveme.crud_kotlin.comunicacao

import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class Comunicacao {

    val ip:String = "192.168.1.106"
    val URL:String = "cervejaria/web/pedidoapp/"
    val CARDAPIO:String = "cardapio"
    val PEDIR:String = "pedir"

    private var client = okhttp3.OkHttpClient()





    fun cardapio():String {
        val url: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(ip)
                .addPathSegments(URL+CARDAPIO)
                .build()


        //montagem da requisição
        val request = Request.Builder()
                .url(url)
                .build()

        //execução da requisição
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful)
                throw IOException("Unexpected code $response")

            return response.body!!.string()

        }
    }


    /** //parâmetros POST
     * val formBody = FormBody.Builder()
    //            .add("search", "Jurassic Park")
    //            .build()
     */
    fun enviaPedido(formBody:FormBody, pk_venda:String):String {
        val url: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(ip)
                .addPathSegments(URL+PEDIR)
                .addQueryParameter("pk_venda", pk_venda) //inclui parâmetros GET
                .build()


        //montagem da requisição
        val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()



        //execução da requisição
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful)
                throw IOException("Unexpected code $response")

            return response.body!!.string()

        }
    }
}