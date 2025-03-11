package com.example.forestsurvey.fb

import android.util.Log
import com.example.forestsurvey.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FBDatabase {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getCurrentUser() = auth.currentUser

    fun registerUser(name: String, email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = User(name = name, email = email)

                    auth.currentUser?.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                    )

                    db.collection("users").document(uid).set(user)
                        .addOnSuccessListener {
                            onComplete(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Erro ao registrar usuário: ${e.message}")
                            onComplete(false)
                        }
                } else {
                    Log.e("Firestore", "Erro no registro: ${task.exception?.message}")
                    onComplete(false)
                }
            }
    }

    fun addParcela(novaParcela: Parcela, userId: String) {
        val parcelaMap = mapOf(
            "id" to novaParcela.id,
            "nome" to novaParcela.nome,
            "userId" to novaParcela.userId
        )

        db.collection("Parcela").document(novaParcela.id).set(parcelaMap)
            .addOnSuccessListener {
                Log.d("Firestore", "Parcela ${novaParcela.nome} adicionada com sucesso.")

                val userRef = db.collection("users").document(userId)
                userRef.update("parcelasCriadas", FieldValue.arrayUnion(parcelaMap))
                    .addOnSuccessListener {
                        Log.d("Firestore", "Referência da parcela associada ao usuário com sucesso.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Erro ao associar parcela ao usuário: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao adicionar parcela: ${e.message}")
            }
    }

    fun deleteParcela(parcelaId: String, userId: String, onComplete: (Boolean) -> Unit) {
        val docRef = db.collection("Parcela").document(parcelaId)
        docRef.delete()
            .addOnSuccessListener {
                val userRef = db.collection("users").document(userId)
                val parcelaMap = mapOf(
                    "id" to parcelaId,
                    "nome" to "",
                    "userId" to userId
                )
                userRef.update("parcelasCriadas", FieldValue.arrayRemove(parcelaMap))
                    .addOnSuccessListener {
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Erro ao remover parcela do usuário: ${e.message}")
                        onComplete(false)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao deletar parcela: ${e.message}")
                onComplete(false)
            }
    }

    fun addRua(parcelaId: String, rua: Rua) {
        val docRef = db.collection("Parcela")
            .document(parcelaId)
            .collection("ruas")
            .document(rua.id)
        docRef.set(rua)
            .addOnSuccessListener {
                Log.d("Firestore", "Rua ${rua.id} adicionada com sucesso.")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao adicionar rua: ${e.message}")
            }
    }

    fun addSubplot(parcelaId: String, ruaId: String, subplot: Subplot) {
        val docRef = db.collection("Parcela")
            .document(parcelaId)
            .collection("ruas")
            .document(ruaId)
            .collection("subplots")
            .document(subplot.id)
        docRef.set(subplot)
            .addOnSuccessListener {
                Log.d("Firestore", "Subplot ${subplot.id} adicionado com sucesso.")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao adicionar subplot: ${e.message}")
            }
    }

    fun addDados(
        parcelaId: String,
        ruaId: String,
        subplotId: String,
        dados: Dados,
        onComplete: (Boolean) -> Unit
    ) {
        val user = auth.currentUser
        val anotador = Anotador(user?.uid ?: "", user?.displayName ?: user?.email ?: "Anônimo")

        val docRef = db.collection("Parcela")
            .document(parcelaId)
            .collection("ruas")
            .document(ruaId)
            .collection("subplots")
            .document(subplotId)
            .collection("dados")
            .document()

        val dadosComId = dados.copy(id = docRef.id, anotador = anotador)

        docRef.set(dadosComId)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao adicionar dados: ${e.message}")
                onComplete(false)
            }
    }

    fun getParcelas(userId: String, onComplete: (List<Parcela>?, Exception?) -> Unit) {
        if (userId.isBlank()) {
            onComplete(null, IllegalArgumentException("UserId não pode ser vazio"))
            return
        }

        val userRef = db.collection("users").document(userId)
        userRef.get().addOnSuccessListener { userDoc ->
            val user = userDoc.toObject(User::class.java)
            if (user != null) {
                val parcelasCriadas = user.parcelasCriadas
                val parcelasIds = parcelasCriadas.map { it["id"] ?: "" }

                if (parcelasIds.isNotEmpty()) {
                    db.collection("Parcela")
                        .whereIn("id", parcelasIds)
                        .get()
                        .addOnSuccessListener { result ->
                            val parcelas = result.documents.mapNotNull { it.toObject(Parcela::class.java) }
                            onComplete(parcelas, null)
                        }
                        .addOnFailureListener { exception ->
                            onComplete(null, exception)
                        }
                } else {
                    onComplete(emptyList(), null)
                }
            } else {
                onComplete(null, Exception("Usuário não encontrado"))
            }
        }.addOnFailureListener { exception ->
            onComplete(null, exception)
        }
    }

    interface Listener {
        fun onUserLoaded(user: User)
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun loadUser(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    listener?.onUserLoaded(user)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao carregar usuário: ${e.message}")
            }
    }
}