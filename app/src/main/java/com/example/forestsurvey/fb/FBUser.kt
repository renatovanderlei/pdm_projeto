package com.example.forestsurvey.fb

import com.example.forestsurvey.model.User

class FBUser {
    var id: String? = null  // Novo campo para ID
    var name: String? = null
    var email: String? = null
    var parcelasCriadas: List<Map<String, String>>? = null

    fun toUser(): User {
        return User(
            id = id ?: "",  // Passa o ID corretamente
            name = name ?: "Usuário Desconhecido",
            email = email ?: "sem.email@example.com",
            parcelasCriadas = parcelasCriadas?.map { it.mapValues { entry -> entry.value.toString() } } ?: emptyList()  // Conversão para String
        )
    }
}

fun User.toFBUser(): FBUser {
    return FBUser().apply {
        id = this@toFBUser.id
        name = this@toFBUser.name
        email = this@toFBUser.email
        // A conversão não é necessária aqui se parcelasCriadas já estiver em Map<String, String>
        parcelasCriadas = this@toFBUser.parcelasCriadas
    }
}
