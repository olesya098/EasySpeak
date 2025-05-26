// LogInViewModel.kt
package com.hfad.easyspeak.presentation.LogIn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern

class LogInViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    // Состояния UI
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    // Регулярное выражение для проверки email (формат name@name.name)
    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    fun login(email: String, password: String) {
        // Валидация email
        if (!isEmailValid(email)) {
            _loginState.value = LoginState.Error("Invalid email address. Use the format name@name.name")
            return
        }

        // Валидация пароля
        if (password.length < 6) {
            _loginState.value = LoginState.Error("Пароль должен содержать минимум 6 символов")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("badly formatted") == true -> "Некорректный формат email"
                    e.message?.contains("password is invalid") == true -> "Неверный пароль"
                    e.message?.contains("no user record") == true -> "Пользователь не найден"
                    else -> "Ошибка при входе: ${e.message ?: "Неизвестная ошибка"}"
                }
                _loginState.value = LoginState.Error(errorMessage)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }
}