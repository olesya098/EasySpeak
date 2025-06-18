package com.hfad.easyspeak.presentation.mainscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    //хранение текущего пользователя Firebase
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()
    private val _user = MutableStateFlow<UserModel?>(null)
    val user = _user.asStateFlow()

    // инициализация viewmodel сразу при создании ViewModel (сразу запрашиваем текущего пользователя)
    init {
        getCurrentUser()
    }

    //получение текущего пользователя
    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = UserService().getCurrentUser()
        }
    }

    //получение данных из Firebase пользователя
    fun getUser(userFirebase: FirebaseUser) {
        viewModelScope.launch {
            try {
                _user.value = UserService().getUser(userFirebase)


            } catch (e: Exception) {
                Log.e("My", e.toString())
            }
        }

    }


}