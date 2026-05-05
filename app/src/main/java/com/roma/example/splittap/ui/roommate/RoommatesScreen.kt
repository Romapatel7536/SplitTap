package com.roma.example.splittap.ui.roommate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.roma.example.splittap.ui.home.AppDanger
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSuccess
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.RoommateBalanceUi
import com.roma.example.splittap.ui.home.formatCurrency
import kotlin.math.abs

@Composable
fun RoommatesScreen(
    roommates: List<RoommateBalanceUi>,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    onAddRoommate: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 18.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            RoommatesHeader(
                onBack = onBack,
                onOpenDrawer = onOpenDrawer
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .widthIn(max = 560.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .padding(top = 24.dp, bottom = 28.dp)
            ) {
                Button(
                    onClick = onAddRoommate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_24),
                        contentDescription = null,
                        tint = AppSurface,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.roommates_add_roommate),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                if (roommates.isEmpty()) {
                    RoommatesEmptyState(
                        onAddRoommate = onAddRoommate,
                        modifier = Modifier.padding(top = 22.dp)
                    )
                } else {
                    roommates.forEach { roommate ->
                        Spacer(modifier = Modifier.height(12.dp))
                        RoommateBalanceRow(roommate = roommate)
                    }
                }
            }
        }
    }
}

@Composable
private fun RoommatesHeader(
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(AppPrimary)
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderIconButton(
            iconRes = R.drawable.ic_chevron_right_24,
            contentDescription = stringResource(R.string.home_cd_back),
            onClick = onBack,
            rotateBack = true
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = stringResource(R.string.roommates_title),
            color = AppSurface,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        HeaderIconButton(
            iconRes = R.drawable.ic_menu_24,
            contentDescription = stringResource(R.string.home_cd_open_menu),
            onClick = onOpenDrawer
        )
    }
}

@Composable
private fun HeaderIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    rotateBack: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = AppSurface,
            modifier = Modifier
                .size(26.dp)
                .rotate(if (rotateBack) 180f else 0f)
        )
    }
}

@Composable
private fun RoommateBalanceRow(roommate: RoommateBalanceUi) {
    val isOwedToYou = roommate.amount > 0
    val amountColor = if (isOwedToYou) AppSuccess else AppDanger
    val name = roommate.name.ifBlank { stringResource(R.string.home_unknown_user) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp),
        shape = RoundedCornerShape(24.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AppAccent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase()
                        ?: stringResource(R.string.home_roommate_initial),
                    color = AppPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name,
                color = AppInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isOwedToYou) {
                        stringResource(R.string.roommates_owes_you)
                    } else {
                        stringResource(R.string.roommates_you_owe)
                    },
                    color = amountColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formatCurrency(abs(roommate.amount)),
                    color = amountColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RoommatesEmptyState(
    onAddRoommate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(78.dp)
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

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.roommates_empty_title),
                color = AppInk,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.roommates_empty_body),
                color = AppMuted,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onAddRoommate,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
            ) {
                Text(
                    text = stringResource(R.string.roommates_add_roommate),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
