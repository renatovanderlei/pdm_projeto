package com.example.forestsurvey.model

data class User(
    var id: String = "",  // Use 'var' e forneça um valor padrão
    var name: String = "",  // Use 'var' e forneça um valor padrão
    var email: String = "",  // Use 'var' e forneça um valor padrão
    var parcelasCriadas: List<Map<String, String>> = emptyList()  // Use 'var' e forneça um valor padrão
) {
    // Construtor sem argumentos (obrigatório para o Firestore)
    constructor() : this("", "", "", emptyList())
}
