package io.github.cerveme.crud_kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.unidade_cardapio.*



class ViewCardapioUnidade : View() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unidade_cardapio)

        btnMinus.setOnClickListener {
            editQuantidade.text = "5"
        }

        btnPlus.setOnClickListener {
            editQuantidade.text = "4"
        }
    }
}