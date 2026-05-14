package com.roma.example.splittap.ui.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
        Spacer(modifier = Modifier.height(4.dp))
        AuthBackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(12.dp))
        AuthLogoMark(modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(22.dp))
        AuthHeader(
            title = stringResource(R.string.auth_register_headline),
            subtitle = stringResource(R.string.auth_register_subtitle)
        )

        Spacer(modifier = Modifier.height(30.dp))
        AuthField(
            labelRes = R.string.auth_full_name,
            placeholderRes = R.string.auth_name_hint,
            value = uiState.fullName,
            onValueChange = onFullNameChange,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            leadingIconRes = R.drawable.ic_person_24,
            errorRes = uiState.nameErrorRes,
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(18.dp))
        AuthField(
            labelRes = R.string.auth_email_address,
            placeholderRes = R.string.auth_email_hint,
            value = uiState.email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            leadingIconRes = R.drawable.ic_mail_24,
            errorRes = uiState.emailErrorRes,
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(18.dp))
        AuthField(
            labelRes = R.string.auth_contact_number,
            placeholderRes = R.string.auth_contact_hint,
            value = uiState.contact,
            onValueChange = onContactChange,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            leadingIconRes = R.drawable.ic_phone_24,
            errorRes = uiState.contactErrorRes,
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(18.dp))
        AuthField(
            labelRes = R.string.auth_password,
            placeholderRes = R.string.auth_password_create_hint,
            value = uiState.password,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            leadingIconRes = R.drawable.ic_lock_24,
            errorRes = uiState.passwordErrorRes,
            visualTransformation = PasswordVisualTransformation(),
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(18.dp))
        AuthField(
            labelRes = R.string.auth_confirm_password,
            placeholderRes = R.string.auth_confirm_password_hint,
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            leadingIconRes = R.drawable.ic_lock_24,
            errorRes = uiState.confirmPasswordErrorRes,
            visualTransformation = PasswordVisualTransformation(),
            onDone = {
                focusManager.clearFocus()
                onRegisterClick()
            }
        )

        AuthMessageBlock(
            uiState = uiState,
            modifier = Modifier.padding(top = 18.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))
        AuthPrimaryButton(
            textRes = R.string.auth_create_account,
            isLoading = uiState.isLoading,
            onClick = {
                focusManager.clearFocus()
                onRegisterClick()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        AuthFooterAction(
            promptRes = R.string.auth_already_have_account_prompt,
            actionRes = R.string.auth_sign_in_link,
            onActionClick = onBackClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
