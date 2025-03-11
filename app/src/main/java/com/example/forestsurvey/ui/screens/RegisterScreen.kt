import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.navigation.NavHostController
import com.example.forestsurvey.R
import com.example.forestsurvey.fb.FBDatabase
import com.example.forestsurvey.model.User

@Composable
fun RegisterScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmpassword by rememberSaveable { mutableStateOf("") }
    val activity = LocalContext.current as? Activity

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_fp), // Substitua pelo ID correto da sua imagem
            contentDescription = "Logo",
            modifier = Modifier
                //.size(250.dp) // Ajuste o tamanho da imagem conforme necessário
                .padding(bottom = 8.dp), // Diminuir o espaçamento entre a logo e o texto
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Registre-se!",
            fontSize = 40.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = name,
            label = { Text(text = "Digite seu nome", fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold) },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { name = it },
            textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail: nome@email.com", fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold) },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { email = it },
            textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha (mínimo 6 caracteres)", fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold) },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            value = confirmpassword,
            label = { Text(text = "Repita sua senha", fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold) },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { confirmpassword = it },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.size(24.dp))

        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    val fbDatabase = FBDatabase()
                    fbDatabase.registerUser(name, email, password) { success ->
                        if (success) {
                            Toast.makeText(activity, "Registro OK!", Toast.LENGTH_LONG).show()
                            navController.navigate("main")
                        } else {
                            Toast.makeText(activity, "Registro FALHOU!", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                enabled = name.isNotEmpty() && email.isNotEmpty() &&
                        password.isNotEmpty() && password == confirmpassword,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text("Registrar", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }


            Button(
                onClick = {
                    name = ""; email = ""; password = ""; confirmpassword = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text("Limpar", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text("Cancelar", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
