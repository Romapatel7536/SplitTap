package com.roma.example.splittap.ui.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppPrimaryDark
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.IconButtonSurface
import com.roma.example.splittap.ui.home.formatCurrency

@Composable
fun DashboardHeader(
    totalBalance: Double,
    headerHeight: androidx.compose.ui.unit.Dp,
    horizontalPadding: androidx.compose.ui.unit.Dp,
    onOpenDrawer: () -> Unit
) {
    val balanceStatusRes = when {
        totalBalance > 0.0 -> R.string.home_you_are_owed
        totalBalance < 0.0 -> R.string.home_you_owe
        else -> R.string.home_no_shared_expenses
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(
                Brush.linearGradient(
                    listOf(AppPrimary, AppPrimaryDark)
                )
            )
            .statusBarsPadding()
            .padding(horizontal = horizontalPadding, vertical = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 92.dp, y = (-88).dp)
                .size(210.dp)
                .clip(CircleShape)
                .background(AppSurface.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-92).dp, y = 72.dp)
                .size(164.dp)
                .clip(CircleShape)
                .background(AppSurface.copy(alpha = 0.08f))
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.home_dashboard_title),
                        color = AppSurface,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButtonSurface(
                    iconRes = R.drawable.ic_menu_24,
                    contentDescription = stringResource(R.string.home_cd_open_menu),
                    onClick = onOpenDrawer
                )
            }

            Spacer(
                modifier = Modifier.height(
                    if (headerHeight < 260.dp) 16.dp else 24.dp
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.home_total_balance),
                    color = AppSurface.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(totalBalance),
                    color = AppSurface,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(balanceStatusRes),
                    color = AppSurface.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
