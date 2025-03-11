package com.example.forestsurvey.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forestsurvey.fb.FBDatabase
import com.google.firebase.auth.FirebaseUser

class MainViewModel(private val fbDB: FBDatabase) : ViewModel(), FBDatabase.Listener {

    // Estado do usuário logado
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> get() = _user

    // Estado das parcelas (imutável para a UI)
    private val _parcelas = mutableStateOf<List<Parcela>>(emptyList())
    val parcelas: State<List<Parcela>> get() = _parcelas

    init {
        fbDB.setListener(this)  // Define o listener para atualizações
        loadUser()  // Carrega o usuário logado
    }

    // Carrega o usuário logado e suas parcelas
    private fun loadUser() {
        val firebaseUser: FirebaseUser? = fbDB.getCurrentUser()
        firebaseUser?.let {
            _user.value = User(name = it.displayName ?: "", email = it.email ?: "")
            loadParcelas(it.uid)  // Usando UID para segurança
        }
    }

    // Carrega as parcelas associadas ao usuário
    private fun loadParcelas(userId: String) {
        if (userId.isBlank()) {
            println("Erro: userId não pode ser vazio")
            return
        }
        fbDB.getParcelas(userId) { parcelas, exception ->
            _parcelas.value = parcelas ?: emptyList()
            exception?.let { println("Erro ao carregar parcelas: ${it.message}") }
        }
    }

    fun onAdicionarParcela(parcela: Parcela, userId: String) {
        if (userId.isBlank()) {
            println("Erro: userId não pode ser vazio")
            return
        }
        fbDB.addParcela(parcela, userId)
        loadParcelas(userId)
    }




    // Atualiza o usuário ao receber do listener
    override fun onUserLoaded(user: User) {
        _user.value = user
    }
}

// Factory para criação do ViewModel
class MainViewModelFactory(private val db: FBDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
