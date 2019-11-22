package io.github.adrianogba.crud_kotlin.old

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.github.adrianogba.crud_kotlin.MainActivity
import io.github.adrianogba.crud_kotlin.R
import io.github.adrianogba.crud_kotlin.VolleyCustomRequest
import kotlinx.android.synthetic.main.activity_add_veiculo.*

import java.util.HashMap


class AddVeiculo_old : AppCompatActivity() {

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
            val builder = AlertDialog.Builder(this@AddVeiculo_old)
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
            //teste
           // val requestQueue = Volley.newRequestQueue(getActivity())
            val params = HashMap<String, String>()
            if (editar) {
                params["PATH"] = "updateVeiculo"
            } else {
                params["PATH"] = "addVeiculo"
            }
            params["MARCA"] = etmarca.text.toString().trim { it <= ' ' }
            params["MODELO"] = etmodelo.text.toString().trim { it <= ' ' }
            val jsObjRequest = VolleyCustomRequest(Request.Method.GET,
                    getString(R.string.webservice),
                    params,
                    Response.Listener { response ->
                        try {
                            progressDialog.cancel()

                            val i = Intent(this@AddVeiculo_old, MainActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(i)

                        } catch (e: Exception) {
                            Toast.makeText(this@AddVeiculo_old, "Problemas na comuncação com o servidor.", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                            progressDialog.cancel()
                        }
                    },
                    Response.ErrorListener { error ->
                        var i = 0
                    }
            )

            queue.add(jsObjRequest)
            //teste








            if (testForm()!!) {

                progressDialog = ProgressDialog(this@AddVeiculo_old)
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
                        Toast.makeText(this@AddVeiculo_old, response, Toast.LENGTH_LONG).show()
                        val i = Intent(this@AddVeiculo_old, MainActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)

                    } catch (e: Exception) {
                        Toast.makeText(this@AddVeiculo_old, "Problemas na comuncação com o servidor.", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                        progressDialog.cancel()
                    }
                }, Response.ErrorListener {
                    progressDialog.cancel()

                    Toast.makeText(this@AddVeiculo_old,
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
}
