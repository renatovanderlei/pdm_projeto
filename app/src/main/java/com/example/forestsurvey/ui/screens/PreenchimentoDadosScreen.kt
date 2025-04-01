import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.forestsurvey.fb.FBDados
import com.example.forestsurvey.model.Anotador
import com.example.forestsurvey.model.Dados
import com.example.forestsurvey.model.Flag
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PreenchimentoDadosScreen(
    parcela: String,
    rua: Int,
    subplot: String,
    navController: NavController,
    fbDados: FBDados = FBDados(FirebaseFirestore.getInstance())
) {
    // User state
    var userName by remember { mutableStateOf("") }
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }

    // Photo state
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var fotoUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Form fields state
    var plotCode by remember { mutableStateOf(parcela) }
    var newTagNo by remember { mutableStateOf(0) }
    var newStemGrouping by remember { mutableStateOf("") }
    var t1 by remember { mutableStateOf(0) }
    var t2 by remember { mutableStateOf(0) }
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var family by remember { mutableStateOf("") }
    var originalIdentification by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var diametro30cm by remember { mutableStateOf(0f) }
    var diametro130cm by remember { mutableStateOf(0f) }
    var altura by remember { mutableStateOf(0f) }
    var observacoes by remember { mutableStateOf("") }
    var flagsSelecionadas by remember { mutableStateOf<List<Flag>>(emptyList()) }

    // UI state
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            fotoUri?.let { uri ->
                uploadFoto(context, uri, newTagNo) { url ->
                    fotoUrl = url
                }
            }
        }
    }

    // Load user name
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            userName = user.displayName ?: user.email ?: "Usuário"
        }
    }

    val anotador = remember(userName) {
        Anotador(
            userId = currentUser?.uid ?: "",
            nome = userName
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Parcela: $parcela, Subplot: $subplot",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    OutlinedTextField(
                        value = plotCode,
                        onValueChange = {},
                        label = { Text("Plot Code") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )

                    CampoInteiro("New Tag No", newTagNo, { newTagNo = it })
                    CampoTexto("New Stem Grouping", newStemGrouping, { newStemGrouping = it })
                    CampoInteiro("T1", t1, { t1 = it })
                    CampoInteiro("T2", t2, { t2 = it })
                    CampoFloat("X", x, { x = it })
                    CampoFloat("Y", y, { y = it })
                    CampoFloat("Diâmetro 30 cm", diametro30cm, { diametro30cm = it })
                    CampoFloat("Diâmetro 130 cm", diametro130cm, { diametro130cm = it })
                    CampoFloat("Altura", altura, { altura = it })
                    CampoTexto("Family", family, { family = it })
                    CampoTexto("Original Identification", originalIdentification, { originalIdentification = it })
                    CampoTexto("Species", species, { species = it })
                    CampoTexto("Observações", observacoes, { observacoes = it })

                    // Camera section
                    Button(
                        onClick = {
                            try {
                                val photoFile = createImageFile(context) ?: run {
                                    Toast.makeText(context, "Não foi possível criar arquivo para foto", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val uri = try {
                                    FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.provider",
                                        photoFile
                                    )
                                } catch (e: IllegalArgumentException) {
                                    Toast.makeText(context, "Erro de configuração do FileProvider", Toast.LENGTH_LONG).show()
                                    return@Button
                                }

                                fotoUri = uri

                                val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                                    putExtra(MediaStore.EXTRA_OUTPUT, uri)
                                    addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                }

                                if (captureIntent.resolveActivity(context.packageManager) != null) {
                                    cameraLauncher.launch(captureIntent)
                                } else {
                                    Toast.makeText(context, "Nenhum app de câmera encontrado", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                e.printStackTrace()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Tirar foto"
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Tirar Foto da Árvore")
                    }

                    // Flags section
                    Text("Flags:", style = MaterialTheme.typography.bodyLarge)
                    Column {
                        val flags = Flag.values().toList()
                        val chunkedFlags = flags.chunked(6)

                        chunkedFlags.forEach { rowFlags ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowFlags.forEach { flag ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Checkbox(
                                            checked = flagsSelecionadas.contains(flag),
                                            onCheckedChange = { isChecked ->
                                                flagsSelecionadas = if (isChecked) {
                                                    flagsSelecionadas + flag
                                                } else {
                                                    flagsSelecionadas - flag
                                                }
                                            }
                                        )
                                        Text(text = flag.name, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (newTagNo <= 0 || species.isEmpty()) {
                            showSnackbarMessage = "Preencha todos os campos obrigatórios."
                            return@Button
                        }

                        isLoading = true
                        val dados = Dados(
                            PlotCode = plotCode,
                            newTagNo = newTagNo,
                            newStemGrouping = newStemGrouping,
                            t1 = t1,
                            t2 = t2,
                            X = x,
                            Y = y,
                            family = family,
                            originalIdentification = originalIdentification,
                            species = species,
                            diametro30cm = diametro30cm,
                            diametro130cm = diametro130cm,
                            altura = altura,
                            observacoes = observacoes,
                            fotos = if (fotoUrl.isNotEmpty()) listOf(fotoUrl) else emptyList(),
                            flags = flagsSelecionadas,
                            anotador = anotador,
                            timestamp = Timestamp.now()
                        )

                        fbDados.addDados(
                            parcelaId = parcela,
                            ruaId = rua.toString(),
                            subplotId = subplot,
                            dados = dados.copy(id = newTagNo.toString()),
                            onComplete = { success ->
                                isLoading = false
                                showSnackbarMessage = if (success) {
                                    navController.popBackStack()
                                    "Dados salvos com sucesso!"
                                } else {
                                    "Erro ao salvar dados."
                                }
                            },
                            onError = { exception ->
                                isLoading = false
                                showSnackbarMessage = "Erro: ${exception.message}"
                            }
                        )
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Salvar")
                    }
                }
            }
        }
    }

    LaunchedEffect(showSnackbarMessage) {
        showSnackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            showSnackbarMessage = null
        }
    }
}

@Composable
fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(12.dp)
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun CampoInteiro(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = value.toString(),
            onValueChange = { newValue ->
                onValueChange(newValue.toIntOrNull() ?: 0)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(12.dp)
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun CampoFloat(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember(value) {
        mutableStateOf(
            if (value % 1 == 0f) value.toInt().toString()
            else value.toString()
        )
    }

    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = textValue,
            onValueChange = { newValue ->
                textValue = newValue
                val floatValue = newValue.toFloatOrNull() ?: 0f
                onValueChange(floatValue)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(12.dp)
                ) {
                    innerTextField()
                }
            }
        )
    }
}

private fun createImageFile(context: Context): File? {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir

        if (!storageDir.exists() && !storageDir.mkdirs()) {
            return null
        }

        File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            createNewFile()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun uploadFoto(
    context: Context,
    imageUri: Uri,
    tagNo: Int,
    onSuccess: (String) -> Unit
) {
    val storageRef = Firebase.storage.reference
    val fotoRef = storageRef.child("arvores/${tagNo}_${UUID.randomUUID()}.jpg")

    fotoRef.putFile(imageUri)
        .addOnSuccessListener {
            fotoRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Erro ao enviar foto: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}