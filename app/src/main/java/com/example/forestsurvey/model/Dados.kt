package com.example.forestsurvey.model

import com.google.firebase.Timestamp


// Definição da enum class para as flags em minúsculas
enum class Flag {
    a, b, c, d, e, f, g, h, i, j, k, l, m, n,
    o, p, q, r, s, t, u, v, w, x, y, z
}

data class Dados(
    val id: String = "",
    val PlotCode: String,
    val newTagNo: Int,
    val newStemGrouping: String,
    val t1: Int,
    val t2: Int,
    val X: Float,
    val Y: Float,
    val family: String,
    val originalIdentification: String,
    val species: String,
    val diametro30cm: Float,
    val diametro130cm: Float,
    val altura: Float,
    val observacoes: String,
    val flags: List<Flag>,  // Agora é uma lista de flags
    val anotador: Anotador,
    val fotos: List<String> = emptyList(), // Lista de IDs/referências das fotos
    val timestamp: Timestamp = Timestamp.now()
)
