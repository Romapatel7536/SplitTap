package com.roma.example.splittap.ui.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
fun CreateGroupScreen(
    isSaving: Boolean,
    onBack: () -> Unit,
    onCreateGroup: (
        name: String,
        description: String,
        groupType: String
    ) -> Unit
) {
    var groupName by rememberSaveable { mutableStateOf("") }
    var memberEmail by rememberSaveable { mutableStateOf("") }
    val canCreate = groupName.isNotBlank() && !isSaving

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            CreateGroupBottomBar(
                canCreate = canCreate,
                onCreate = {
                    onCreateGroup(
                        groupName.trim(),
                        memberEmail.trim(),
                        GroupTypeCode.Home.name
                    )
                }
            )
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
                    .padding(top = 32.dp, bottom = 28.dp)
            ) {
                FlatTopBar(
                    title = stringResource(R.string.create_group_title),
                    onBack = onBack
                )

                Spacer(modifier = Modifier.height(32.dp))

                GroupFormTextField(
                    label = stringResource(R.string.create_group_name),
                    value = groupName,
                    onValueChange = { groupName = it },
                    placeholder = stringResource(R.string.create_group_name_hint)
                )

                Spacer(modifier = Modifier.height(24.dp))

                FormSectionLabel(text = stringResource(R.string.create_group_add_members))
                GroupBareTextField(
                    value = memberEmail,
                    onValueChange = { memberEmail = it },
                    placeholder = stringResource(R.string.create_group_member_email_hint),
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(8.dp))

                AddMemberButton()

                Spacer(modifier = Modifier.height(24.dp))

                InvitationInfoBox()
            }
        }
    }
}

private enum class GroupTypeCode {
    Home
}

@Composable
private fun FlatTopBar(
    title: String,
    onBack: () -> Unit
) {
    Row(
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
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            color = AppInk,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GroupFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        FormSectionLabel(text = label)
        GroupBareTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder
        )
    }
}

@Composable
private fun GroupBareTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(
            color = AppInk,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Medium
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(AppSurface, RoundedCornerShape(16.dp))
                    .border(1.dp, AppBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
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
private fun FormSectionLabel(text: String) {
    Text(
        text = text,
        color = AppInk,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun AddMemberButton() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        color = AppBackground,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_24),
                contentDescription = null,
                tint = AppInk,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.create_group_add_another_member),
                color = AppInk,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun InvitationInfoBox() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 18.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.create_group_invitation_notice),
                color = AppMuted,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CreateGroupBottomBar(
    canCreate: Boolean,
    onCreate: () -> Unit
) {
    Surface(color = AppBackground) {
        Button(
            onClick = onCreate,
            enabled = canCreate,
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
                text = stringResource(R.string.create_group_save),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
