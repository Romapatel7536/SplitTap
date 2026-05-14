package com.roma.example.splittap.ui.roommate

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.expense.SplitMemberUi
import com.roma.example.splittap.ui.home.AppAccent
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppDanger
import com.roma.example.splittap.ui.home.AppGradientButton
import com.roma.example.splittap.ui.home.AppInitialAvatar
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppScreenTopBar
import com.roma.example.splittap.ui.home.AppSearchField
import com.roma.example.splittap.ui.home.AppSuccess
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppUiTokens
import com.roma.example.splittap.ui.home.RoommateBalanceUi
import com.roma.example.splittap.ui.home.formatCurrency
import kotlin.math.abs

@Composable
fun RoommatesScreen(
    roommates: List<RoommateBalanceUi>,
    friends: List<SplitMemberUi>,
    onBack: () -> Unit,
    onAddRoommate: () -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val visibleFriends = friends.filter { friend ->
        friend.name.contains(searchQuery, ignoreCase = true)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            AppScreenTopBar(
                title = stringResource(R.string.roommates_friends_title),
                onBack = onBack,
                trailing = if (friends.isEmpty()) {
                    null
                } else {
                    { AddCircleButton(onClick = onAddRoommate) }
                }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = horizontalPadding, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (friends.isEmpty()) {
                    RoommatesEmptyState(onAddRoommate = onAddRoommate)
                } else {
                    AppSearchField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = stringResource(R.string.roommates_search_people)
                    )

                    visibleFriends.forEach { friend ->
                        val balance = roommates
                            .firstOrNull { it.uid == friend.uid }
                            ?.amount ?: 0.0

                        RoommateBalanceRow(
                            friend = friend,
                            balance = balance
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddCircleButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = AppPrimary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_add_24),
                contentDescription = stringResource(R.string.roommates_add_person),
                tint = AppSurface,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun RoommateBalanceRow(
    friend: SplitMemberUi,
    balance: Double
) {
    val amountColor = when {
        balance > 0.0 -> AppSuccess
        balance < 0.0 -> AppDanger
        else -> AppMuted
    }
    val name = friend.name.ifBlank { stringResource(R.string.home_unknown_user) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppUiTokens.CardCorner),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppInitialAvatar(name = name, size = AppUiTokens.AvatarMedium)
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = AppInk,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AppAccent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_dashboard_24),
                            contentDescription = null,
                            tint = AppPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.add_roommate_type_roommate),
                            color = AppPrimary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = when {
                        balance > 0.0 -> stringResource(R.string.roommates_owes_you)
                        balance < 0.0 -> stringResource(R.string.roommates_you_owe)
                        else -> stringResource(R.string.roommates_settled_up)
                    },
                    color = amountColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                if (balance != 0.0) {
                    Text(
                        text = formatCurrency(abs(balance)),
                        color = amountColor,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RoommatesEmptyState(
    onAddRoommate: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .background(AppAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_person_add_24),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.roommates_empty_title),
                color = AppInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.roommates_empty_body),
                color = AppMuted,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            AppGradientButton(
                text = stringResource(R.string.roommates_add_person),
                onClick = onAddRoommate,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
