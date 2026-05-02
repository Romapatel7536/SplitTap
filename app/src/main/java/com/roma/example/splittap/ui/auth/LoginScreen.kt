package com.roma.example.splittap.ui.auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.viewmodel.AuthUiState

private const val MinimumPasswordLength = 8

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String, String, String) -> Unit,
    onForgotPasswordClick: (String) -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var contact by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isSignUp by rememberSaveable { mutableStateOf(false) }
    var nameTouched by rememberSaveable { mutableStateOf(false) }
    var contactTouched by rememberSaveable { mutableStateOf(false) }
    var emailTouched by rememberSaveable { mutableStateOf(false) }
    var passwordTouched by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val green = colorResource(R.color.auth_primary)
    val ink = colorResource(R.color.auth_ink)
    val muted = colorResource(R.color.auth_muted)
    val border = colorResource(R.color.auth_border)
    val surface = colorResource(R.color.white)
    val emailRequired = stringResource(R.string.auth_email_required)
    val validEmailRequired = stringResource(R.string.auth_enter_valid_email)
    val passwordRequired = stringResource(R.string.auth_password_required)
    val minimumPassword = stringResource(R.string.auth_minimum_password)
    val nameRequired = stringResource(R.string.auth_name_required)
    val contactRequired = stringResource(R.string.auth_contact_required)

    val nameError = when {
        !isSignUp || !nameTouched -> null
        fullName.isBlank() -> nameRequired
        else -> null
    }
    val contactError = when {
        !isSignUp || !contactTouched -> null
        contact.isBlank() -> contactRequired
        else -> null
    }
    val emailError = when {
        !emailTouched -> null
        email.isBlank() -> emailRequired
        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> validEmailRequired
        else -> null
    }
    val passwordError = when {
        !passwordTouched -> null
        password.isBlank() -> passwordRequired
        password.length < MinimumPasswordLength -> minimumPassword
        else -> null
    }

    fun submit() {
        nameTouched = isSignUp
        contactTouched = isSignUp
        emailTouched = true
        passwordTouched = true

        val hasValidName = !isSignUp || fullName.isNotBlank()
        val hasValidContact = !isSignUp || contact.isNotBlank()
        val hasValidEmail = email.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        val hasValidPassword = password.length >= MinimumPasswordLength

        if (hasValidName && hasValidContact && hasValidEmail && hasValidPassword) {
            focusManager.clearFocus()
            if (isSignUp) {
                onRegisterClick(
                    fullName.trim(),
                    contact.trim(),
                    email.trim(),
                    password
                )
            } else {
                onLoginClick(email.trim(), password)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSignUp) {
                        TextButton(
                            onClick = { isSignUp = false },
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.auth_back),
                                color = ink,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        SplitTapMark(green = green, ink = ink)
                    }
                }

                Spacer(modifier = Modifier.height(52.dp))

                Text(
                    text = if (isSignUp) {
                        stringResource(R.string.auth_sign_up_title)
                    } else {
                        stringResource(R.string.auth_log_in_title)
                    },
                    style = MaterialTheme.typography.displaySmall,
                    color = ink,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(46.dp))

                if (isSignUp) {
                    AuthField(
                        label = stringResource(R.string.auth_full_name),
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            if (!nameTouched) nameTouched = true
                        },
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                        error = nameError,
                        border = border,
                        focusedBorder = ink,
                        textColor = ink
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    AuthField(
                        label = stringResource(R.string.auth_contact_number),
                        value = contact,
                        onValueChange = {
                            contact = it
                            if (!contactTouched) contactTouched = true
                        },
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                        error = contactError,
                        border = border,
                        focusedBorder = ink,
                        textColor = ink
                    )

                    Spacer(modifier = Modifier.height(22.dp))
                }

                AuthField(
                    label = stringResource(R.string.auth_email_address),
                    value = email,
                    onValueChange = {
                        email = it
                        if (!emailTouched) emailTouched = true
                    },
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    error = emailError,
                    border = border,
                    focusedBorder = ink,
                    textColor = ink
                )

                Spacer(modifier = Modifier.height(22.dp))

                AuthField(
                    label = stringResource(R.string.auth_password),
                    value = password,
                    onValueChange = {
                        password = it
                        if (!passwordTouched) passwordTouched = true
                    },
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    onDone = { submit() },
                    error = passwordError,
                    helperText = if (isSignUp && passwordError == null) {
                        minimumPassword
                    } else {
                        null
                    },
                    trailingText = if (showPassword) {
                        stringResource(R.string.auth_hide_password)
                    } else {
                        stringResource(R.string.auth_show_password)
                    },
                    onTrailingClick = { showPassword = !showPassword },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    border = border,
                    focusedBorder = ink,
                    textColor = ink
                )

                uiState.errorMessageRes?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(it),
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                uiState.successMessageRes?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(it),
                        modifier = Modifier.fillMaxWidth(),
                        color = green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                Button(
                    onClick = { submit() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = green,
                        disabledContainerColor = green.copy(alpha = 0.55f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = surface
                        )
                    } else {
                        Text(
                            text = if (isSignUp) {
                                stringResource(R.string.auth_create_account)
                            } else {
                                stringResource(R.string.auth_log_in_title)
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (!isSignUp) {
                    Spacer(modifier = Modifier.height(28.dp))
                    TextButton(
                        onClick = {
                            emailTouched = true
                            onForgotPasswordClick(email.trim())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    ) {
                        Text(
                            text = stringResource(R.string.auth_forgot_password),
                            color = green,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        isSignUp = !isSignUp
                        nameTouched = false
                        contactTouched = false
                        emailTouched = false
                        passwordTouched = false
                    },
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = if (isSignUp) {
                            stringResource(R.string.auth_already_have_account)
                        } else {
                            stringResource(R.string.auth_new_to_splittap)
                        },
                        color = green,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SplitTapMark(
    green: Color,
    ink: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(green.copy(alpha = 0.14f), RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.auth_logo_letter),
                color = green,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            color = ink,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    border: Color,
    focusedBorder: Color,
    textColor: Color,
    error: String? = null,
    helperText: String? = null,
    trailingText: String? = null,
    onTrailingClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(13.dp),
            textStyle = MaterialTheme.typography.titleMedium,
            singleLine = true,
            isError = error != null,
            visualTransformation = visualTransformation,
            trailingIcon = {
                if (trailingText != null && onTrailingClick != null) {
                    TextButton(onClick = onTrailingClick) {
                        Text(
                            text = trailingText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
                onDone = { onDone?.invoke() }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = focusedBorder,
                unfocusedBorderColor = border,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = focusedBorder
            )
        )

        val supportingText = error ?: helperText
        if (supportingText != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = supportingText,
                color = if (error != null) {
                    MaterialTheme.colorScheme.error
                } else {
                    colorResource(R.color.auth_muted)
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
