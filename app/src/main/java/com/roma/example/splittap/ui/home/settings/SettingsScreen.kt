package com.roma.example.splittap.ui.home.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppAccent
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppDanger
import com.roma.example.splittap.ui.home.AppFormField
import com.roma.example.splittap.ui.home.AppGradientButton
import com.roma.example.splittap.ui.home.AppGradientEnd
import com.roma.example.splittap.ui.home.AppGradientIconTile
import com.roma.example.splittap.ui.home.AppGradientStart
import com.roma.example.splittap.ui.home.AppInitialAvatar
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppScreenTopBar
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppSwitch
import com.roma.example.splittap.ui.home.AppUiTokens
import com.roma.example.splittap.ui.home.AppWarning
import com.roma.example.splittap.ui.home.HomeUiState

private enum class SettingsPage {
    Profile,
    PersonalInfo,
    Notifications,
    PaymentDetection,
    PaymentMethods,
    PrivacySecurity,
    HelpSupport
}

@Composable
fun SettingsScreen(
    uiState: HomeUiState,
    onBack: () -> Unit,
    onLogoutClick: () -> Unit,
    onOpenGroups: () -> Unit,
    onOpenRoommates: () -> Unit,
    onOpenExpenses: () -> Unit
) {
    var pageName by rememberSaveable { mutableStateOf(SettingsPage.Profile.name) }
    val page = SettingsPage.valueOf(pageName)
    val openPage: (SettingsPage) -> Unit = { pageName = it.name }

    when (page) {
        SettingsPage.Profile -> ProfilePage(
            uiState = uiState,
            onBack = onBack,
            onOpenPage = openPage,
            onLogoutClick = onLogoutClick,
            onOpenGroups = onOpenGroups,
            onOpenRoommates = onOpenRoommates,
            onOpenExpenses = onOpenExpenses
        )
        SettingsPage.PersonalInfo -> PersonalInfoPage(
            uiState = uiState,
            onBack = { pageName = SettingsPage.Profile.name }
        )
        SettingsPage.Notifications -> NotificationsPage(
            onBack = { pageName = SettingsPage.Profile.name }
        )
        SettingsPage.PaymentDetection -> PaymentDetectionPage(
            onBack = { pageName = SettingsPage.Profile.name }
        )
        SettingsPage.PaymentMethods -> PaymentMethodsPage(
            onBack = { pageName = SettingsPage.Profile.name }
        )
        SettingsPage.PrivacySecurity -> PrivacySecurityPage(
            onBack = { pageName = SettingsPage.Profile.name }
        )
        SettingsPage.HelpSupport -> HelpSupportPage(
            onBack = { pageName = SettingsPage.Profile.name }
        )
    }
}

