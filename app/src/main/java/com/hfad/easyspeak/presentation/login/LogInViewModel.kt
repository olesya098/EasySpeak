package com.hfad.easyspeak.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern

class LogInViewModel(
    private val auth: FirebaseAuth
) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    companion object {
        private val EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        private const val MIN_PASSWORD_LENGTH = 6
    }

    fun login(email: String, password: String) {
        when {
            !isEmailValid(email) -> {
                _loginState.value = LoginState.Error("Invalid email address. Use the format name@name.name")
                return
            }
            password.length < MIN_PASSWORD_LENGTH -> {
                _loginState.value = LoginState.Error("Password must be at least 6 characters")
                return
            }
            else -> authenticateUser(email, password)
        }
    }

    private fun authenticateUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(mapFirebaseError(e))
            }
        }
    }

    private fun mapFirebaseError(e: Exception): String {
        return when {
            e.message?.contains("badly formatted") == true -> "Invalid email format"
            e.message?.contains("password is invalid") == true -> "Wrong password"
            e.message?.contains("no user record") == true -> "User not found"
            else -> "Login error: ${e.message ?: "Unknown error"}"
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }
}