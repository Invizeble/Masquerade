package com.invize.masquerade.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (!validate(email, password)) return
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()
                _uiState.value = AuthUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = mapError(e.message))
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _uiState.value = AuthUiState(error = "Пароли не совпадают")
            return
        }
        if (!validate(email, password)) return
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                auth.createUserWithEmailAndPassword(email.trim(), password).await()
                _uiState.value = AuthUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = mapError(e.message))
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun validate(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                _uiState.value = AuthUiState(error = "Введите email")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                _uiState.value = AuthUiState(error = "Некорректный email")
                false
            }
            password.length < 6 -> {
                _uiState.value = AuthUiState(error = "Пароль минимум 6 символов")
                false
            }
            else -> true
        }
    }

    private fun mapError(message: String?): String = when {
        message == null -> "Неизвестная ошибка"
        message.contains("email address is already in use") -> "Email уже зарегистрирован"
        message.contains("INVALID_LOGIN_CREDENTIALS") ||
                message.contains("password is invalid") ||
                message.contains("no user record") -> "Неверный email или пароль"
        message.contains("network") -> "Нет подключения к интернету"
        message.contains("too-many-requests") -> "Слишком много попыток. Подождите"
        else -> "Ошибка. Попробуйте ещё раз"
    }
}