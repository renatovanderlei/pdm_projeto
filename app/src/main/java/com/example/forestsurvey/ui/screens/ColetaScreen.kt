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
import com.example.forestsurvey.model.Parcela
import com.example.forestsurvey.model.User
import java.util.UUID

@Composable
fun ColetaScreen(
    navController: NavHostController,
    usuarioLogado: User,
    onAdicionarParcela: (Parcela, String) -> Unit // Função para adicionar uma nova parcela (agora recebe userId)
) {
    var parcelaNome by remember { mutableStateOf(TextFieldValue()) }
    var parcelaCriada by remember { mutableStateOf<Parcela?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Input field for parcel name
        OutlinedTextField(
            value = parcelaNome,
            onValueChange = { parcelaNome = it },
            label = { Text("Nome da Parcela") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Button to create a new parcela
        Button(
            onClick = {
                if (parcelaNome.text.isNotEmpty()) {
                    val novaParcela = Parcela(
                        id = UUID.randomUUID().toString(),
                        nome = parcelaNome.text,
                        userId = usuarioLogado.id // Já está no objeto Parcela
                    )
                    onAdicionarParcela(novaParcela, usuarioLogado.id)  // Passa o userId aqui
                    parcelaCriada = novaParcela // Atualiza o estado da parcela criada
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Criar Parcela")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Exibe as ruas se uma parcela foi criada
        parcelaCriada?.let { parcela ->
            Text(
                text = "Última parcela criada: ${parcela.nome}",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Grid de botões para as ruas
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
                            onClick = {
                                navController.navigate("subplots/${parcela.nome}/$rua")
                            },
                            modifier = Modifier
                                .size(140.dp)
                                .padding(8.dp),
                            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
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
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .fillMaxSize()
                                    .wrapContentHeight(Alignment.CenterVertically)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}