@Composable
private fun ProfilePage(
    uiState: HomeUiState,
    onBack: () -> Unit,
    onOpenPage: (SettingsPage) -> Unit,
    onLogoutClick: () -> Unit,
    onOpenGroups: () -> Unit,
    onOpenRoommates: () -> Unit,
    onOpenExpenses: () -> Unit
) {
    val displayName = uiState.userName.ifBlank { stringResource(R.string.home_signed_in) }
    val displayEmail = uiState.userEmail.ifBlank { stringResource(R.string.home_no_email_available) }

    ResponsiveSettingsScaffold {
        ProfileHeader(
            name = displayName,
            email = displayEmail,
            onBack = onBack,
            onEditProfile = { onOpenPage(SettingsPage.PersonalInfo) }
        )

        Column(
            modifier = Modifier.padding(horizontal = it, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            StatsCard(
                groups = 0,
                roommates = uiState.friends.size,
                expenses = uiState.recentExpenses.size,
                onOpenGroups = onOpenGroups,
                onOpenRoommates = onOpenRoommates,
                onOpenExpenses = onOpenExpenses
            )

            SettingsMenuCard(
                rows = listOf(
                    SettingsMenuRowData(
                        iconRes = R.drawable.ic_person_24,
                        titleRes = R.string.settings_personal_information,
                        onClick = { onOpenPage(SettingsPage.PersonalInfo) }
                    ),
                    SettingsMenuRowData(
                        iconRes = R.drawable.ic_notifications_24,
                        titleRes = R.string.home_notifications,
                        onClick = { onOpenPage(SettingsPage.Notifications) }
                    ),
                    SettingsMenuRowData(
                        iconRes = R.drawable.ic_phone_24,
                        titleRes = R.string.settings_payment_detection,
                        onClick = { onOpenPage(SettingsPage.PaymentDetection) }
                    ),
                    SettingsMenuRowData(
                        iconRes = R.drawable.ic_credit_card_24,
                        titleRes = R.string.settings_payment_methods,
                        onClick = { onOpenPage(SettingsPage.PaymentMethods) }
                    ),
                    SettingsMenuRowData(
                        iconRes = R.drawable.ic_lock_24,
                        titleRes = R.string.settings_privacy_security,
                        onClick = { onOpenPage(SettingsPage.PrivacySecurity) }
                    ),
                    SettingsMenuRowData(
                        iconRes = R.drawable.ic_settings_24,
                        titleRes = R.string.settings_help_support,
                        onClick = { onOpenPage(SettingsPage.HelpSupport) }
                    )
                )
            )

            LogoutCard(onLogoutClick = onLogoutClick)

            Text(
                text = stringResource(R.string.settings_app_version),
                color = AppMuted.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    name: String,
    email: String,
    onBack: () -> Unit,
    onEditProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd)))
            .statusBarsPadding()
            .heightIn(min = 248.dp, max = 310.dp)
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp, bottom = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderIcon(
                iconRes = R.drawable.ic_arrow_left_24,
                contentDescription = stringResource(R.string.home_cd_back),
                onClick = onBack
            )

            Text(
                text = stringResource(R.string.settings_profile_title),
                color = AppSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier = Modifier.size(86.dp),
            shape = CircleShape,
            color = AppSurface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = initials(name),
                    color = AppPrimary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = name,
            color = AppSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = email,
            color = AppSurface.copy(alpha = 0.88f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .clickable(onClick = onEditProfile),
            shape = RoundedCornerShape(22.dp),
            color = AppSurface.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, AppSurface.copy(alpha = 0.3f))
        ) {
            Text(
                text = stringResource(R.string.settings_edit_profile),
                color = AppSurface,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 9.dp)
            )
        }
    }
}

@Composable
private fun StatsCard(
    groups: Int,
    roommates: Int,
    expenses: Int,
    onOpenGroups: () -> Unit,
    onOpenRoommates: () -> Unit,
    onOpenExpenses: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row {
            StatItem(
                value = groups.toString(),
                label = stringResource(R.string.settings_groups_count_label),
                onClick = onOpenGroups,
                modifier = Modifier.weight(1f)
            )
            VerticalDivider()
            StatItem(
                value = roommates.toString(),
                label = stringResource(R.string.settings_roommates_count_label),
                onClick = onOpenRoommates,
                modifier = Modifier.weight(1f)
            )
            VerticalDivider()
            StatItem(
                value = expenses.toString(),
                label = stringResource(R.string.settings_expenses_count_label),
                onClick = onOpenExpenses,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = AppInk,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = AppMuted,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .heightIn(min = 68.dp)
            .background(AppBorder)
    )
}

private data class SettingsMenuRowData(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    val onClick: () -> Unit
)

@Composable
private fun SettingsMenuCard(rows: List<SettingsMenuRowData>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column {
            rows.forEachIndexed { index, row ->
                SettingsMenuRow(row = row)
                if (index != rows.lastIndex) {
                    HorizontalDivider(color = AppBorder)
                }
            }
        }
    }
}

