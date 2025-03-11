package com.example.forestsurvey.fb

import com.example.forestsurvey.model.Parcela

class FBParcela {
    var id: String? = null
    var nome: String? = null
    var userId: String? = null  // Para registrar o usuário que criou a parcela
    //var usuariosAutorizados: List<String> = emptyList()  // Lista de usuários autorizados

    fun toParcela(): Parcela {
        return Parcela(
            id = id ?: "",
            nome = nome ?: "",
            userId = userId ?: "",  // Atribuindo o userId
            //usuariosAutorizados = usuariosAutorizados  // Incluindo a lista de usuários autorizados
        )
    }
}

fun Parcela.toFBParcela(): FBParcela {
    return FBParcela().apply {
        id = this@toFBParcela.id
        nome = this@toFBParcela.nome
        userId = this@toFBParcela.userId  // Incluindo o userId
        //usuariosAutorizados = this@toFBParcela.usuariosAutorizados  // Incluindo a lista de usuários autorizados
    }
}
