import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.forestsurvey.model.Parcela
import com.example.forestsurvey.model.User
import java.util.UUID

@Composable
fun ColetaScreen(
    navController: NavHostController,
    usuarioLogado: User,
    onAdicionarParcela: (Parcela, String) -> Unit
) {
    var parcelaNome by remember { mutableStateOf("") }
    var parcelaCriada by remember { mutableStateOf<Parcela?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = parcelaNome,
            onValueChange = { parcelaNome = it },
            label = { Text("Nome da Parcela", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 18.sp
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para criar parcela
        Button(
            onClick = {
                if (parcelaNome.isNotBlank()) {
                    val novaParcela = Parcela(
                        id = UUID.randomUUID().toString(),
                        nome = parcelaNome.trim(),
                        userId = usuarioLogado.id
                    )
                    onAdicionarParcela(novaParcela, usuarioLogado.id)
                    parcelaCriada = novaParcela
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = parcelaNome.isNotBlank(),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Criar Parcela", fontSize = 18.sp)
        }

        // Seção das ruas (aparece apenas após criar a parcela)
        parcelaCriada?.let { parcela ->
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Parcela: ${parcela.nome}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Grid de ruas no estilo anterior (5 linhas x 2 colunas)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0 until 5) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 1..2) {
                            val rua = row * 2 + col
                            RuaButtonStyled(rua = rua, parcela = parcela, navController)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun RuaButtonStyled(rua: Int, parcela: Parcela, navController: NavHostController) {
    val inicio = (rua - 1) * 10
    val fim = rua * 10

    // Definição de tamanho fixo para todos os botões
    val buttonWidth = 160.dp
    val buttonHeight = 80.dp

    OutlinedButton(
        onClick = {
            navController.navigate("subplots/${parcela.nome}/$rua") {
                launchSingleTop = true
            }
        },
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Rua $rua",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$inicio-$fim m",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}