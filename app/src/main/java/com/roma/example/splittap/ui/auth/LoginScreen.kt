package com.roma.example.splittap.ui.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
fun LoginScreen(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    AuthPage {
        Spacer(modifier = Modifier.height(34.dp))
        AuthLogoMark(modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(24.dp))
        AuthHeader(
            title = stringResource(R.string.auth_login_headline),
            subtitle = stringResource(R.string.auth_login_subtitle)
        )

        Spacer(modifier = Modifier.height(36.dp))
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

        Spacer(modifier = Modifier.height(20.dp))
        AuthField(
            labelRes = R.string.auth_password,
            placeholderRes = R.string.auth_password_hint,
            value = uiState.password,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            leadingIconRes = R.drawable.ic_lock_24,
            errorRes = uiState.passwordErrorRes,
            visualTransformation = PasswordVisualTransformation(),
            onDone = {
                focusManager.clearFocus()
                onLoginClick()
            }
        )

        Spacer(modifier = Modifier.height(18.dp))
        AuthRememberMeRow(
            checked = rememberMe,
            onCheckedChange = { rememberMe = it },
            onForgotPasswordClick = onForgotPasswordClick
        )

        AuthMessageBlock(
            uiState = uiState,
            modifier = Modifier.padding(top = 18.dp)
        )

        Spacer(modifier = Modifier.height(34.dp))
        AuthPrimaryButton(
            textRes = R.string.auth_sign_in_button,
            isLoading = uiState.isLoading,
            onClick = {
                focusManager.clearFocus()
                onLoginClick()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        AuthFooterAction(
            promptRes = R.string.auth_dont_have_account,
            actionRes = R.string.auth_sign_up_link,
            onActionClick = onCreateAccountClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
