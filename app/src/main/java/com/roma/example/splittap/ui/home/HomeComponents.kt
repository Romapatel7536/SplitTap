package com.roma.example.splittap.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import java.text.NumberFormat
import java.util.Locale

object AppUiTokens {
    val ScreenMaxWidth: Dp = 480.dp
    val TopBarHeight: Dp = 56.dp
    val FieldMinHeight: Dp = 52.dp
    val ButtonMinHeight: Dp = 52.dp
    val ChipMinHeight: Dp = 42.dp
    val CardCorner: Dp = 18.dp
    val ControlCorner: Dp = 16.dp
    val FieldHorizontalPadding: Dp = 16.dp
    val FieldIconSize: Dp = 20.dp
    val AvatarMedium: Dp = 46.dp
}

data class AppDropdownOption<T>(
    val value: T,
    val label: String
)

@Composable
fun EmptyStatePanel(
    title: String,
    body: String,
    action: String,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmptyIllustration()
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                color = AppInk,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = body,
                color = AppMuted,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            AppGradientButton(
                text = action,
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EmptyIllustration() {
    Box(
        modifier = Modifier
            .size(112.dp)
            .clip(CircleShape)
            .background(AppAccent.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(AppSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_credit_card_24),
                contentDescription = stringResource(R.string.home_cd_empty_illustration),
                tint = AppPrimary,
                modifier = Modifier.size(38.dp)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-20).dp, y = (-22).dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(AppSuccess),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check_24),
                contentDescription = null,
                tint = AppSurface,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun AppMark(size: androidx.compose.ui.unit.Dp = 46.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(AppPrimary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_credit_card_24),
            contentDescription = stringResource(R.string.home_cd_app_mark),
            tint = AppSurface,
            modifier = Modifier.size(size * 0.54f)
        )
    }
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = AppInk,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun AppScreenTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppSurface
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(AppUiTokens.TopBarHeight)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_left_24),
                        contentDescription = stringResource(R.string.home_cd_back),
                        tint = AppInk,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = title,
                    color = AppInk,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    trailing?.invoke()
                }
            }

            HorizontalDivider(color = AppBorder)
        }
    }
}

@Composable
fun AppGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconRes: Int? = null
) {
    Surface(
        modifier = modifier
            .heightIn(min = AppUiTokens.ButtonMinHeight)
            .clip(RoundedCornerShape(AppUiTokens.ControlCorner))
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(AppUiTokens.ControlCorner),
        color = AppTransparent,
        shadowElevation = if (enabled) 8.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (enabled) {
                        Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd))
                    } else {
                        Brush.linearGradient(listOf(AppBorder, AppBorder))
                    }
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = AppSurface,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                color = AppSurface,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun AppGradientIconTile(
    iconRes: Int,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 58.dp,
    iconSize: androidx.compose.ui.unit.Dp = 28.dp,
    rounded: androidx.compose.ui.unit.Dp = 16.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(rounded))
            .background(Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = AppSurface,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun AppInitialAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 52.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercase()
                ?: stringResource(R.string.home_roommate_initial),
            color = AppSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AppFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minHeight: Dp = AppUiTokens.FieldMinHeight
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = AppInk,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 7.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(
                color = AppInk,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Medium
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight)
                        .background(AppSurface, RoundedCornerShape(AppUiTokens.ControlCorner))
                        .border(1.dp, AppBorder, RoundedCornerShape(AppUiTokens.ControlCorner))
                        .padding(
                            horizontal = AppUiTokens.FieldHorizontalPadding,
                            vertical = if (singleLine) 0.dp else 12.dp
                        ),
                    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = AppMuted,
                        modifier = Modifier
                            .padding(top = if (singleLine) 0.dp else 2.dp)
                            .size(AppUiTokens.FieldIconSize)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = AppMuted.copy(alpha = 0.74f),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = if (singleLine) 1 else 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun AppSelectionField(
    label: String,
    title: String,
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    onClick: (() -> Unit)? = null,
    trailingIconRes: Int? = if (onClick != null) R.drawable.ic_chevron_right_24 else null
) {
    Column(modifier = modifier) {
        AppFieldLabel(text = label)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = AppUiTokens.FieldMinHeight)
                .then(
                    if (onClick != null) {
                        Modifier
                            .clip(RoundedCornerShape(AppUiTokens.ControlCorner))
                            .clickable(onClick = onClick)
                    } else {
                        Modifier
                    }
                ),
            shape = RoundedCornerShape(AppUiTokens.ControlCorner),
            color = AppSurface,
            border = BorderStroke(1.dp, AppBorder)
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = AppUiTokens.FieldHorizontalPadding,
                    vertical = 12.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconRes != null) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = AppMuted,
                        modifier = Modifier.size(AppUiTokens.FieldIconSize)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Text(
                    text = title,
                    color = AppInk,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (trailingIconRes != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(trailingIconRes),
                        contentDescription = null,
                        tint = AppPrimary,
                        modifier = Modifier.size(AppUiTokens.FieldIconSize)
                    )
                }
            }
        }
    }
}

@Composable
fun <T> AppDropdownList(
    options: List<AppDropdownOption<T>>,
    selectedValue: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    AppOutlinedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(vertical = 2.dp)) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = AppUiTokens.ChipMinHeight)
                        .clickable { onSelected(option.value) }
                        .padding(horizontal = 16.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option.label,
                        color = AppInk,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (option.value == selectedValue) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check_24),
                            contentDescription = null,
                            tint = AppPrimary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppCheckIndicator(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                color = if (selected) AppPrimary else AppSurface,
                shape = RoundedCornerShape(7.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) AppPrimary else AppBorder,
                shape = RoundedCornerShape(7.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                painter = painterResource(R.drawable.ic_check_24),
                contentDescription = null,
                tint = AppSurface,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
fun AppSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            color = AppInk,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Medium
        ),
        cursorBrush = SolidColor(AppPrimary),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppUiTokens.FieldMinHeight)
            .clip(RoundedCornerShape(AppUiTokens.ControlCorner))
            .background(AppSurface),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppUiTokens.FieldHorizontalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search_24),
                    contentDescription = null,
                    tint = AppMuted,
                    modifier = Modifier.size(AppUiTokens.FieldIconSize)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = AppMuted.copy(alpha = 0.78f),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun AppFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = AppInk,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 7.dp)
    )
}

@Composable
fun AppSwitch(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(width = 52.dp, height = 30.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = if (checked) AppPrimary else AppBorder
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(AppSurface)
            )
        }
    }
}

@Composable
fun AppOutlinedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp,
        content = content
    )
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.CANADA).format(amount)
}
