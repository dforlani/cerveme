package io.github.cerveme.crud_kotlin.model

import android.graphics.Color
import java.io.Serializable

class Pedido :Serializable{
    lateinit var pk_pedido_app: String
    lateinit var status: String
    lateinit var itens: ArrayList<Cardapio>
     var dt_pedido: String = ""



    override fun toString()
    : String {
        return status
    }

    public fun esperandoResposta():Boolean{
        return status == "Enviado" || status == "Em Atendimento"
    }

    public fun getColor():Int{
       return when(status) {
            "Cancelado pelo Atendente" -> Color.RED
           "Enviado" -> Color.GREEN
           "Em Atendimento" -> Color.GRAY
           "Pronto" -> Color.BLUE
            else -> Color.BLACK
        }
    }


    public fun isCancelado():Boolean{
        return status == "Cancelado pelo Atendente"
    }
}