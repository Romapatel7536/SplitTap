package com.roma.example.splittap.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roma.example.splittap.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val MinimumPasswordLength = 8

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(isLoggedIn = repository.getCurrentUser() != null)
    )
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                repository.login(email.trim(), password)
                _uiState.value = AuthUiState(isLoggedIn = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String) {
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                repository.register(email.trim(), password)
                _uiState.value = AuthUiState(isLoggedIn = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = e.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState(isLoggedIn = false)
    }

    private fun validate(email: String, password: String): Boolean {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            _uiState.value = AuthUiState(errorMessage = "Enter your email address")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _uiState.value = AuthUiState(errorMessage = "Enter a valid email address")
            return false
        }

        if (password.isBlank()) {
            _uiState.value = AuthUiState(errorMessage = "Enter your password")
            return false
        }

        if (password.length < MinimumPasswordLength) {
            _uiState.value = AuthUiState(
                errorMessage = "Password must be at least $MinimumPasswordLength characters"
            )
            return false
        }

        return true
    }
}
