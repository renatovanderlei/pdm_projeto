package com.example.forestsurvey.fb

import com.example.forestsurvey.model.Dados
import com.google.firebase.firestore.FirebaseFirestore

class FBDados(private val db: FirebaseFirestore) {

    fun addDados(
        parcelaId: String,  // ID da parcela
        ruaId: String,      // ID da rua
        subplotId: String,  // ID do subplot
        dados: Dados,
        onComplete: (Boolean) -> Unit,
        onError: (Exception) -> Unit // Novo parâmetro para tratar erros
    ) {
        val docRef = db.collection("Parcela")
            .document(parcelaId)
            .collection("ruas")
            .document(ruaId)
            .collection("subplots")
            .document(subplotId)
            .collection("dados")
            .document()  // Gera um ID automático

        // Define o ID no objeto Dados
        val dadosComId = dados.copy(id = docRef.id)

        docRef.set(dadosComId)
            .addOnSuccessListener {
                onComplete(true)  // Sucesso
            }
            .addOnFailureListener { exception ->
                onComplete(false)  // Falha
                onError(exception) // Passa a exceção para o onError
            }
    }
}