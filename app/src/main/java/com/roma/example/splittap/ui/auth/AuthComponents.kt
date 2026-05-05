package com.roma.example.splittap.ui.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R

@Composable
internal fun AuthPage(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(R.color.white)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            content = content
        )
    }
}

@Composable
internal fun AuthHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = colorResource(R.color.auth_ink),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            color = colorResource(R.color.auth_muted),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
internal fun AuthBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(R.string.auth_back),
            color = colorResource(R.color.auth_ink),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
internal fun SplitTapMark(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    colorResource(R.color.auth_primary).copy(alpha = 0.14f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.auth_logo_letter),
                color = colorResource(R.color.auth_primary),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            color = colorResource(R.color.auth_ink),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun AuthField(
    @StringRes labelRes: Int,
    @StringRes placeholderRes: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    @StringRes errorRes: Int? = null,
    @StringRes trailingTextRes: Int? = null,
    onTrailingClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(labelRes),
            color = colorResource(R.color.auth_ink),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.titleMedium,
            singleLine = true,
            isError = errorRes != null,
            placeholder = {
                Text(
                    text = stringResource(placeholderRes),
                    color = colorResource(R.color.auth_muted)
                )
            },
            visualTransformation = visualTransformation,
            trailingIcon = {
                if (trailingTextRes != null && onTrailingClick != null) {
                    TextButton(onClick = onTrailingClick) {
                        Text(
                            text = stringResource(trailingTextRes),
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
                focusedBorderColor = colorResource(R.color.auth_primary),
                unfocusedBorderColor = colorResource(R.color.auth_border),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedTextColor = colorResource(R.color.auth_ink),
                unfocusedTextColor = colorResource(R.color.auth_ink),
                cursorColor = colorResource(R.color.auth_primary)
            )
        )

        if (errorRes != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
internal fun AuthPrimaryButton(
    @StringRes textRes: Int,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = colorResource(R.color.auth_primary)
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(16.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = primary,
            disabledContainerColor = primary.copy(alpha = 0.55f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = colorResource(R.color.white)
            )
        } else {
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
internal fun AuthMessageBlock(
    uiState: AuthUiState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        uiState.errorMessageRes?.let { messageRes ->
            Text(
                text = stringResource(messageRes),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        uiState.successMessageRes?.let { messageRes ->
            Text(
                text = stringResource(messageRes),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (uiState.errorMessageRes == null) 0.dp else 8.dp),
                color = colorResource(R.color.auth_primary),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

internal fun passwordTrailingTextRes(showPassword: Boolean): Int {
    return if (showPassword) {
        R.string.auth_hide_password
    } else {
        R.string.auth_show_password
    }
}
