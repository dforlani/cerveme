package io.github.cerveme.crud_kotlin.model

import java.io.Serializable

class Pedido :Serializable{
    lateinit var pk_pedido_app: String
    lateinit var status: String


    override fun toString()
    : String {
        return status
    }
}