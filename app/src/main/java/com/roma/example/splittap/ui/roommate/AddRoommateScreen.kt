package com.roma.example.splittap.ui.roommate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSurface

@Composable
fun AddRoommateScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    onBack: () -> Unit,
    onSendInvitation: () -> Unit
) {
    val canSend = email.isNotBlank()

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            Surface(color = AppBackground) {
                Button(
                    onClick = onSendInvitation,
                    enabled = canSend,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppPrimary,
                        disabledContainerColor = AppBorder,
                        disabledContentColor = AppSurface
                    )
                ) {
                    Text(
                        text = stringResource(R.string.add_roommate_send_invitation),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppBackground)
        ) {
            val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .widthIn(max = 560.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .padding(top = 34.dp, bottom = 28.dp)
            ) {
                AddRoommateHeader(onBack = onBack)

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = stringResource(R.string.add_roommate_email_address),
                    color = AppInk,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                AddRoommateEmailField(
                    value = email,
                    onValueChange = onEmailChange
                )

                Spacer(modifier = Modifier.height(32.dp))

                InvitationInfoCard()
            }
        }
    }
}

@Composable
private fun AddRoommateHeader(onBack: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right_24),
                contentDescription = stringResource(R.string.home_cd_back),
                tint = AppInk,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(180f)
            )
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = stringResource(R.string.add_roommate_title),
            color = AppInk,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AddRoommateEmailField(
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        textStyle = TextStyle(
            color = AppInk,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Medium
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(AppSurface, RoundedCornerShape(18.dp))
                    .border(1.dp, AppBorder, RoundedCornerShape(18.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.add_roommate_email_hint),
                        color = AppMuted,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
private fun InvitationInfoCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Text(
            text = stringResource(R.string.add_roommate_invitation_notice),
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
            color = AppMuted,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
