package com.roma.example.splittap.viewmodel

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.roma.example.splittap.R
import com.roma.example.splittap.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val MinimumPasswordLength = 8

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUserEmail: String? = null,
    @StringRes val errorMessageRes: Int? = null,
    @StringRes val successMessageRes: Int? = null
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
                _uiState.value = AuthUiState(errorMessageRes = e.toFriendlyAuthMessageRes())
            }
        }
    }

    fun register(
        name: String,
        contact: String,
        email: String,
        password: String
    ) {
        val trimmedName = name.trim()
        val trimmedContact = contact.trim()
        val trimmedEmail = email.trim()

        if (!validateRegister(trimmedName, trimmedContact, trimmedEmail, password)) return

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                repository.register(
                    name = trimmedName,
                    contact = trimmedContact,
                    email = trimmedEmail,
                    password = password
                )
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    currentUserEmail = repository.getCurrentUser()?.email
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessageRes = e.toFriendlyAuthMessageRes())
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
                    successMessageRes = R.string.auth_reset_sent
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessageRes = e.toFriendlyAuthMessageRes())
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
            _uiState.value = AuthUiState(errorMessageRes = R.string.auth_enter_password)
            return false
        }

        if (password.length < MinimumPasswordLength) {
            _uiState.value = AuthUiState(
                errorMessageRes = R.string.auth_password_min_error
            )
            return false
        }

        return true
    }

    private fun validateRegister(
        name: String,
        contact: String,
        email: String,
        password: String
    ): Boolean {
        if (name.isBlank()) {
            _uiState.value = AuthUiState(errorMessageRes = R.string.auth_name_required)
            return false
        }

        if (contact.isBlank()) {
            _uiState.value = AuthUiState(errorMessageRes = R.string.auth_contact_required)
            return false
        }

        return validate(email, password)
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) {
            _uiState.value = AuthUiState(errorMessageRes = R.string.auth_enter_email)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = AuthUiState(errorMessageRes = R.string.auth_enter_valid_email)
            return false
        }

        return true
    }

    @StringRes
    private fun Exception.toFriendlyAuthMessageRes(): Int {
        return when (this) {
            is FirebaseAuthInvalidUserException -> R.string.auth_no_account
            is FirebaseAuthInvalidCredentialsException -> R.string.auth_invalid_credentials
            is FirebaseAuthUserCollisionException -> R.string.auth_email_exists
            is FirebaseAuthWeakPasswordException -> R.string.auth_weak_password
            is FirebaseNetworkException -> R.string.auth_network_error
            else -> R.string.auth_generic_error
        }
    }
}
