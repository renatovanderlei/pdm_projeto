package com.example.forestsurvey.fb

import com.example.forestsurvey.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FBDatabase {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var listener: Listener? = null

    fun getCurrentUser() = auth.currentUser

    fun loadCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val refCurrUser = db.collection("users").document(currentUser.uid)
            refCurrUser.get().addOnSuccessListener {
                val fbUser = it.toObject(FBUser::class.java)
                fbUser?.let { user -> listener?.onUserLoaded(user.toUser()) }
            }.addOnFailureListener {
                // Log de erro (Opcional: adicionar tratamento de erro)
            }
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
        loadCurrentUser()
    }

    fun register(user: User) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).set(user.toFBUser())
    }

    interface Listener {
        fun onUserLoaded(user: User)
    }
}
