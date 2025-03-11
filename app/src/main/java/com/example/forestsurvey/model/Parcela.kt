package com.example.forestsurvey.model

data class Parcela(
    var id: String = "",  // Use 'var' e forneça um valor padrão
    var nome: String = "",  // Use 'var' e forneça um valor padrão
    var userId: String = ""  // Use 'var' e forneça um valor padrão
) {
    // Construtor sem argumentos (obrigatório para o Firestore)
    constructor() : this("", "", "")
}