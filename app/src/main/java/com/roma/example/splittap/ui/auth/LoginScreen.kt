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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val MinimumPasswordLength = 8

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val green = Color(0xFF1F8F74)
    val ink = Color(0xFF343A40)
    val muted = Color(0xFF747D86)
    val border = Color(0xFFC9D2DA)

    val emailError = when {
        !emailTouched -> null
        email.isBlank() -> "Email is required"
        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Enter a valid email address"
        else -> null
    }
    val passwordError = when {
        !passwordTouched -> null
        password.isBlank() -> "Password is required"
        password.length < MinimumPasswordLength -> "Minimum $MinimumPasswordLength characters"
        else -> null
    }

    fun submit() {
        emailTouched = true
        passwordTouched = true

        val hasValidEmail = email.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        val hasValidPassword = password.length >= MinimumPasswordLength

        if (hasValidEmail && hasValidPassword) {
            focusManager.clearFocus()
            if (isSignUp) {
                onRegisterClick(email.trim(), password)
            } else {
                onLoginClick(email.trim(), password)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
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
                                text = "<",
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
                    text = if (isSignUp) "Sign up" else "Log in",
                    style = MaterialTheme.typography.displaySmall,
                    color = ink,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(46.dp))

                AuthField(
                    label = "Email address",
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
                    label = "Password",
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
                        "Minimum $MinimumPasswordLength characters"
                    } else {
                        null
                    },
                    trailingText = if (showPassword) "Hide" else "Show",
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

                uiState.errorMessage?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
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
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = if (isSignUp) "Next" else "Log in",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (!isSignUp) {
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = "Forgot your password?",
                        modifier = Modifier.fillMaxWidth(),
                        color = green,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
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
                        emailTouched = false
                        passwordTouched = false
                    },
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = if (isSignUp) {
                            "Already have an account? Log in"
                        } else {
                            "New to SplitTap? Sign up"
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
                text = "S",
                color = green,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "SplitTap",
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
                color = if (error != null) MaterialTheme.colorScheme.error else Color(0xFF747D86),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
