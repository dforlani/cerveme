package io.github.cerveme.crud_kotlin.model

import java.io.Serializable

class Cardapio :Serializable{
    lateinit var fk_preco: String
    lateinit var denominacao: String
    lateinit var quantidade: String
    lateinit var preco_unitario: String


    override fun toString()
            : String {
        return denominacao
    }
}