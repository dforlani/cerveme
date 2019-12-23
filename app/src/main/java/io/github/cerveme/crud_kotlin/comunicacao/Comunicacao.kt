package io.github.cerveme.crud_kotlin.comunicacao

import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class Comunicacao {

    val ip: String = "192.168.123.106"
    val URL: String = "cervejaria/web/pedidoapp/"
    val CARDAPIO: String = "cardapio"
    val PEDIR: String = "pedir"
    val VERIFICARSTATUSPEDIDO:String = "verificar-status-pedido"
    val REQUISITAPEDIDOSCOMANDAABERTA:String = "requisita-pedidos-comanda-aberta"

    private var client = okhttp3.OkHttpClient()



    @Throws(Exception::class)
    fun cardapio(): String {
        val url: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(ip)
                .addPathSegments(URL + CARDAPIO)
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
    @Throws(Exception::class)
    fun enviaPedido(formBody: FormBody, codigo_cliente_app: String): String {
        val url: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(ip)
                .addPathSegments(URL + PEDIR)
                .addQueryParameter("codigo_cliente_app", codigo_cliente_app) //inclui parâmetros GET
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


    /** //parâmetros POST
     * val formBody = FormBody.Builder()
    //            .add("search", "Jurassic Park")
    //            .build()
     */
    @Throws(Exception::class)
    fun verificaStatusPedido(pk_pedido_app: String): String {
        val url: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(ip)
                .addPathSegments(URL + VERIFICARSTATUSPEDIDO)
                .addQueryParameter("pk_pedido_app", pk_pedido_app) //inclui parâmetros GET
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

    /**
     * Requisita ao servidor todos os pedidos da comanda aberta
     */
    @Throws(Exception::class)
    fun requisitaPedidosDaComandaAberta(codigo_cliente_app: String): String {
        val url: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(ip)
                .addPathSegments(URL + REQUISITAPEDIDOSCOMANDAABERTA)
                .addQueryParameter("codigo_cliente_app", codigo_cliente_app) //inclui parâmetros GET
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
}