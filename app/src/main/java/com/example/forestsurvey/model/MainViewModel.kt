package com.example.forestsurvey.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forestsurvey.fb.FBDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val fbDB: FBDatabase) : ViewModel(), FBDatabase.Listener {

    private val _page = MutableStateFlow("home")
    val page: StateFlow<String> get() = _page

    fun setPage(page: String) {
        _page.value = page
    }

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    init {
        fbDB.setListener(this)
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }
}

class MainViewModelFactory(private val db: FBDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
