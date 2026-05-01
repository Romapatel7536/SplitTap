package com.roma.example.splittap.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.roma.example.splittap.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val MinimumPasswordLength = 8

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUserEmail: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(
            isLoggedIn = repository.getCurrentUser() != null,
            currentUserEmail = repository.getCurrentUser()?.email
        )
    )
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                repository.login(email.trim(), password)
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    currentUserEmail = repository.getCurrentUser()?.email
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = e.toFriendlyAuthMessage())
            }
        }
    }

    fun register(email: String, password: String) {
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                repository.register(email.trim(), password)
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    currentUserEmail = repository.getCurrentUser()?.email
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = e.toFriendlyAuthMessage())
            }
        }
    }

    fun resetPassword(email: String) {
        val trimmedEmail = email.trim()

        if (!validateEmail(trimmedEmail)) return

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                repository.sendPasswordResetEmail(trimmedEmail)
                _uiState.value = AuthUiState(
                    successMessage = "Password reset email sent. Check your inbox."
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = e.toFriendlyAuthMessage())
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState(isLoggedIn = false)
    }

    private fun validate(email: String, password: String): Boolean {
        val trimmedEmail = email.trim()

        if (!validateEmail(trimmedEmail)) return false

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

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) {
            _uiState.value = AuthUiState(errorMessage = "Enter your email address")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = AuthUiState(errorMessage = "Enter a valid email address")
            return false
        }

        return true
    }

    private fun Exception.toFriendlyAuthMessage(): String {
        return when (this) {
            is FirebaseAuthInvalidUserException -> "No account found with this email address."
            is FirebaseAuthInvalidCredentialsException -> "Email or password is incorrect."
            is FirebaseAuthUserCollisionException -> "An account already exists with this email address."
            is FirebaseAuthWeakPasswordException -> "Password is too weak. Use at least $MinimumPasswordLength characters."
            is FirebaseNetworkException -> "Network error. Check your connection and try again."
            else -> "Something went wrong. Please try again."
        }
    }
}
