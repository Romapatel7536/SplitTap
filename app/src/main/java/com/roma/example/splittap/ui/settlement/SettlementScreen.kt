package com.roma.example.splittap.ui.settlement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppAccent
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppGradientEnd
import com.roma.example.splittap.ui.home.AppGradientStart
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSuccess
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppWarning
import com.roma.example.splittap.ui.home.HomeUiState
import com.roma.example.splittap.ui.home.RoommateBalanceUi
import com.roma.example.splittap.ui.home.formatCurrency
import kotlin.math.abs

@Composable
fun SettlementScreen(
    uiState: HomeUiState,
    onBack: () -> Unit
) {
    val paymentsOwed = uiState.roommateBalances.filter { it.amount < 0.0 }
    val paymentsReceivable = uiState.roommateBalances.filter { it.amount > 0.0 }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            SettlementHeader(
                uiState = uiState,
                onBack = onBack,
                horizontalPadding = horizontalPadding
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = horizontalPadding, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettlementSectionTitle(text = stringResource(R.string.settle_payments_you_owe))

                if (paymentsOwed.isEmpty()) {
                    SettlementEmptyCard(text = stringResource(R.string.settle_no_payments_to_make))
                } else {
                    paymentsOwed.forEach { roommate ->
                        SettlementPersonCard(
                            roommate = roommate,
                            amountColor = AppWarning,
                            actionText = stringResource(
                                R.string.settle_with_person,
                                roommate.name.ifBlank { stringResource(R.string.home_unknown_user) }
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                SettlementSectionTitle(text = stringResource(R.string.settle_payments_you_receive))

                if (paymentsReceivable.isEmpty()) {
                    SettlementEmptyCard(text = stringResource(R.string.settle_no_payments_to_receive))
                } else {
                    paymentsReceivable.forEach { roommate ->
                        SettlementPersonCard(
                            roommate = roommate,
                            amountColor = AppSuccess,
                            actionText = stringResource(R.string.settle_waiting_for_payment)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettlementHeader(
    uiState: HomeUiState,
    onBack: () -> Unit,
    horizontalPadding: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd)))
            .statusBarsPadding()
            .padding(horizontal = horizontalPadding)
            .padding(top = 12.dp, bottom = 22.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .widthIn(max = 480.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        painter = painterResource(R.drawable.ic_chevron_right_24),
                        contentDescription = stringResource(R.string.home_cd_back),
                        tint = AppSurface,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(180f)
                    )
                }

                Text(
                    text = stringResource(R.string.settle_title),
                    color = AppSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.size(44.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = AppSurface.copy(alpha = 0.16f),
                border = BorderStroke(1.dp, AppSurface.copy(alpha = 0.18f))
            ) {
                Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
                    Text(
                        text = stringResource(R.string.settle_net_balance),
                        color = AppSurface.copy(alpha = 0.86f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatCurrency(uiState.totalBalance),
                        color = AppSurface,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SettlementMetric(
                            label = stringResource(R.string.settle_total_to_pay),
                            value = formatCurrency(uiState.youOwe),
                            modifier = Modifier.weight(1f)
                        )

                        SettlementMetric(
                            label = stringResource(R.string.settle_total_to_receive),
                            value = formatCurrency(uiState.youAreOwed),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettlementMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = AppSurface.copy(alpha = 0.86f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            color = AppSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SettlementSectionTitle(text: String) {
    Text(
        text = text,
        color = AppInk,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SettlementPersonCard(
    roommate: RoommateBalanceUi,
    amountColor: androidx.compose.ui.graphics.Color,
    actionText: String
) {
    val name = roommate.name.ifBlank {
        stringResource(R.string.home_unknown_user)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.firstOrNull()?.uppercase()
                            ?: stringResource(R.string.home_roommate_initial),
                        color = AppSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = name,
                    color = AppInk,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatCurrency(abs(roommate.amount)),
                    color = amountColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = if (amountColor == AppSuccess) AppAccent else AppSuccess
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = actionText,
                        color = if (amountColor == AppSuccess) AppMuted else AppSurface,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun SettlementEmptyCard(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AppAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_24),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = text,
                color = AppMuted,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
