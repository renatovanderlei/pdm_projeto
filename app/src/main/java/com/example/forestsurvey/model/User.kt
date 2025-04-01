package com.example.forestsurvey.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val parcelasCriadas: List<Map<String, String>> = emptyList()
) {
    // Construtor secundário caso precise criar um usuário sem parâmetros
    constructor() : this("", "", "", emptyList())
}
