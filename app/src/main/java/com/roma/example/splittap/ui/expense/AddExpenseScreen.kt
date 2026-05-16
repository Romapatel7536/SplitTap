package com.roma.example.splittap.ui.expense

import androidx.annotation.StringRes
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.roma.example.splittap.data.model.ExpenseCategory
import com.roma.example.splittap.data.model.ExpenseSplitType
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppCheckIndicator
import com.roma.example.splittap.ui.home.AppDanger
import com.roma.example.splittap.ui.home.AppDropdownList
import com.roma.example.splittap.ui.home.AppDropdownOption
import com.roma.example.splittap.ui.home.AppFieldLabel
import com.roma.example.splittap.ui.home.AppFormField
import com.roma.example.splittap.ui.home.AppGradientButton
import com.roma.example.splittap.ui.home.AppInitialAvatar
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppScreenTopBar
import com.roma.example.splittap.ui.home.AppSelectionField
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.AppUiTokens

private data class ExpenseCategoryOption(
    @StringRes val titleRes: Int,
    val code: ExpenseCategory
)

@Composable
fun AddExpenseScreen(
    uiState: AddExpenseUiState,
    members: List<SplitMemberUi>,
    onBack: () -> Unit,
    onSaveExpense: (
        description: String,
        amount: Double?,
        category: ExpenseCategory,
        splitType: ExpenseSplitType,
        selectedRoommateIds: List<String>
    ) -> Unit
) {
    var description by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var selectedCategoryName by rememberSaveable { mutableStateOf(ExpenseCategory.FOOD.name) }
    var selectedSplitOptionName by rememberSaveable { mutableStateOf(SplitOption.Equal.name) }
    var selectedRoommateIdsText by rememberSaveable { mutableStateOf("") }
    var categoryOptionsVisible by rememberSaveable { mutableStateOf(false) }
    var didAutoSelectMembers by rememberSaveable { mutableStateOf(false) }
    var showSplitOptions by rememberSaveable { mutableStateOf(false) }

    val selectedCategory = ExpenseCategory.valueOf(selectedCategoryName)
    val selectedSplitOption = SplitOption.valueOf(selectedSplitOptionName)
    val splitType = selectedSplitOption.toExpenseSplitType()
    val selectedRoommateIds = selectedRoommateIdsText.toIdList()
    val canSubmit = !uiState.isSaving

    val categories = remember {
        listOf(
            ExpenseCategoryOption(R.string.add_expense_category_food, ExpenseCategory.FOOD),
            ExpenseCategoryOption(R.string.add_expense_category_bills, ExpenseCategory.BILLS),
            ExpenseCategoryOption(R.string.add_expense_category_dining, ExpenseCategory.DINING),
            ExpenseCategoryOption(R.string.add_expense_category_transport, ExpenseCategory.TRANSPORT),
            ExpenseCategoryOption(R.string.add_expense_category_shopping, ExpenseCategory.SHOPPING),
            ExpenseCategoryOption(R.string.add_expense_category_entertainment, ExpenseCategory.ENTERTAINMENT),
            ExpenseCategoryOption(R.string.add_expense_category_other, ExpenseCategory.OTHER)
        )
    }

    LaunchedEffect(members) {
        if (!didAutoSelectMembers && members.isNotEmpty() && selectedRoommateIdsText.isBlank()) {
            selectedRoommateIdsText = members.joinToString(IdSeparator) { it.uid }
            didAutoSelectMembers = true
        }
    }

    fun toggleRoommate(uid: String) {
        selectedRoommateIdsText = selectedRoommateIdsText.toIdList()
            .toggle(uid)
            .joinToString(IdSeparator)
    }

    if (showSplitOptions) {
        SplitOptionsScreen(
            amount = amount.toDoubleOrNull(),
            members = members,
            selectedRoommateIds = selectedRoommateIds,
            selectedOption = selectedSplitOption,
            onOptionSelected = { selectedSplitOptionName = it.name },
            onClose = { showSplitOptions = false },
            onDone = { showSplitOptions = false }
        )
        return
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            AppScreenTopBar(
                title = stringResource(R.string.add_expense_title),
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
                    .padding(horizontal = horizontalPadding, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppFormField(
                    label = stringResource(R.string.add_expense_expense_title),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.add_expense_title_hint),
                    iconRes = R.drawable.ic_receipt_24
                )

                AppFormField(
                    label = stringResource(R.string.add_expense_amount),
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = stringResource(R.string.add_expense_amount_hint),
                    iconRes = R.drawable.ic_dollar_24,
                    keyboardType = KeyboardType.Decimal
                )

                CategorySelector(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    optionsVisible = categoryOptionsVisible,
                    onToggleOptions = { categoryOptionsVisible = !categoryOptionsVisible },
                    onCategorySelected = {
                        selectedCategoryName = it.code.name
                        categoryOptionsVisible = false
                    }
                )

                AppSelectionField(
                    label = stringResource(R.string.add_expense_paid_by),
                    title = stringResource(R.string.add_expense_you),
                    iconRes = R.drawable.ic_person_24
                )

                SplitTypeCard(
                    splitOption = selectedSplitOption,
                    onClick = { showSplitOptions = true }
                )

                ParticipantsSection(
                    members = members,
                    selectedRoommateIds = selectedRoommateIds,
                    onToggleRoommate = ::toggleRoommate
                )

                AppFormField(
                    label = stringResource(R.string.add_expense_notes_optional),
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = stringResource(R.string.add_expense_notes_hint),
                    iconRes = R.drawable.ic_receipt_24,
                    singleLine = false,
                    minHeight = 96.dp
                )

                uiState.errorMessageRes?.let { errorMessageRes ->
                    Text(
                        text = stringResource(errorMessageRes),
                        color = AppDanger,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                AppGradientButton(
                    text = if (uiState.isSaving) {
                        stringResource(R.string.add_expense_saving)
                    } else {
                        stringResource(R.string.add_expense_save)
                    },
                    onClick = {
                        onSaveExpense(
                            description,
                            amount.toDoubleOrNull(),
                            selectedCategory,
                            splitType,
                            selectedRoommateIds
                        )
                    },
                    enabled = canSubmit,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CategorySelector(
    categories: List<ExpenseCategoryOption>,
    selectedCategory: ExpenseCategory,
    optionsVisible: Boolean,
    onToggleOptions: () -> Unit,
    onCategorySelected: (ExpenseCategoryOption) -> Unit
) {
    val selectedOption = categories.first { it.code == selectedCategory }

    Column {
        AppSelectionField(
            label = stringResource(R.string.add_expense_category),
            title = stringResource(selectedOption.titleRes),
            iconRes = R.drawable.ic_receipt_24,
            onClick = onToggleOptions
        )

        if (optionsVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            AppDropdownList(
                options = categories.map {
                    AppDropdownOption(
                        value = it,
                        label = stringResource(it.titleRes)
                    )
                },
                selectedValue = selectedOption,
                onSelected = onCategorySelected
            )
        }
    }
}

@Composable
private fun SplitTypeCard(
    splitOption: SplitOption,
    onClick: () -> Unit
) {
    Column {
        AppFieldLabel(text = stringResource(R.string.add_expense_split_type))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .clip(RoundedCornerShape(AppUiTokens.ControlCorner))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(AppUiTokens.ControlCorner),
            color = AppSurface,
            border = BorderStroke(1.dp, AppBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.add_expense_paid_by_split),
                        color = AppMuted,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (splitOption == SplitOption.Equal) {
                            stringResource(R.string.add_expense_equally)
                        } else {
                            stringResource(splitOption.titleRes)
                        },
                        color = AppInk,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right_24),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(AppUiTokens.FieldIconSize)
                )
            }
        }
    }
}

@Composable
private fun ParticipantsSection(
    members: List<SplitMemberUi>,
    selectedRoommateIds: List<String>,
    onToggleRoommate: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.add_expense_participants),
            color = AppInk,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 7.dp)
        )

        ParticipantRow(
            name = stringResource(R.string.add_expense_you),
            selected = true,
            enabled = false,
            onClick = {}
        )

        if (members.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            EmptyParticipantsCard()
        } else {
            members.forEach { member ->
                Spacer(modifier = Modifier.height(8.dp))
                ParticipantRow(
                    name = member.name.ifBlank { stringResource(R.string.home_unknown_user) },
                    selected = selectedRoommateIds.contains(member.uid),
                    enabled = true,
                    onClick = { onToggleRoommate(member.uid) }
                )
            }
        }
    }
}

@Composable
private fun ParticipantRow(
    name: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clip(RoundedCornerShape(AppUiTokens.ControlCorner))
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(AppUiTokens.ControlCorner),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppInitialAvatar(name = name, size = 42.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                color = AppInk,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            AppCheckIndicator(selected = selected)
        }
    }
}

@Composable
private fun EmptyParticipantsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = stringResource(R.string.add_expense_no_friends_title),
                color = AppInk,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.add_expense_no_friends_body),
                color = AppMuted,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun String.toIdList(): List<String> {
    return if (isBlank()) emptyList() else split(IdSeparator)
}

private fun List<String>.toggle(id: String): List<String> {
    return if (contains(id)) this - id else this + id
}

private const val IdSeparator = ","
