package com.roma.example.splittap.ui.expense

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.data.model.ExpenseSplitType
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppGradientEnd
import com.roma.example.splittap.ui.home.AppGradientStart
import com.roma.example.splittap.ui.home.AppInitialAvatar
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSuccess
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppUiTokens
import com.roma.example.splittap.ui.home.formatCurrency

internal enum class SplitOption(@StringRes val titleRes: Int) {
    Equal(R.string.split_options_equal),
    Exact(R.string.split_options_exact),
    Percent(R.string.split_options_percent),
    Shares(R.string.split_options_shares),
    Adjustment(R.string.split_options_adjustment)
}

internal fun SplitOption.toExpenseSplitType(): ExpenseSplitType {
    return if (this == SplitOption.Equal) {
        ExpenseSplitType.EQUAL
    } else {
        ExpenseSplitType.CUSTOM
    }
}

@Composable
internal fun SplitOptionsScreen(
    amount: Double?,
    members: List<SplitMemberUi>,
    selectedRoommateIds: List<String>,
    selectedOption: SplitOption,
    onOptionSelected: (SplitOption) -> Unit,
    onClose: () -> Unit,
    onDone: () -> Unit
) {
    val selectedMembers = remember(members, selectedRoommateIds) {
        members.filter { selectedRoommateIds.contains(it.uid) }
    }
    val participants = listOf(
        SplitMemberUi(uid = "", name = stringResource(R.string.add_expense_you))
    ) + selectedMembers
    val totalAmount = amount ?: 0.0
    val participantShare = if (participants.isNotEmpty()) totalAmount / participants.size else 0.0

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            SplitOptionsTopBar(
                onClose = onClose,
                onDone = onDone
            )

            LazyColumn(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 480.dp)
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    SplitOptionsTotalCard(totalAmount = totalAmount)
                }

                item {
                    SplitOptionSection(
                        selectedOption = selectedOption,
                        onOptionSelected = onOptionSelected
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.add_expense_participants),
                        color = AppInk,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                participants.forEach { participant ->
                    item {
                        SplitOptionParticipantRow(
                            participant = participant,
                            amount = participantShare
                        )
                    }
                }

                item {
                    SplitOptionsSummaryCard(
                        totalAllocated = totalAmount,
                        remaining = 0.0
                    )
                }
            }
        }
    }
}

@Composable
private fun SplitOptionsTopBar(
    onClose: () -> Unit,
    onDone: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
                        .clickable(onClick = onClose),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_24),
                        contentDescription = stringResource(R.string.split_options_close),
                        tint = AppMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = stringResource(R.string.split_options_title),
                    color = AppInk,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onDone)
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check_24),
                        contentDescription = null,
                        tint = AppPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.split_options_done),
                        color = AppPrimary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(color = AppBorder)
        }
    }
}

@Composable
private fun SplitOptionsTotalCard(totalAmount: Double) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 116.dp),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd)))
                .padding(horizontal = 18.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.split_options_total_amount),
                color = AppSurface.copy(alpha = 0.88f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatCurrency(totalAmount),
                color = AppSurface,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.split_options_paid_by_you),
                color = AppSurface.copy(alpha = 0.92f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SplitOptionSection(
    selectedOption: SplitOption,
    onOptionSelected: (SplitOption) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.add_expense_split_type),
            color = AppInk,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(SplitOption.Equal, SplitOption.Exact, SplitOption.Percent).forEach { option ->
                SplitOptionChip(
                    option = option,
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(SplitOption.Shares, SplitOption.Adjustment).forEach { option ->
                SplitOptionChip(
                    option = option,
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SplitOptionChip(
    option: SplitOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .heightIn(min = AppUiTokens.ChipMinHeight)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) AppPrimary else AppSurface,
        border = BorderStroke(1.dp, if (selected) AppPrimary else AppBorder)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(option.titleRes),
                color = if (selected) AppSurface else AppMuted,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SplitOptionParticipantRow(
    participant: SplitMemberUi,
    amount: Double
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppInitialAvatar(name = participant.name, size = AppUiTokens.AvatarMedium)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = participant.name,
                color = AppInk,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatCurrency(amount),
                color = AppPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SplitOptionsSummaryCard(
    totalAllocated: Double,
    remaining: Double
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            SummaryRow(
                label = stringResource(R.string.split_options_total_allocated),
                amount = totalAllocated,
                amountColor = AppInk
            )
            HorizontalDivider(
                color = AppBorder,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            SummaryRow(
                label = stringResource(R.string.split_options_remaining),
                amount = remaining,
                amountColor = AppSuccess
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    amount: Double,
    amountColor: androidx.compose.ui.graphics.Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            color = AppMuted,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatCurrency(amount),
            color = amountColor,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
