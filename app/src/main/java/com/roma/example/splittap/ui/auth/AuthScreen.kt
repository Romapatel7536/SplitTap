package com.roma.example.splittap.ui.auth

import androidx.compose.runtime.Composable

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onFullNameChange: (String) -> Unit,
    onContactChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onShowLogin: () -> Unit,
    onShowRegister: () -> Unit
) {
    if (uiState.isRegisterMode) {
        RegisterScreen(
            uiState = uiState,
            onFullNameChange = onFullNameChange,
            onContactChange = onContactChange,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            onRegisterClick = onRegisterClick,
            onBackClick = onShowLogin
        )
    } else {
        LoginScreen(
            uiState = uiState,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            onLoginClick = onLoginClick,
            onForgotPasswordClick = onForgotPasswordClick,
            onCreateAccountClick = onShowRegister
        )
    }
}
