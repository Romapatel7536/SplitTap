package com.roma.example.splittap.ui.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppGradientEnd
import com.roma.example.splittap.ui.home.AppGradientStart
import com.roma.example.splittap.ui.home.AppMutedOnGradient
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppWarning
import com.roma.example.splittap.ui.home.AppSuccess
import com.roma.example.splittap.ui.home.formatCurrency

@Composable
fun DashboardHeader(
    userName: String,
    totalBalance: Double,
    youOwe: Double,
    youAreOwed: Double,
    horizontalPadding: Dp,
    compact: Boolean,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val displayName = userName.ifBlank {
        stringResource(R.string.home_signed_in_fallback)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (compact) 250.dp else 274.dp)
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(
                Brush.linearGradient(
                    listOf(AppGradientStart, AppGradientEnd)
                )
            )
            .statusBarsPadding()
            .padding(horizontal = horizontalPadding)
            .padding(
                top = if (compact) 8.dp else 10.dp,
                bottom = if (compact) 12.dp else 14.dp
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .widthIn(max = 480.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.home_welcome_back),
                        color = AppSurface.copy(alpha = 0.86f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = displayName,
                        color = AppSurface,
                        style = if (compact) {
                            MaterialTheme.typography.titleLarge
                        } else {
                            MaterialTheme.typography.headlineSmall
                        },
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                HeaderIconButton(
                    iconRes = R.drawable.ic_notifications_24,
                    contentDescription = stringResource(R.string.home_cd_notifications),
                    onClick = onNotificationsClick
                )

                Spacer(modifier = Modifier.width(12.dp))

                HeaderIconButton(
                    iconRes = R.drawable.ic_person_24,
                    contentDescription = stringResource(R.string.home_cd_open_profile_menu),
                    onClick = onProfileClick
                )
            }

            Spacer(modifier = Modifier.height(if (compact) 12.dp else 14.dp))

            BalanceSummaryCard(
                totalBalance = totalBalance,
                youOwe = youOwe,
                youAreOwed = youAreOwed,
                compact = compact
            )
        }
    }
}

@Composable
private fun HeaderIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(
                onClickLabel = contentDescription,
                onClick = onClick
            ),
        shape = CircleShape,
        color = AppSurface.copy(alpha = 0.2f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                tint = AppSurface,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun BalanceSummaryCard(
    totalBalance: Double,
    youOwe: Double,
    youAreOwed: Double,
    compact: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (compact) 150.dp else 164.dp),
        shape = RoundedCornerShape(22.dp),
        color = AppSurface.copy(alpha = 0.14f),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = if (compact) 18.dp else 22.dp,
                vertical = if (compact) 14.dp else 16.dp
            )
        ) {
            Text(
                text = stringResource(R.string.home_total_balance),
                color = AppMutedOnGradient,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(if (compact) 6.dp else 8.dp))

            Text(
                text = formatCurrency(totalBalance),
                color = AppSurface,
                style = if (compact) {
                    MaterialTheme.typography.headlineMedium
                } else {
                    MaterialTheme.typography.headlineLarge
                },
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(if (compact) 12.dp else 14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BalanceMiniColumn(
                    iconRes = R.drawable.ic_trending_up_24,
                    label = stringResource(R.string.home_you_owe),
                    amount = formatCurrency(youOwe),
                    iconTint = AppWarning,
                    modifier = Modifier.weight(1f)
                )

                BalanceMiniColumn(
                    iconRes = R.drawable.ic_trending_down_24,
                    label = stringResource(R.string.home_you_are_owed),
                    amount = formatCurrency(youAreOwed),
                    iconTint = AppSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BalanceMiniColumn(
    iconRes: Int,
    label: String,
    amount: String,
    iconTint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = label,
                color = AppSurface.copy(alpha = 0.88f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = amount,
            color = AppSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
