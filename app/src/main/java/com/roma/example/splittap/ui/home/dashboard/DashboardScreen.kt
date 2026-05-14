package com.roma.example.splittap.ui.home.dashboard

import android.text.format.DateUtils
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.data.model.Expense
import com.roma.example.splittap.ui.home.AppAccent
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppGradientEnd
import com.roma.example.splittap.ui.home.AppGradientStart
import com.roma.example.splittap.ui.home.AppIconBlue
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSuccess
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppWarning
import com.roma.example.splittap.ui.home.HomeUiState
import com.roma.example.splittap.ui.home.formatCurrency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    uiState: HomeUiState,
    onOpenProfile: () -> Unit,
    onAddExpense: () -> Unit,
    onCreateGroup: () -> Unit,
    onOpenGroups: () -> Unit,
    onOpenRoommates: () -> Unit,
    onOpenExpenses: () -> Unit,
    onSettleUp: () -> Unit,
    onShowPaymentDetection: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val compactWidth = maxWidth < 370.dp
        val compactHeight = maxHeight < 690.dp
        val compactLayout = compactWidth || compactHeight
        val horizontalPadding = if (compactWidth) 18.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            DashboardHeader(
                userName = uiState.userName,
                totalBalance = uiState.totalBalance,
                youOwe = uiState.youOwe,
                youAreOwed = uiState.youAreOwed,
                horizontalPadding = horizontalPadding,
                compact = compactLayout,
                onNotificationsClick = onShowPaymentDetection,
                onProfileClick = onOpenProfile
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 480.dp)
                        .padding(
                            horizontal = horizontalPadding,
                            vertical = if (compactLayout) 20.dp else 24.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(if (compactLayout) 18.dp else 24.dp)
                ) {
                    QuickActionsRow(
                        compact = compactWidth,
                        onAddExpense = onAddExpense,
                        onOpenGroups = onOpenGroups,
                        onSettleUp = onSettleUp
                    )

                    RoommatesShortcutCard(onClick = onOpenRoommates)

                    DashboardSectionHeader(
                        title = stringResource(R.string.home_active_groups),
                        action = stringResource(R.string.home_see_all),
                        onActionClick = onOpenGroups
                    )

                    EmptyDashboardCard(
                        iconRes = R.drawable.ic_groups_24,
                        title = stringResource(R.string.home_no_active_groups),
                        body = stringResource(R.string.home_no_active_groups_body),
                        action = stringResource(R.string.home_create_group),
                        onActionClick = onCreateGroup
                    )

                    DashboardSectionHeader(
                        title = stringResource(R.string.home_recent_expenses),
                        action = stringResource(R.string.home_see_all),
                        onActionClick = onOpenExpenses
                    )

                    if (uiState.recentExpenses.isEmpty()) {
                        EmptyDashboardCard(
                            iconRes = R.drawable.ic_receipt_24,
                            title = stringResource(R.string.home_no_recent_expenses),
                            body = stringResource(R.string.home_no_recent_expenses_body),
                            action = stringResource(R.string.home_add_expense),
                            onActionClick = onAddExpense
                        )
                    } else {
                        uiState.recentExpenses.forEach { expense ->
                            RecentExpenseCard(
                                expense = expense,
                                userEmail = uiState.userEmail
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    compact: Boolean,
    onAddExpense: () -> Unit,
    onOpenGroups: () -> Unit,
    onSettleUp: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 10.dp else 14.dp)
    ) {
        DashboardActionCard(
            title = stringResource(R.string.home_add_expense),
            iconRes = R.drawable.ic_add_24,
            iconBrush = Brush.linearGradient(listOf(AppGradientEnd, AppPrimary)),
            onClick = onAddExpense,
            compact = compact,
            modifier = Modifier.weight(1f)
        )

        DashboardActionCard(
            title = stringResource(R.string.home_groups_title),
            iconRes = R.drawable.ic_groups_24,
            iconBrush = Brush.linearGradient(listOf(AppGradientStart, AppIconBlue)),
            onClick = onOpenGroups,
            compact = compact,
            modifier = Modifier.weight(1f)
        )

        DashboardActionCard(
            title = stringResource(R.string.home_settle_up),
            iconRes = R.drawable.ic_receipt_24,
            iconBrush = Brush.linearGradient(listOf(AppSuccess, AppSuccess)),
            onClick = onSettleUp,
            compact = compact,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DashboardActionCard(
    title: String,
    iconRes: Int,
    iconBrush: Brush,
    onClick: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .heightIn(min = if (compact) 112.dp else 124.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GradientIconTile(
                iconRes = iconRes,
                brush = iconBrush,
                size = if (compact) 48.dp else 54.dp,
                iconSize = if (compact) 23.dp else 26.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = AppInk,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RoommatesShortcutCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GradientIconTile(
                iconRes = R.drawable.ic_person_add_24,
                brush = Brush.linearGradient(listOf(AppWarning, AppWarning)),
                size = 58.dp,
                iconSize = 29.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_roommates_friends_title),
                    color = AppInk,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.home_roommates_friends_body),
                    color = AppMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_chevron_right_24),
                contentDescription = null,
                tint = AppMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun DashboardSectionHeader(
    title: String,
    action: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = AppInk,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = action,
            color = AppPrimary,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onActionClick)
        )
    }
}

@Composable
private fun EmptyDashboardCard(
    iconRes: Int,
    title: String,
    body: String,
    action: String,
    onActionClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = AppInk,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = body,
                    color = AppMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = action,
                color = AppPrimary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
private fun RecentExpenseCard(
    expense: Expense,
    userEmail: String
) {
    val title = expense.merchant.ifBlank {
        stringResource(R.string.home_unknown_expense)
    }
    val paidBy = if (expense.paidByEmail.equals(userEmail, ignoreCase = true)) {
        stringResource(R.string.add_expense_you)
    } else {
        expense.paidByEmail.ifBlank {
            stringResource(R.string.home_unknown_user)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = title,
                    color = AppInk,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = formatCurrency(expense.amount),
                    color = AppInk,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppBorder)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.home_paid_by_format, paidBy),
                    color = AppMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                ExpenseDateText(createdAt = expense.createdAt)
            }
        }
    }
}

@Composable
private fun ExpenseDateText(createdAt: Long) {
    val formattedDate = remember(createdAt) {
        SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(createdAt))
    }
    val dateLabel = when {
        DateUtils.isToday(createdAt) -> stringResource(R.string.home_today)
        DateUtils.isToday(createdAt + DateUtils.DAY_IN_MILLIS) -> stringResource(R.string.home_yesterday)
        else -> formattedDate
    }

    Text(
        text = dateLabel,
        color = AppMuted,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1
    )
}

@Composable
private fun GradientIconTile(
    iconRes: Int,
    brush: Brush,
    size: androidx.compose.ui.unit.Dp,
    iconSize: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(brush),
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
