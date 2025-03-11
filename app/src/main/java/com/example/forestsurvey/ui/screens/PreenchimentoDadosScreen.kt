import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.forestsurvey.fb.FBDados
import com.example.forestsurvey.fb.FBDatabase
import com.example.forestsurvey.model.Anotador
import com.example.forestsurvey.model.Dados
import com.example.forestsurvey.model.Flag
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun PreenchimentoDadosScreen(
    parcela: String, // Nome da parcela passado como parâmetro
    rua: Int,
    subplot: String,
    navController: NavController,
    fbDados: FBDados = FBDados(FirebaseFirestore.getInstance())
) {
    // Variáveis de estado para o nome do usuário
    var userName by remember { mutableStateOf("") }

    // Obter o usuário logado
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }

    // Buscar o nome do usuário
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            // 1. Tenta usar o displayName
            val displayName = currentUser.displayName
            if (!displayName.isNullOrEmpty()) {
                userName = displayName
            } else {
                // 2. Se o displayName não estiver disponível, busca no Firestore
                getUserNameFromFirestore(
                    userId = currentUser.uid,
                    onSuccess = { name ->
                        userName = name
                    },
                    onError = { exception ->
                        // 3. Se não encontrar no Firestore, usa o email como fallback
                        userName = currentUser.email ?: "Usuário"
                    }
                )
            }
        }
    }

    // Criar o Anotador com o nome do usuário
    val anotador = remember(userName) {
        Anotador(
            userId = currentUser?.uid ?: "",
            nome = userName
        )
    }

    // Variáveis de estado para os campos de Dados
    var plotCode by remember { mutableStateOf(parcela) } // Usar o nome da parcela como valor inicial
    var newTagNo by remember { mutableStateOf(0) }
    var newStemGrouping by remember { mutableStateOf("") }
    var t1 by remember { mutableStateOf(0) }
    var t2 by remember { mutableStateOf(0) }
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var family by remember { mutableStateOf("") }
    var originalIdentification by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var diametro30cm by remember { mutableStateOf(0.0) }
    var diametro130cm by remember { mutableStateOf(0.0) }
    var altura by remember { mutableStateOf(0.0) }
    var observacoes by remember { mutableStateOf("") }
    var flagsSelecionadas by remember { mutableStateOf<List<Flag>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            // Mostrar informações da parcela, rua e subplot
            Text(
                text = "Parcela: $parcela, Rua: $rua, Subplot: $subplot",
                style = MaterialTheme.typography.titleLarge
            )

            // Campos para preencher
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    // Campo Plot Code (não editável)
                    OutlinedTextField(
                        value = plotCode,
                        onValueChange = { }, // Impede a edição
                        label = { Text("Plot Code") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false // Desabilita o campo
                    )
                    CampoNumero("New Tag No", newTagNo.toDouble(), { newTagNo = it.toInt() })
                    CampoTexto("New Stem Grouping", newStemGrouping, { newStemGrouping = it })
                    CampoNumero("T1", t1.toDouble(), { t1 = it.toInt() })
                    CampoNumero("T2", t2.toDouble(), { t2 = it.toInt() })
                    CampoNumero("X", x.toDouble(), { x = it.toFloat() })
                    CampoNumero("Y", y.toDouble(), { y = it.toFloat() })
                    CampoTexto("Family", family, { family = it })
                    CampoTexto("Original Identification", originalIdentification, { originalIdentification = it })
                    CampoTexto("Species", species, { species = it })
                    CampoNumero("Diâmetro 30 cm", diametro30cm, { diametro30cm = it })
                    CampoNumero("Diâmetro 130 cm", diametro130cm, { diametro130cm = it })
                    CampoNumero("Altura", altura, { altura = it })
                    CampoTexto("Observações", observacoes, { observacoes = it })

                    // Exibir as flags em um grid 5x6
                    Text("Flags:", style = MaterialTheme.typography.bodyLarge)
                    Column {
                        val flags = Flag.values()
                        val rows = 5
                        val cols = 6

                        for (i in 0 until rows) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                for (j in 0 until cols) {
                                    val index = i * cols + j
                                    if (index < flags.size) {
                                        val flag = flags[index]
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
            }

            // Botões para salvar ou cancelar
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (family.isEmpty() || species.isEmpty()) { // Remova a verificação de plotCode
                            showSnackbarMessage = "Preencha todos os campos obrigatórios."
                            return@Button
                        }

                        isLoading = true
                        val dados = Dados(
                            PlotCode = plotCode, // Usa o nome da parcela como PlotCode
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
                            flags = flagsSelecionadas,
                            anotador = anotador,
                            timestamp = Timestamp.now()
                        )

                        fbDados.addDados(
                            parcelaId = parcela,
                            ruaId = rua.toString(),
                            subplotId = subplot,
                            dados = dados,
                            onComplete = { success ->
                                isLoading = false
                                if (success) {
                                    showSnackbarMessage = "Dados salvos com sucesso!"
                                    navController.popBackStack()
                                } else {
                                    showSnackbarMessage = "Erro ao salvar dados."
                                }
                            },
                            onError = { exception -> // Agora o onError é reconhecido
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

    // Exibir mensagem de erro se necessário
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
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun CampoNumero(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value.toString(),
        onValueChange = { newValue ->
            onValueChange(newValue.toDoubleOrNull() ?: 0.0)
        },
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

fun getUserNameFromFirestore(
    userId: String,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("users").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val name = document.getString("name") ?: ""
                onSuccess(name)
            } else {
                onError(Exception("Usuário não encontrado no Firestore"))
            }
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}