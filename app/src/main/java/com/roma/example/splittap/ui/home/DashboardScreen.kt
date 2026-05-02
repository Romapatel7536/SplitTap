package com.roma.example.splittap.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.viewmodel.HomeUiState

@Composable
fun DashboardScreen(
    uiState: HomeUiState,
    onOpenDrawer: () -> Unit,
    onAddExpense: () -> Unit,
    onCreateGroup: () -> Unit,
    onAddRoommate: () -> Unit,
    onShowPaymentDetection: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val compactWidth = maxWidth < 360.dp
        val horizontalPadding = if (compactWidth) 16.dp else 22.dp
        val contentSpacing = if (compactWidth) 12.dp else 18.dp
        val headerHeight = when {
            maxHeight < 650.dp -> 250.dp
            maxWidth > 600.dp -> 340.dp
            else -> 292.dp
        }
        val contentTopPadding = headerHeight - when {
            maxHeight < 650.dp -> 24.dp
            maxWidth > 600.dp -> 56.dp
            else -> 40.dp
        }

        DashboardHeader(
            totalBalance = uiState.totalBalance,
            headerHeight = headerHeight,
            horizontalPadding = horizontalPadding,
            onOpenDrawer = onOpenDrawer
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .fillMaxHeight()
                .padding(top = contentTopPadding)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(AppBackground)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(contentSpacing)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(if (compactWidth) 10.dp else 14.dp)) {
                BalanceMetricCard(
                    title = stringResource(R.string.home_you_are_owed),
                    value = formatCurrency(uiState.youAreOwed),
                    tone = AppSuccess,
                    iconRes = R.drawable.ic_trending_up_24,
                    modifier = Modifier.weight(1f)
                )
                BalanceMetricCard(
                    title = stringResource(R.string.home_you_owe),
                    value = formatCurrency(uiState.youOwe),
                    tone = AppDanger,
                    iconRes = R.drawable.ic_trending_down_24,
                    modifier = Modifier.weight(1f)
                )
            }

            SectionTitle(title = stringResource(R.string.home_quick_actions))
            Row(horizontalArrangement = Arrangement.spacedBy(if (compactWidth) 10.dp else 14.dp)) {
                QuickActionCard(
                    title = stringResource(R.string.home_add_expense),
                    caption = stringResource(R.string.home_add_expense_caption),
                    iconRes = R.drawable.ic_add_24,
                    onClick = onAddExpense,
                    compact = compactWidth,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = stringResource(R.string.home_create_group),
                    caption = stringResource(R.string.home_create_group_caption),
                    iconRes = R.drawable.ic_groups_24,
                    onClick = onCreateGroup,
                    compact = compactWidth,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(if (compactWidth) 10.dp else 14.dp)) {
                QuickActionCard(
                    title = stringResource(R.string.home_add_roommate),
                    caption = stringResource(R.string.home_add_roommate_caption),
                    iconRes = R.drawable.ic_person_add_24,
                    onClick = onAddRoommate,
                    compact = compactWidth,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = stringResource(R.string.home_payment_alert),
                    caption = stringResource(R.string.home_payment_alert_caption),
                    iconRes = R.drawable.ic_notifications_24,
                    onClick = onShowPaymentDetection,
                    compact = compactWidth,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(
                    title = stringResource(R.string.home_roommates_title),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.home_view_all),
                    color = AppPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right_24),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            RoommatePreviewRow(
                name = stringResource(R.string.home_test_roommate_a),
                amount = stringResource(R.string.home_test_roommate_a_balance),
                amountColor = AppDanger
            )
            RoommatePreviewRow(
                name = stringResource(R.string.home_test_roommate_b),
                amount = stringResource(R.string.home_test_roommate_b_balance),
                amountColor = AppSuccess
            )
            RoommatePreviewRow(
                name = stringResource(R.string.home_test_roommate_c),
                amount = stringResource(R.string.home_test_roommate_c_balance),
                amountColor = AppDanger
            )

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    caption: String,
    iconRes: Int,
    onClick: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(62.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = AppBackground,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (compact) 12.dp else 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = AppInk,
                modifier = Modifier.size(if (compact) 20.dp else 22.dp)
            )
            Spacer(modifier = Modifier.width(if (compact) 8.dp else 12.dp))
            Text(
                text = title,
                color = AppInk,
                style = if (compact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BalanceMetricCard(
    title: String,
    value: String,
    tone: Color,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(tone.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = tone,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    color = AppMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = value,
                    color = tone,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RoommatePreviewRow(
    name: String,
    amount: String,
    amountColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp),
        shape = RoundedCornerShape(22.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppAccent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_roommate_initial),
                    color = AppPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                color = AppInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = amount,
                color = amountColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right_24),
                contentDescription = null,
                tint = AppMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
