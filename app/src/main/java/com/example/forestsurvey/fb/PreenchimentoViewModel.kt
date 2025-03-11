import androidx.lifecycle.ViewModel
import com.example.forestsurvey.model.Dados
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PreenchimentoViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Função para salvar dados
    fun salvarDados(
        parcela: String,
        rua: String,
        subplot: String,
        dados: Dados,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return

        // Salvar os dados no Firestore
        db.collection("Parcela")
            .document(parcela)
            .collection("ruas")
            .document(rua)
            .collection("subplots")
            .document(subplot)
            .collection("dados")
            .document()  // Gera um ID automático
            .set(dados)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    // Função para carregar dados
    fun carregarDados(
        parcela: String,
        rua: String,
        subplot: String,
        onResult: (List<Dados>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return

        // Carregar os dados do Firestore
        db.collection("Parcela")
            .document(parcela)
            .collection("ruas")
            .document(rua)
            .collection("subplots")
            .document(subplot)
            .collection("dados")
            .get()
            .addOnSuccessListener { result ->
                val dados = result.documents.mapNotNull { it.toObject(Dados::class.java) }
                onResult(dados)
            }
            .addOnFailureListener { exception -> onError(exception) }
    }
}