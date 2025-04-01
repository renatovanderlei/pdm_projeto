package com.example.forestsurvey.fb

import com.example.forestsurvey.model.Dados
import com.google.firebase.firestore.FirebaseFirestore

class FBDados(private val db: FirebaseFirestore) {

    fun addDados(
        parcelaId: String,
        ruaId: String,
        subplotId: String,
        dados: Dados,
        onComplete: (Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        // Verificação básica do newTagNo
        if (dados.newTagNo <= 0) {
            onError(IllegalArgumentException("New Tag No deve ser maior que zero"))
            onComplete(false)
            return
        }

        // Cria a referência do documento usando newTagNo como ID
        val docRef = db.collection("Parcela")
            .document(parcelaId)
            .collection("ruas")
            .document(ruaId)
            .collection("subplots")
            .document(subplotId)
            .collection("dados")
            .document(dados.newTagNo.toString())

        // Verifica se já existe um documento com esse newTagNo
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                onError(Exception("Já existe um registro com o New Tag No ${dados.newTagNo}"))
                onComplete(false)
            } else {
                // Se não existir, procede com o salvamento
                docRef.set(dados.copy(id = dados.newTagNo.toString()))
                    .addOnSuccessListener {
                        onComplete(true)
                    }
                    .addOnFailureListener { exception ->
                        onError(exception)
                        onComplete(false)
                    }
            }
        }.addOnFailureListener { exception ->
            onError(exception)
            onComplete(false)
        }
    }
}

//class FBDados(private val db: FirebaseFirestore) {
//
//    fun addDados(
//        parcelaId: String,  // ID da parcela
//        ruaId: String,      // ID da rua
//        subplotId: String,  // ID do subplot
//        dados: Dados,
//        onComplete: (Boolean) -> Unit,
//        onError: (Exception) -> Unit // Novo parâmetro para tratar erros
//    ) {
//        val docRef = db.collection("Parcela")
//            .document(parcelaId)
//            .collection("ruas")
//            .document(ruaId)
//            .collection("subplots")
//            .document(subplotId)
//            .collection("dados")
//            .document()  // Gera um ID automático
//
//        // Define o ID no objeto Dados
//        val dadosComId = dados.copy(id = docRef.id)
//
//        docRef.set(dadosComId)
//            .addOnSuccessListener {
//                onComplete(true)  // Sucesso
//            }
//            .addOnFailureListener { exception ->
//                onComplete(false)  // Falha
//                onError(exception) // Passa a exceção para o onError
//            }
//    }
//}