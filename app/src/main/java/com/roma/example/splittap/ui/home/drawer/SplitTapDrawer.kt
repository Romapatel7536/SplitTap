package com.roma.example.splittap.ui.home.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppAccent
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMark
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppTransparent
import com.roma.example.splittap.ui.home.HomeDestination
import com.roma.example.splittap.ui.home.HomeUiState

@Composable
fun SplitTapDrawer(
    selectedDestination: HomeDestination,
    uiState: HomeUiState,
    onClose: () -> Unit,
    onDestinationSelected: (HomeDestination) -> Unit
) {
    val displayName = uiState.userName.ifBlank {
        stringResource(R.string.home_signed_in_fallback)
    }
    val displayEmail = uiState.userEmail.ifBlank {
        stringResource(R.string.home_no_email_available)
    }

    ModalDrawerSheet(
        drawerContainerColor = AppSurface,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.86f)
            .widthIn(max = 320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppMark(size = 54.dp)
                Spacer(modifier = Modifier.width(12.dp))
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
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onClose),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_24),
                        contentDescription = stringResource(R.string.home_cd_close_menu),
                        tint = AppInk,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HomeDestination.entries.forEach { destination ->
                NavigationDrawerItem(
                    icon = {
                        DrawerIcon(
                            iconRes = destination.iconRes,
                            selected = selectedDestination == destination
                        )
                    },
                    label = {
                        Column {
                            Text(
                                text = stringResource(destination.titleRes),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(destination.subtitleRes),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (selectedDestination == destination) {
                                    AppSurface.copy(alpha = 0.82f)
                                } else {
                                    AppMuted
                                }
                            )
                        }
                    },
                    selected = selectedDestination == destination,
                    onClick = { onDestinationSelected(destination) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = AppPrimary,
                        selectedTextColor = AppSurface,
                        unselectedContainerColor = AppTransparent,
                        unselectedTextColor = AppInk
                    ),
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = AppAccent.copy(alpha = 0.55f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.home_next_feature),
                        color = AppInk,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.home_next_feature_body),
                        color = AppMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
@Composable
fun DrawerIcon(
    iconRes: Int,
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(if (selected) AppSurface.copy(alpha = 0.18f) else AppAccent.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = if (selected) AppSurface else AppPrimary,
            modifier = Modifier.size(21.dp)
        )
    }
}
