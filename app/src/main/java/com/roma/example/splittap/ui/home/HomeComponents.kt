package com.roma.example.splittap.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CompactHeader(
    title: String,
    subtitle: String,
    onOpenDrawer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
            .background(AppPrimary)
            .statusBarsPadding()
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .widthIn(max = 560.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = AppSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = AppSurface.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButtonSurface(
                iconRes = R.drawable.ic_menu_24,
                contentDescription = stringResource(R.string.home_cd_open_menu),
                onClick = onOpenDrawer
            )
        }
    }
}

@Composable
fun IconButtonSurface(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(
                onClickLabel = contentDescription,
                onClick = onClick
            ),
        shape = CircleShape,
        color = AppSurface.copy(alpha = 0.16f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                tint = AppSurface,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
fun EmptyStatePanel(
    title: String,
    body: String,
    action: String,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmptyIllustration()
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = title,
                color = AppInk,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = body,
                color = AppMuted,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onActionClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
            ) {
                Text(action)
            }
        }
    }
}

@Composable
fun EmptyIllustration() {
    Box(
        modifier = Modifier
            .size(132.dp)
            .clip(CircleShape)
            .background(AppAccent.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(AppSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_credit_card_24),
                contentDescription = stringResource(R.string.home_cd_empty_illustration),
                tint = AppPrimary,
                modifier = Modifier.size(44.dp)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-24).dp, y = (-26).dp)
                .size(26.dp)
                .clip(CircleShape)
                .background(AppSuccess),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check_24),
                contentDescription = null,
                tint = AppSurface,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun AppMark(size: androidx.compose.ui.unit.Dp = 46.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(AppPrimary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_credit_card_24),
            contentDescription = stringResource(R.string.home_cd_app_mark),
            tint = AppSurface,
            modifier = Modifier.size(size * 0.54f)
        )
    }
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = AppInk,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.CANADA).format(amount)
}
