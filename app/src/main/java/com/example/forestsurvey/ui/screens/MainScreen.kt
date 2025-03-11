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
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_fp),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Button for "Nova Coleta"
        Button(
            onClick = { navController.navigate("coleta") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(IntrinsicSize.Min),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Nova Coleta",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button for "Continuar última coleta"
        Button(
            onClick = { /* Lógica para continuar última coleta */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(IntrinsicSize.Min),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Continuar última coleta",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button for "Ver coletas passadas"
        Button(
            onClick = { /* Lógica para ver coletas passadas */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(IntrinsicSize.Min),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Ver coletas passadas",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button for "Minhas Parcelas" (new button added)
        Button(
            onClick = { navController.navigate("minhasParcelas") }, // Navegar para MinhasParcelasScreen
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(IntrinsicSize.Min),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text(
                text = "Minhas Parcelas",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}
