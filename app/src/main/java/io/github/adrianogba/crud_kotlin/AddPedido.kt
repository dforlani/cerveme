package io.github.adrianogba.crud_kotlin

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_add_veiculo.*

import java.util.HashMap
import com.android.volley.Request.Method.POST
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.AsyncTask.execute
import com.android.volley.toolbox.HttpResponse
import okhttp3.FormBody
import okhttp3.HttpUrl


import okhttp3.MediaType.Companion.toMediaType







class AddPedido : AppCompatActivity() {

    internal var ehnovo: String = "-1"

    internal lateinit var progressDialog: ProgressDialog
    internal lateinit var queue: RequestQueue
    internal var bundle: Bundle? = null

    //variaveis a guardar no modo edição
    internal lateinit var dt_cadastro: String
    internal lateinit var id: String
    internal var editar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_veiculo)


        queue = Volley.newRequestQueue(this)


        rlEhnovo.setOnClickListener {
            val lista = arrayOfNulls<String>(2)
            lista[0] = "Novo"
            lista[1] = "Usado"
            val builder = AlertDialog.Builder(this@AddPedido)
            builder.setTitle("O veículo é Novo ou Usado?")
            builder.setItems(lista) { dialogInterface, which ->
                if (which == 0) {
                    txtehnovo.text = "Novo"
                    ehnovo = "1"
                } else {
                    txtehnovo.text = "Usado"
                    ehnovo = "0"
                }
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        btncadastrar.setOnClickListener {


            postTeste()





            if (testForm()!!) {

                progressDialog = ProgressDialog(this@AddPedido)
                progressDialog.setMessage("Carregando...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                var url = getString(R.string.webservice)
                if (editar) {
                    url = getString(R.string.webservice)
                }

                val stringRequest = object : StringRequest(Request.Method.GET,
                        url,

                        Response.Listener { response ->

                    try {
                         progressDialog.cancel()
                        Toast.makeText(this@AddPedido, response, Toast.LENGTH_LONG).show()
                        val i = Intent(this@AddPedido, MainActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)

                    } catch (e: Exception) {
                        Toast.makeText(this@AddPedido, "Problemas na comuncação com o servidor.", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                        progressDialog.cancel()
                    }
                }, Response.ErrorListener {
                    progressDialog.cancel()

                    Toast.makeText(this@AddPedido,
                            "Problema na comunicação com o servidor!",
                            Toast.LENGTH_LONG).show()
                }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val params = HashMap<String, String>()


                        params["Content-Type"] = "application/json; charset=utf-8"
                        params["User-agent"] = "My useragent"



                        return params
                    }

                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        if (editar) {
                            params["PATH"] = "updateVeiculo"
                        } else {
                            params["PATH"] = "addVeiculo"
                        }
                        params["MARCA"] = etmarca.text.toString().trim { it <= ' ' }
                        params["MODELO"] = etmodelo.text.toString().trim { it <= ' ' }
                        params["PRECO"] = etpreco.text.toString().trim { it <= ' ' }
                        params["COR"] = etcor.text.toString().trim { it <= ' ' }
                        params["ANO"] = etano.text.toString().trim { it <= ' ' }
                        params["DESCRICAO"] = etdescricao.text.toString().trim { it <= ' ' }
                        params["EHNOVO"] = ehnovo
                        if (editar) {
                            params["ID"] = id
                            //params.put("DT_CADASTRO", dt_cadastro);
                        }

                        return params
                    }




                }


                queue.add(stringRequest)
            }
        }

        try {
            bundle = intent.extras
            if (bundle!!.getString("editar")!!.equals("editar", ignoreCase = true)) {

                editar = true

                etmarca.setText(bundle!!.getString("marca"), TextView.BufferType.EDITABLE)
                etmodelo.setText(bundle!!.getString("modelo"), TextView.BufferType.EDITABLE)
                etcor.setText(bundle!!.getString("cor"), TextView.BufferType.EDITABLE)
                etano.setText(bundle!!.getString("ano"), TextView.BufferType.EDITABLE)
                etpreco.setText(bundle!!.getString("preco"), TextView.BufferType.EDITABLE)
                etdescricao.setText(bundle!!.getString("descricao"), TextView.BufferType.EDITABLE)

                id = bundle!!.getString("id")
                dt_cadastro = bundle!!.getString("dt_cadastro")

                ehnovo = bundle!!.getString("ehnovo")
                if (ehnovo === "1") {
                    txtehnovo.text = "Novo"

                } else {
                    txtehnovo.text = "Usado"
                }
            }

        } catch (e: Exception) {
        }

    }

    fun testForm(): Boolean? {
        if (etmarca.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
                || etmodelo.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
                || etcor.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
                || etano.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
                || etpreco.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
                || etdescricao.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
                || ehnovo === "-1") {
            Toast.makeText(this, "Preencha todo o formulário.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    private val client = okhttp3.OkHttpClient()

      fun bowlingJson(player1:String, player2:String):String {
        return "{'winCondition':'HIGH_SCORE',"         + "'players':["        + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"        + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"        + "]}"
  }



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
