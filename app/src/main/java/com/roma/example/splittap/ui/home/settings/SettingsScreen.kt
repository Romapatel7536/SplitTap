package com.roma.example.splittap.ui.home.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppDanger
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMark
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.CompactHeader
import com.roma.example.splittap.ui.home.HomeUiState
import com.roma.example.splittap.ui.home.SectionTitle


@Composable
fun SettingsScreen(
    uiState: HomeUiState,
    onOpenDrawer: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val displayName = uiState.userName.ifBlank {
        stringResource(R.string.home_signed_in)
    }
    val displayEmail = uiState.userEmail.ifBlank {
        stringResource(R.string.home_no_email_available)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        CompactHeader(
            title = stringResource(R.string.home_settings_title),
            subtitle = stringResource(R.string.home_settings_header_subtitle),
            onOpenDrawer = onOpenDrawer
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .widthIn(max = 560.dp)
                .align(Alignment.CenterHorizontally)
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppMark(size = 58.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = displayName,
                            color = AppInk,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = displayEmail,
                            color = AppMuted,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (uiState.userContact.isNotBlank()) {
                            Text(
                                text = uiState.userContact,
                                color = AppMuted,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            SettingsGroup(
                title = stringResource(R.string.home_notifications),
                rows = listOf(
                    stringResource(R.string.home_payment_detection) to stringResource(R.string.home_payment_detection_caption),
                    stringResource(R.string.home_expense_reminders) to stringResource(R.string.home_expense_reminders_caption),
                    stringResource(R.string.home_settlement_notifications) to stringResource(R.string.home_settlement_notifications_caption)
                )
            )

            SettingsGroup(
                title = stringResource(R.string.home_support),
                rows = listOf(
                    stringResource(R.string.home_help_center) to stringResource(R.string.home_help_center_caption),
                    stringResource(R.string.home_contact_support) to stringResource(R.string.home_contact_support_caption),
                    stringResource(R.string.home_privacy_policy) to stringResource(R.string.home_privacy_policy_caption)
                )
            )

            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppDanger)
            ) {
                Text(stringResource(R.string.home_logout))
            }

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}


@Composable
fun SettingsGroup(
    title: String,
    rows: List<Pair<String, String>>
) {
    Column {
        SectionTitle(title = title)
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = AppSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                rows.forEachIndexed { index, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = row.first,
                                color = AppInk,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = row.second,
                                color = AppMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_right_24),
                            contentDescription = null,
                            tint = AppMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    if (index != rows.lastIndex) {
                        HorizontalDivider(color = AppBorder)
                    }
                }
            }
        }
    }
}
