package com.roma.example.splittap.ui.auth

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R

@Composable
internal fun AuthPage(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(R.color.auth_background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                content = content
            )
        }
    }
}

@Composable
internal fun AuthLogoMark(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(64.dp),
        shape = RoundedCornerShape(18.dp),
        color = colorResource(R.color.transparent),
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier.background(AuthGradient()),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_dollar_24),
                contentDescription = stringResource(R.string.auth_cd_logo),
                modifier = Modifier.size(38.dp),
                tint = colorResource(R.color.white)
            )
        }
    }
}

@Composable
internal fun AuthHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = colorResource(R.color.auth_new_ink),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            color = colorResource(R.color.auth_new_muted),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun AuthBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left_24),
            contentDescription = stringResource(R.string.auth_back),
            tint = colorResource(R.color.auth_new_ink)
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
    @DrawableRes leadingIconRes: Int? = null,
    @StringRes errorRes: Int? = null,
    @StringRes trailingTextRes: Int? = null,
    onTrailingClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(labelRes),
            color = colorResource(R.color.auth_new_ink),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CompactAuthTextField(
            value = value,
            onValueChange = onValueChange,
            isError = errorRes != null,
            leadingIconRes = leadingIconRes,
            placeholderRes = placeholderRes,
            trailingTextRes = trailingTextRes,
            onTrailingClick = onTrailingClick,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
                onDone = { onDone?.invoke() }
            ),
            visualTransformation = visualTransformation
        )

        if (errorRes != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun CompactAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    @DrawableRes leadingIconRes: Int?,
    @StringRes placeholderRes: Int,
    @StringRes trailingTextRes: Int?,
    onTrailingClick: (() -> Unit)?,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation
) {
    val shape = RoundedCornerShape(14.dp)
    val borderColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        colorResource(R.color.auth_new_border)
    }
    val textStyle = MaterialTheme.typography.bodyMedium.merge(
        TextStyle(
            color = colorResource(R.color.auth_new_ink),
            fontWeight = FontWeight.Medium
        )
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(colorResource(R.color.auth_surface))
            .border(1.dp, borderColor, shape),
        singleLine = true,
        textStyle = textStyle,
        cursorBrush = SolidColor(colorResource(R.color.auth_new_primary)),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIconRes != null) {
                    Icon(
                        painter = painterResource(leadingIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.auth_new_muted)
                    )
                    Spacer(modifier = Modifier.size(14.dp))
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(placeholderRes),
                            color = colorResource(R.color.auth_new_muted).copy(alpha = 0.72f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    innerTextField()
                }

                if (trailingTextRes != null && onTrailingClick != null) {
                    TextButton(
                        onClick = onTrailingClick,
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = stringResource(trailingTextRes),
                            color = colorResource(R.color.auth_new_primary),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}

@Composable
internal fun AuthPrimaryButton(
    @StringRes textRes: Int,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .alpha(if (isLoading) 0.7f else 1f)
            .clip(shape)
            .background(AuthGradient())
            .clickable(
                enabled = !isLoading,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
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
                color = colorResource(R.color.white),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
internal fun AuthRememberMeRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(24.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(R.color.auth_new_primary),
                uncheckedColor = colorResource(R.color.auth_new_muted),
                checkmarkColor = colorResource(R.color.white)
            )
        )
        Text(
            text = stringResource(R.string.auth_remember_me),
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            color = colorResource(R.color.auth_new_muted),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        TextButton(
            onClick = onForgotPasswordClick,
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.auth_forgot_password_short),
                color = colorResource(R.color.auth_new_primary),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
internal fun AuthFooterAction(
    @StringRes promptRes: Int,
    @StringRes actionRes: Int,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = stringResource(promptRes),
            color = colorResource(R.color.auth_new_muted),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = stringResource(actionRes),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable(role = Role.Button, onClick = onActionClick),
            color = colorResource(R.color.auth_new_primary),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun AuthMessageBlock(
    uiState: AuthUiState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        uiState.errorMessageRes?.let { messageRes ->
            Text(
                text = stringResource(messageRes),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        uiState.successMessageRes?.let { messageRes ->
            Text(
                text = stringResource(messageRes),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (uiState.errorMessageRes == null) 0.dp else 8.dp),
                color = colorResource(R.color.auth_new_primary),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AuthGradient(): Brush {
    return Brush.linearGradient(
        listOf(
            colorResource(R.color.auth_gradient_start),
            colorResource(R.color.auth_gradient_end)
        )
    )
}

internal fun passwordTrailingTextRes(showPassword: Boolean): Int {
    return if (showPassword) {
        R.string.auth_hide_password
    } else {
        R.string.auth_show_password
    }
}
