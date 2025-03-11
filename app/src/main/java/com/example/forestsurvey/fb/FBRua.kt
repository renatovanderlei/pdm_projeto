import com.example.forestsurvey.model.Rua

class FBRua {
    var id: String? = null
    var parcelaId: String? = null
    var nome: String? = null

    fun toRua(): Rua {
        return Rua(
            id ?: throw IllegalArgumentException("id não pode ser nulo"),
            parcelaId ?: throw IllegalArgumentException("parcelaId não pode ser nulo"),
            nome ?: "Sem nome"
        )
    }
}

fun Rua.toFBRua(): FBRua {
    return FBRua().apply {
        id = this@toFBRua.id
        parcelaId = this@toFBRua.parcelaId
        nome = this@toFBRua.nome
    }
}


// Lista imutável de 10 ruas
val ruasPreDefinidas = listOf(
    "Rua 1", "Rua 2", "Rua 3", "Rua 4", "Rua 5",
    "Rua 6", "Rua 7", "Rua 8", "Rua 9", "Rua 10"
)

// Função para gerar as 10 ruas para uma parcela específica
fun gerarRuasParaParcela(parcelaId: String): List<FBRua> {
    return ruasPreDefinidas.mapIndexed { index, nomeRua ->
        FBRua().apply {
            id = "rua_${index + 1}"
            this.parcelaId = parcelaId
            nome = nomeRua
        }
    }
}