@Composable
private fun SettingsMenuRow(row: SettingsMenuRowData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = row.onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(16.dp),
            color = AppAccent
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(row.iconRes),
                    contentDescription = null,
                    tint = AppMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = stringResource(row.titleRes),
            color = AppInk,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            painter = painterResource(R.drawable.ic_chevron_right_24),
            contentDescription = null,
            tint = AppMuted,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun LogoutCard(onLogoutClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = AppUiTokens.ButtonMinHeight)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onLogoutClick),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close_24),
                contentDescription = null,
                tint = AppDanger,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.home_logout),
                color = AppDanger,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PersonalInfoPage(
    uiState: HomeUiState,
    onBack: () -> Unit
) {
    var fullName by rememberSaveable(uiState.userName) { mutableStateOf(uiState.userName) }
    var email by rememberSaveable(uiState.userEmail) { mutableStateOf(uiState.userEmail) }
    var phone by rememberSaveable(uiState.userContact) { mutableStateOf(uiState.userContact) }
    var location by rememberSaveable { mutableStateOf("") }

    DetailPageScaffold(
        title = stringResource(R.string.settings_personal_information),
        onBack = onBack
    ) { horizontalPadding ->
        Column(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AppInitialAvatar(
                name = fullName.ifBlank { stringResource(R.string.home_signed_in) },
                size = 92.dp
            )

            AppFormField(
                label = stringResource(R.string.settings_full_name),
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = stringResource(R.string.auth_name_hint),
                iconRes = R.drawable.ic_person_24
            )

            AppFormField(
                label = stringResource(R.string.settings_email),
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(R.string.auth_email_hint),
                iconRes = R.drawable.ic_mail_24,
                keyboardType = KeyboardType.Email
            )

            AppFormField(
                label = stringResource(R.string.settings_phone),
                value = phone,
                onValueChange = { phone = it },
                placeholder = stringResource(R.string.auth_contact_hint),
                iconRes = R.drawable.ic_phone_24,
                keyboardType = KeyboardType.Phone
            )

            AppFormField(
                label = stringResource(R.string.settings_location),
                value = location,
                onValueChange = { location = it },
                placeholder = stringResource(R.string.settings_location_placeholder),
                iconRes = R.drawable.ic_dashboard_24
            )

            AppGradientButton(
                text = stringResource(R.string.settings_save_changes),
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun NotificationsPage(onBack: () -> Unit) {
    var newExpenses by rememberSaveable { mutableStateOf(true) }
    var paymentRequests by rememberSaveable { mutableStateOf(true) }
    var paymentReceived by rememberSaveable { mutableStateOf(true) }
    var groupInvites by rememberSaveable { mutableStateOf(true) }
    var comments by rememberSaveable { mutableStateOf(false) }
    var weeklySummary by rememberSaveable { mutableStateOf(true) }

    DetailPageScaffold(
        title = stringResource(R.string.home_notifications),
        onBack = onBack
    ) { horizontalPadding ->
        Column(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ToggleCard(
                titleRes = R.string.settings_new_expenses,
                bodyRes = R.string.settings_new_expenses_body,
                checked = newExpenses,
                onToggle = { newExpenses = !newExpenses }
            )
            ToggleCard(
                titleRes = R.string.settings_payment_requests,
                bodyRes = R.string.settings_payment_requests_body,
                checked = paymentRequests,
                onToggle = { paymentRequests = !paymentRequests }
            )
            ToggleCard(
                titleRes = R.string.settings_payment_received,
                bodyRes = R.string.settings_payment_received_body,
                checked = paymentReceived,
                onToggle = { paymentReceived = !paymentReceived }
            )
            ToggleCard(
                titleRes = R.string.settings_group_invites,
                bodyRes = R.string.settings_group_invites_body,
                checked = groupInvites,
                onToggle = { groupInvites = !groupInvites }
            )
            ToggleCard(
                titleRes = R.string.settings_expense_comments,
                bodyRes = R.string.settings_expense_comments_body,
                checked = comments,
                onToggle = { comments = !comments }
            )
            ToggleCard(
                titleRes = R.string.settings_weekly_summary,
                bodyRes = R.string.settings_weekly_summary_body,
                checked = weeklySummary,
                onToggle = { weeklySummary = !weeklySummary }
            )
        }
    }
}

@Composable
private fun PaymentDetectionPage(onBack: () -> Unit) {
    var paymentDetection by rememberSaveable { mutableStateOf(true) }
    var autoSuggestGroup by rememberSaveable { mutableStateOf(true) }

    DetailPageScaffold(
        title = stringResource(R.string.settings_payment_detection),
        onBack = onBack
    ) { horizontalPadding ->
        Column(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GradientInfoCard(
                iconRes = R.drawable.ic_notifications_24,
                title = stringResource(R.string.settings_smart_payment_detection),
                body = stringResource(R.string.settings_smart_payment_detection_body)
            )

            ToggleCard(
                titleRes = R.string.settings_enable_payment_detection,
                bodyRes = R.string.settings_enable_payment_detection_body,
                checked = paymentDetection,
                onToggle = { paymentDetection = !paymentDetection }
            )
            ToggleCard(
                titleRes = R.string.settings_auto_suggest_group,
                bodyRes = R.string.settings_auto_suggest_group_body,
                checked = autoSuggestGroup,
                onToggle = { autoSuggestGroup = !autoSuggestGroup }
            )

            PermissionNoteCard()
        }
    }
}

@Composable
private fun PaymentMethodsPage(onBack: () -> Unit) {
    DetailPageScaffold(
        title = stringResource(R.string.settings_payment_methods),
        onBack = onBack
    ) { horizontalPadding ->
        Column(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AddPaymentMethodCard()
            PaymentMethodCard(
                name = stringResource(R.string.settings_visa),
                digits = stringResource(R.string.settings_visa_digits),
                isDefault = true
            )
            PaymentMethodCard(
                name = stringResource(R.string.settings_mastercard),
                digits = stringResource(R.string.settings_mastercard_digits),
                isDefault = false
            )
        }
    }
}

@Composable
private fun PrivacySecurityPage(onBack: () -> Unit) {
    var biometricLock by rememberSaveable { mutableStateOf(true) }
    var showBalance by rememberSaveable { mutableStateOf(true) }

    DetailPageScaffold(
        title = stringResource(R.string.settings_privacy_security),
        onBack = onBack
    ) { horizontalPadding ->
        Column(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SimpleSettingsCard(
                iconRes = R.drawable.ic_lock_24,
                title = stringResource(R.string.settings_change_password)
            )
            ToggleSettingsCard(
                iconRes = R.drawable.ic_lock_24,
                title = stringResource(R.string.settings_biometric_lock),
                body = stringResource(R.string.settings_biometric_lock_body),
                checked = biometricLock,
                onToggle = { biometricLock = !biometricLock }
            )
            ToggleSettingsCard(
                iconRes = R.drawable.ic_dashboard_24,
                title = stringResource(R.string.settings_show_balance_home),
                body = stringResource(R.string.settings_show_balance_home_body),
                checked = showBalance,
                onToggle = { showBalance = !showBalance }
            )
            SimpleSettingsCard(
                iconRes = R.drawable.ic_lock_24,
                title = stringResource(R.string.settings_two_factor)
            )
            GradientInfoCard(
                iconRes = R.drawable.ic_lock_24,
                title = stringResource(R.string.settings_data_safe_title),
                body = stringResource(R.string.settings_data_safe_body)
            )
        }
    }
}

@Composable
private fun HelpSupportPage(onBack: () -> Unit) {
    DetailPageScaffold(
        title = stringResource(R.string.settings_help_support),
        onBack = onBack
    ) { horizontalPadding ->
        Column(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SupportCard(
                iconRes = R.drawable.ic_activity_24,
                title = stringResource(R.string.settings_live_chat),
                body = stringResource(R.string.settings_live_chat_body)
            )
            SupportCard(
                iconRes = R.drawable.ic_mail_24,
                title = stringResource(R.string.settings_email_support),
                body = stringResource(R.string.settings_support_email)
            )
            SupportCard(
                iconRes = R.drawable.ic_phone_24,
                title = stringResource(R.string.settings_phone_support),
                body = stringResource(R.string.settings_support_phone)
            )
            SupportCard(
                iconRes = R.drawable.ic_receipt_24,
                title = stringResource(R.string.home_help_center),
                body = stringResource(R.string.settings_help_center_body)
            )

            Text(
                text = stringResource(R.string.settings_faq_title),
                color = AppInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )

            FaqCard(
                question = stringResource(R.string.settings_faq_split_question),
                answer = stringResource(R.string.settings_faq_split_answer)
            )
            FaqCard(
                question = stringResource(R.string.settings_faq_settle_question),
                answer = stringResource(R.string.settings_faq_settle_answer)
            )
            FaqCard(
                question = stringResource(R.string.settings_faq_edit_question),
                answer = stringResource(R.string.settings_faq_edit_answer)
            )
        }
    }
}

@Composable
private fun ResponsiveSettingsScaffold(
    content: @Composable (horizontalPadding: androidx.compose.ui.unit.Dp) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            content(horizontalPadding)
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun DetailPageScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (horizontalPadding: androidx.compose.ui.unit.Dp) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            AppScreenTopBar(
                title = title,
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
            ) {
                content(horizontalPadding)
            }
        }
    }
}

@Composable
private fun ToggleCard(
    @StringRes titleRes: Int,
    @StringRes bodyRes: Int,
    checked: Boolean,
    onToggle: () -> Unit
) {
    ToggleSettingsCard(
        iconRes = null,
        title = stringResource(titleRes),
        body = stringResource(bodyRes),
        checked = checked,
        onToggle = onToggle
    )
}

@Composable
private fun ToggleSettingsCard(
    @DrawableRes iconRes: Int?,
    title: String,
    body: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconRes != null) {
                IconBadge(iconRes = iconRes)
                Spacer(modifier = Modifier.width(14.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                   color = AppInk,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = body,
                    color = AppMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            AppSwitch(checked = checked, onClick = onToggle)
        }
    }
}

@Composable
private fun SimpleSettingsCard(
    @DrawableRes iconRes: Int,
    title: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBadge(iconRes = iconRes)
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                color = AppInk,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GradientInfoCard(
    @DrawableRes iconRes: Int,
    title: String,
    body: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = AppSurface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(AppGradientStart, AppGradientEnd)))
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(62.dp),
                shape = RoundedCornerShape(20.dp),
                color = AppSurface.copy(alpha = 0.18f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = AppSurface,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                color = AppSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = body,
                color = AppSurface.copy(alpha = 0.88f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PermissionNoteCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppWarning.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, AppWarning.copy(alpha = 0.24f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.settings_permission_note),
                color = AppInk,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.settings_grant_permission),
                color = AppWarning,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AddPaymentMethodCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = AppUiTokens.ButtonMinHeight),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppPrimary)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_24),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.settings_add_payment_method),
                color = AppPrimary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PaymentMethodCard(
    name: String,
    digits: String,
    isDefault: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppGradientIconTile(
                    iconRes = R.drawable.ic_credit_card_24,
                    size = 52.dp,
                    iconSize = 26.dp,
                    rounded = 16.dp
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        color = AppInk,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = digits,
                        color = AppMuted,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (isDefault) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = AppPrimary
                    ) {
                        Text(
                            text = stringResource(R.string.settings_default),
                            color = AppSurface,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PaymentCardButton(
                    text = stringResource(R.string.settings_edit),
                    color = AppMuted,
                    background = AppAccent,
                    modifier = Modifier.weight(1f)
                )
                PaymentCardButton(
                    text = stringResource(R.string.settings_remove),
                    color = AppDanger,
                    background = AppDanger.copy(alpha = 0.08f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PaymentCardButton(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    background: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(14.dp),
        color = background
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = color,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SupportCard(
    @DrawableRes iconRes: Int,
    title: String,
    body: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppGradientIconTile(
                iconRes = iconRes,
                size = 52.dp,
                iconSize = 26.dp,
                rounded = 16.dp
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    color = AppInk,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = body,
                    color = AppMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FaqCard(
    question: String,
    answer: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(24.dp),
                shape = CircleShape,
                color = AppSurface,
                border = BorderStroke(2.dp, AppPrimary)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.settings_faq_icon),
                        color = AppPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = question,
                    color = AppInk,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = answer,
                    color = AppMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun IconBadge(@DrawableRes iconRes: Int) {
    Surface(
        modifier = Modifier.size(44.dp),
        shape = RoundedCornerShape(16.dp),
        color = AppAccent
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = AppMuted,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun HeaderIcon(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
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
            modifier = Modifier.size(22.dp)
        )
    }
}

private fun initials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> ""
    }
}
