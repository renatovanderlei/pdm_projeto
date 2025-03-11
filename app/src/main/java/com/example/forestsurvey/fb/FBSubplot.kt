import com.example.forestsurvey.model.Subplot

class FBSubplot {
    var id: String? = null
    var ruaId: String? = null
    var parcelaId: String? = null
    var nome: String? = null

    // Função para converter para o modelo Subplot
    fun toSubplot(): Subplot {
        // Validando se os campos essenciais não são nulos
        return Subplot(
            id ?: throw IllegalArgumentException("id não pode ser nulo"),
            ruaId ?: throw IllegalArgumentException("ruaId não pode ser nulo"),
            parcelaId ?: throw IllegalArgumentException("parcelaId não pode ser nulo"),
            nome ?: "Sem nome"
        )
    }
}

// Função de extensão para converter de Subplot para FBSubplot
fun Subplot.toFBSubplot(): FBSubplot {
    return FBSubplot().also {
        it.id = this.id
        it.ruaId = this.ruaId
        it.parcelaId = this.parcelaId
        it.nome = this.nome
    }
}

// Lista de subplots pré-definidos
val subplotsPreDefinidos = listOf(
    "Subplot 1", "Subplot 2", "Subplot 3", "Subplot 4", "Subplot 5"
)

// Função para gerar subplots para uma rua específica
fun gerarSubplotsParaRua(ruaId: String, parcelaId: String): List<FBSubplot> {
    return subplotsPreDefinidos.mapIndexed { index, nomeSubplot ->
        FBSubplot().apply {
            id = "subplot_${index + 1}"
            this.ruaId = ruaId
            this.parcelaId = parcelaId
            nome = nomeSubplot
        }
    }
}