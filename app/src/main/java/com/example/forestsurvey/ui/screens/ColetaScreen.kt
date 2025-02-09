import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.forestsurvey.ui.theme.AppTypography

@Composable
fun ColetaScreen(navController: NavHostController) {
    var parcelaNome by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Campo de entrada para nome da parcela
        OutlinedTextField(
            value = parcelaNome,
            onValueChange = { parcelaNome = it },
            label = { Text("Nome da Parcela") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Grid 2x5 de botões
        for (row in 0 until 5) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 1..2) {
                    val rua = row * 2 + col
                    val inicio = (rua - 1) * 10
                    val fim = rua * 10
                    Button(
                        onClick = { navController.navigate("detalhesRua/$rua") },
                        modifier = Modifier
                            .size(140.dp) // Botões quadrados maiores
                            .padding(8.dp), // Adicionar margens brancas ao botão
                        shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)), // Bordas levemente arredondadas
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text(
                            text = "Rua $rua:\n$inicio-$fim m",
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center, // Centralizar o texto
                            modifier = Modifier
                                .padding(vertical = 12.dp) // Adicionar mais espaço interno acima e abaixo do texto
                                .fillMaxSize()
                                .wrapContentHeight(Alignment.CenterVertically) // Centralizar verticalmente
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Mais espaço entre os botões
        }
    }
}
