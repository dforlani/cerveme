package io.github.cerveme.crud_kotlin.model

import java.io.Serializable

class Pedido :Serializable{
    lateinit var pk_pedido_app: String
    lateinit var status: String
    lateinit var itens: ArrayList<Cardapio>
    lateinit var dt_pedido: String



    override fun toString()
    : String {
        return status
    }

    public fun esperandoResposta():Boolean{
        return status == "Enviado" || status == "Em Atendimento"
    }
}