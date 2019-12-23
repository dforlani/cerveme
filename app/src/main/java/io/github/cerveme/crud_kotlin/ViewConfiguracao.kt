package io.github.cerveme.crud_kotlin


import android.annotation.SuppressLint
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
import io.github.cerveme.crud_kotlin.model.Cliente
import io.github.cerveme.crud_kotlin.model.Pedido
import kotlinx.android.synthetic.main.activity_configuracao.*
import okhttp3.FormBody
import java.util.*
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class ViewConfiguracao() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracao)

        //val actionBar = this.actionBar
        // actionBar!!.setDisplayHomeAsUpEnabled(true)

        carregaCodigoCliente()

        btnSalvar.setOnClickListener {
            salvarCodigoCliente()
        }


    }

    private fun carregaCodigoCliente() {

        val dbHandler = DatabaseHelper(this, null)
        // dbHandler.dropDatabase()
        val cursor = dbHandler.getAllClientes()
        if (cursor!!.count > 0) {
            cursor!!.moveToFirst()
            do {
                codigoCliente.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)))
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    private fun salvarCodigoCliente() {

        if (codigoCliente.text.length == 4) {

            val dbHandler = DatabaseHelper(this, null)
            //remove o código antigo antes de incluir o novo
            dbHandler.deleteAllClientes()

            val user = Cliente(codigoCliente.text.toString())
            dbHandler.addCliente(user)
            Toast.makeText(this, user.codigo_cliente + " Código adicionado ao banco", Toast.LENGTH_LONG).show()

            finish()
        } else {
            Toast.makeText(this, "O código do cliente deve ter 4 dígitos", Toast.LENGTH_LONG).show()
        }
    }


}
