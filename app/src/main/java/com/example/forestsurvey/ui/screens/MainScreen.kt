import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forestsurvey.R
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Mover a logo para mais cima na tela
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_fp), // Substitua pelo ID correto da sua imagem
            contentDescription = "Logo",
            modifier = Modifier
                //.size(150.dp) // Ajuste o tamanho da imagem conforme necessário
                .padding(bottom = 8.dp), // Diminuir o espaçamento entre a logo e o texto
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp)) // Adicionar um espaçamento adicional entre a logo e os botões

        Button(
            onClick = { navController.navigate("coleta") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Adicionar margens brancas ao botão
                .height(IntrinsicSize.Min), // Ajustar a altura do botão automaticamente
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)), // Bordas levemente arredondadas
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Nova Coleta",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp) // Adicionar mais espaço interno acima e abaixo do texto
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // Mais espaço entre os botões
        Button(
            onClick = { /* Lógica para continuar última coleta */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Adicionar margens brancas ao botão
                .height(IntrinsicSize.Min), // Ajustar a altura do botão automaticamente
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)), // Bordas levemente arredondadas
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Continuar última coleta",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp) // Adicionar mais espaço interno acima e abaixo do texto
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // Mais espaço entre os botões
        Button(
            onClick = { /* Lógica para ver coletas passadas */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Adicionar margens brancas ao botão
                .height(IntrinsicSize.Min), // Ajustar a altura do botão automaticamente
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)), // Bordas levemente arredondadas
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Ver coletas passadas",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp) // Adicionar mais espaço interno acima e abaixo do texto
            )
        }
    }
}
