import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SubplotScreen(navController: NavHostController, parcelaNome: String, ruaId: Int) {
    // Lista de subplots
    val subplots = listOf(
        "Subplot 01: 0 - 20m",
        "Subplot 02: 20 - 40m",
        "Subplot 03: 40 - 60m",
        "Subplot 04: 60 - 80m",
        "Subplot 05: 80 - 100m"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título com nome da Parcela e Rua
        Text(
            text = "Parcela: $parcelaNome", // Corrigido: removido .text
            fontSize = 22.sp,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Rua: $ruaId",
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn para Subplots
        LazyColumn {
            items(subplots) { subplot ->
                val index = subplots.indexOf(subplot)
                val subplotId = "Rua${ruaId}_Subplot0${index + 1}" // Gera o ID do subplot

                // Card para cada subplot
                Card(
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa toda a largura disponível
                        .padding(8.dp)
                        .clickable {
                            // Navegar para a tela de preenchimento de dados
                            navController.navigate("preenchimento_dados/$parcelaNome/$ruaId/$subplotId")
                        },
                    border = BorderStroke(2.dp, Color.Gray), // Borda cinza
                    shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp), // Padding interno
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subplot,
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}