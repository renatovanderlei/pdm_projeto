package com.example.forestsurvey.model

data class Parcela(
    val id: String = "",
    val nome: String = "",
    val userId: String = ""
) {
    // Construtor secundário caso precise criar uma parcela sem passar parâmetros
    constructor() : this("", "", "")
}
