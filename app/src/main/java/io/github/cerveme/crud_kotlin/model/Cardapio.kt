package io.github.cerveme.crud_kotlin.model

import java.io.Serializable

class Cardapio :Serializable{
    lateinit var pk_preco: String
    lateinit var denominacao: String
    lateinit var quantidade: String


    override fun toString()
    : String {
        return denominacao
    }
}