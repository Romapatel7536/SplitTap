package com.roma.example.splittap.ui.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R

@Composable
fun RegisterScreen(
    uiState: AuthUiState,
    onFullNameChange: (String) -> Unit,
    onContactChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    AuthPage {
        Spacer(modifier = Modifier.height(48.dp))
        AuthBackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(54.dp))
        AuthHeader(
            title = stringResource(R.string.auth_register_headline),
            subtitle = stringResource(R.string.auth_register_subtitle)
        )

        Spacer(modifier = Modifier.height(38.dp))
        AuthField(
            labelRes = R.string.auth_full_name,
            placeholderRes = R.string.auth_name_hint,
            value = uiState.fullName,
            onValueChange = onFullNameChange,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            errorRes = uiState.nameErrorRes,
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        AuthField(
            labelRes = R.string.auth_contact_number,
            placeholderRes = R.string.auth_contact_hint,
            value = uiState.contact,
            onValueChange = onContactChange,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            errorRes = uiState.contactErrorRes,
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        AuthField(
            labelRes = R.string.auth_email_address,
            placeholderRes = R.string.auth_email_hint,
            value = uiState.email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            errorRes = uiState.emailErrorRes,
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        AuthField(
            labelRes = R.string.auth_password,
            placeholderRes = R.string.auth_password_create_hint,
            value = uiState.password,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            errorRes = uiState.passwordErrorRes,
            trailingTextRes = passwordTrailingTextRes(uiState.showPassword),
            onTrailingClick = onTogglePasswordVisibility,
            visualTransformation = if (uiState.showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        AuthField(
            labelRes = R.string.auth_confirm_password,
            placeholderRes = R.string.auth_confirm_password_hint,
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            errorRes = uiState.confirmPasswordErrorRes,
            visualTransformation = if (uiState.showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onDone = {
                focusManager.clearFocus()
                onRegisterClick()
            }
        )

        AuthMessageBlock(
            uiState = uiState,
            modifier = Modifier.padding(top = 14.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        AuthPrimaryButton(
            textRes = R.string.auth_sign_up_button,
            isLoading = uiState.isLoading,
            onClick = {
                focusManager.clearFocus()
                onRegisterClick()
            },
            modifier = Modifier.padding(bottom = 26.dp)
        )
    }
}
