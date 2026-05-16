package com.roma.example.splittap.ui.roommate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppAccent
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppFormField
import com.roma.example.splittap.ui.home.AppGradientButton
import com.roma.example.splittap.ui.home.AppIconBlue
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppScreenTopBar
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppUiTokens

@Composable
fun AddRoommateScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    onBack: () -> Unit,
    onSendInvitation: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var isRoommate by rememberSaveable { mutableStateOf(true) }
    val canSend = email.isNotBlank()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            AppScreenTopBar(
                title = stringResource(R.string.add_roommate_friend_title),
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = horizontalPadding, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                TypeSelector(
                    isRoommate = isRoommate,
                    onRoommateSelected = { isRoommate = true },
                    onFriendSelected = { isRoommate = false }
                )

                AppFormField(
                    label = stringResource(R.string.add_roommate_name),
                    value = name,
                    onValueChange = { name = it },
                    placeholder = stringResource(R.string.add_roommate_name_hint),
                    iconRes = R.drawable.ic_person_24
                )

                AppFormField(
                    label = stringResource(R.string.add_roommate_email),
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = stringResource(R.string.add_roommate_email_modern_hint),
                    iconRes = R.drawable.ic_mail_24,
                    keyboardType = KeyboardType.Email
                )

                AppFormField(
                    label = stringResource(R.string.add_roommate_phone_optional),
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = stringResource(R.string.add_roommate_phone_hint),
                    iconRes = R.drawable.ic_phone_24,
                    keyboardType = KeyboardType.Phone
                )

                ExplainerCard(
                    text = if (isRoommate) {
                        stringResource(R.string.add_roommate_roommate_explainer)
                    } else {
                        stringResource(R.string.add_roommate_friend_explainer)
                    }
                )

                AppGradientButton(
                    text = stringResource(R.string.roommates_add_person),
                    onClick = onSendInvitation,
                    enabled = canSend,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TypeSelector(
    isRoommate: Boolean,
    onRoommateSelected: () -> Unit,
    onFriendSelected: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.add_roommate_type),
            color = AppInk,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TypeOption(
                label = stringResource(R.string.add_roommate_type_roommate),
                iconRes = R.drawable.ic_dashboard_24,
                selected = isRoommate,
                onClick = onRoommateSelected,
                modifier = Modifier.weight(1f)
            )

            TypeOption(
                label = stringResource(R.string.add_roommate_type_friend),
                iconRes = R.drawable.ic_person_24,
                selected = !isRoommate,
                onClick = onFriendSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TypeOption(
    label: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .heightIn(min = AppUiTokens.ButtonMinHeight)
            .clip(RoundedCornerShape(AppUiTokens.ControlCorner))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppUiTokens.ControlCorner),
        color = if (selected) AppIconBlue else AppSurface,
        border = BorderStroke(1.dp, if (selected) AppIconBlue else AppBorder),
        shadowElevation = if (selected) 8.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = if (selected) AppSurface else AppMuted,
                modifier = Modifier.size(AppUiTokens.FieldIconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
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
private fun ExplainerCard(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppAccent
    ) {
        Text(
            text = text,
            color = AppMuted,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
