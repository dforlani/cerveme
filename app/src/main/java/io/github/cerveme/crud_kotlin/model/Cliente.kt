package io.github.cerveme.crud_kotlin.model

class Cliente {
    var pk_cliente: String? = null
    var nome: String? = null
    constructor( pk_cliente: String, nome:String) {
        this.pk_cliente = pk_cliente
        this.nome = nome
    }

}