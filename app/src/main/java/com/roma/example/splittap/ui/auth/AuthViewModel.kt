package com.roma.example.splittap.ui.auth

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MinimumPasswordLength = 8

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUserEmail: String? = null,
    val isRegisterMode: Boolean = false,
    val fullName: String = "",
    val contact: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    @StringRes val nameErrorRes: Int? = null,
    @StringRes val contactErrorRes: Int? = null,
    @StringRes val emailErrorRes: Int? = null,
    @StringRes val passwordErrorRes: Int? = null,
    @StringRes val confirmPasswordErrorRes: Int? = null,
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
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun showLogin() {
        _uiState.update {
            it.copy(
                isRegisterMode = false,
                errorMessageRes = null,
                successMessageRes = null,
                nameErrorRes = null,
                contactErrorRes = null,
                confirmPasswordErrorRes = null
            )
        }
    }

    fun showRegister() {
        _uiState.update {
            it.copy(
                isRegisterMode = true,
                errorMessageRes = null,
                successMessageRes = null
            )
        }
    }

    fun updateFullName(name: String) {
        _uiState.update {
            it.copy(
                fullName = name,
                nameErrorRes = validateNameMessage(name),
                errorMessageRes = null,
                successMessageRes = null
            )
        }
    }

    fun updateContact(contact: String) {
        _uiState.update {
            it.copy(
                contact = contact,
                contactErrorRes = validateContactMessage(contact),
                errorMessageRes = null,
                successMessageRes = null
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailErrorRes = validateEmailMessage(email),
                errorMessageRes = null,
                successMessageRes = null
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordErrorRes = validatePasswordMessage(password),
                confirmPasswordErrorRes = validateConfirmPasswordMessage(
                    password = password,
                    confirmPassword = it.confirmPassword
                ),
                errorMessageRes = null,
                successMessageRes = null
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordErrorRes = validateConfirmPasswordMessage(
                    password = it.password,
                    confirmPassword = confirmPassword
                ),
                errorMessageRes = null,
                successMessageRes = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !it.showPassword) }
    }

    fun submitLogin() {
        val state = _uiState.value
        if (!validateLoginForm(state.email, state.password)) return

        login(state.email, state.password)
    }

    fun submitRegister() {
        val state = _uiState.value
        if (!validateRegisterForm(
                name = state.fullName,
                contact = state.contact,
                email = state.email,
                password = state.password,
                confirmPassword = state.confirmPassword
            )
        ) {
            return
        }

        register(
            name = state.fullName,
            contact = state.contact,
            email = state.email,
            password = state.password
        )
    }

    fun resetPasswordForCurrentEmail() {
        val email = _uiState.value.email.trim()
        if (!validateResetEmail(email)) return

        resetPassword(email)
    }

    fun login(email: String, password: String) {
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessageRes = null,
                    successMessageRes = null
                )
            }

            try {
                repository.login(email.trim(), password)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentUserEmail = repository.getCurrentUser()?.email,
                        password = "",
                        confirmPassword = "",
                        errorMessageRes = null,
                        successMessageRes = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = e.toFriendlyAuthMessageRes()
                    )
                }
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
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessageRes = null,
                    successMessageRes = null
                )
            }

            try {
                repository.register(
                    name = trimmedName,
                    contact = trimmedContact,
                    email = trimmedEmail,
                    password = password
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentUserEmail = repository.getCurrentUser()?.email,
                        password = "",
                        confirmPassword = "",
                        errorMessageRes = null,
                        successMessageRes = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = e.toFriendlyAuthMessageRes()
                    )
                }
            }
        }
    }

    fun resetPassword(email: String) {
        val trimmedEmail = email.trim()

        if (!validateEmail(trimmedEmail)) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessageRes = null,
                    successMessageRes = null
                )
            }

            try {
                repository.sendPasswordResetEmail(trimmedEmail)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessageRes = R.string.auth_reset_sent
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = e.toFriendlyAuthMessageRes()
                    )
                }
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState(isLoggedIn = false)
    }

    private fun validate(email: String, password: String): Boolean {
        val trimmedEmail = email.trim()

        return validateLoginForm(trimmedEmail, password)
    }

    private fun validateRegister(
        name: String,
        contact: String,
        email: String,
        password: String
    ): Boolean {
        return validateRegisterForm(
            name = name,
            contact = contact,
            email = email,
            password = password,
            confirmPassword = password
        )
    }

    private fun validateEmail(email: String): Boolean {
        return validateResetEmail(email)
    }

    private fun validateLoginForm(email: String, password: String): Boolean {
        val emailError = validateEmailMessage(email)
        val passwordError = validatePasswordMessage(password)

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailErrorRes = emailError,
                    passwordErrorRes = passwordError,
                    errorMessageRes = emailError ?: passwordError,
                    successMessageRes = null
                )
            }
            return false
        }

        return true
    }

    private fun validateRegisterForm(
        name: String,
        contact: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val nameError = validateNameMessage(name)
        val contactError = validateContactMessage(contact)
        val emailError = validateEmailMessage(email)
        val passwordError = validatePasswordMessage(password)
        val confirmPasswordError = validateConfirmPasswordMessage(password, confirmPassword)

        if (
            nameError != null ||
            contactError != null ||
            emailError != null ||
            passwordError != null ||
            confirmPasswordError != null
        ) {
            _uiState.update {
                it.copy(
                    nameErrorRes = nameError,
                    contactErrorRes = contactError,
                    emailErrorRes = emailError,
                    passwordErrorRes = passwordError,
                    confirmPasswordErrorRes = confirmPasswordError,
                    errorMessageRes = nameError
                        ?: contactError
                        ?: emailError
                        ?: passwordError
                        ?: confirmPasswordError,
                    successMessageRes = null
                )
            }
            return false
        }

        return true
    }

    private fun validateResetEmail(email: String): Boolean {
        val emailError = validateEmailMessage(email)

        if (emailError != null) {
            _uiState.update {
                it.copy(
                    emailErrorRes = emailError,
                    errorMessageRes = emailError,
                    successMessageRes = null
                )
            }
            return false
        }

        return true
    }

    @StringRes
    private fun validateNameMessage(name: String): Int? {
        return if (name.isBlank()) R.string.auth_name_required else null
    }

    @StringRes
    private fun validateContactMessage(contact: String): Int? {
        return if (contact.isBlank()) R.string.auth_contact_required else null
    }

    @StringRes
    private fun validateEmailMessage(email: String): Int? {
        val trimmedEmail = email.trim()
        return when {
            trimmedEmail.isBlank() -> R.string.auth_email_required
            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> {
                R.string.auth_enter_valid_email
            }
            else -> null
        }
    }

    @StringRes
    private fun validatePasswordMessage(password: String): Int? {
        return when {
            password.isBlank() -> R.string.auth_password_required
            password.length < MinimumPasswordLength -> R.string.auth_password_min_error
            else -> null
        }
    }

    @StringRes
    private fun validateConfirmPasswordMessage(
        password: String,
        confirmPassword: String
    ): Int? {
        return when {
            confirmPassword.isBlank() -> R.string.auth_confirm_password_required
            confirmPassword != password -> R.string.auth_passwords_do_not_match
            else -> null
        }
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
