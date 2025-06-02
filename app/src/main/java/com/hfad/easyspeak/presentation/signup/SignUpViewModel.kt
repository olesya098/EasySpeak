package com.hfad.easyspeak.presentation.signup

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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

class SignUpViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    val isPasswordVisible = mutableStateOf(false)
    val isConfirmPasswordVisible = mutableStateOf(false)

    fun togglePasswordVisibility() {
        isPasswordVisible.value = !isPasswordVisible.value
    }

    fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible.value = !isConfirmPasswordVisible.value
    }
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    var email = MutableStateFlow("")
    var password = MutableStateFlow("")
    var confirmPassword = MutableStateFlow("")
    var termsAccepted = MutableStateFlow(false)

    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    sealed class SignUpState {
        object Idle : SignUpState()
        object Loading : SignUpState()
        object Success : SignUpState()
        data class Error(val message: String) : SignUpState()
    }

    fun signUp() {
        if (!isEmailValid(email.value)) {
            _signUpState.value = SignUpState.Error("Некорректный email. Используйте формат name@name.name")
            return
        }

        if (password.value.length < 6) {
            _signUpState.value = SignUpState.Error("Пароль должен содержать минимум 6 символов")
            return
        }

        if (password.value != confirmPassword.value) {
            _signUpState.value = SignUpState.Error("Пароли не совпадают")
            return
        }

        if (!termsAccepted.value) {
            _signUpState.value = SignUpState.Error("Необходимо принять условия использования")
            return
        }

        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                auth.createUserWithEmailAndPassword(email.value, password.value).await()
                _signUpState.value = SignUpState.Success
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("email address is already in use") == true ->
                        "Этот email уже зарегистрирован"
                    e.message?.contains("badly formatted") == true ->
                        "Некорректный формат email"
                    e.message?.contains("password is invalid") == true ->
                        "Пароль слишком слабый"
                    else -> "Ошибка при регистрации: ${e.message ?: "Неизвестная ошибка"}"
                }
                _signUpState.value = SignUpState.Error(errorMessage)
                Log.e("SignUpViewModel", "Registration error", e)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }

    fun toggleTermsAccepted() {
        termsAccepted.value = !termsAccepted.value
    }
}