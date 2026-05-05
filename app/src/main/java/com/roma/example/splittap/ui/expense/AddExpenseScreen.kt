package com.roma.example.splittap.ui.expense

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.roma.example.splittap.data.model.ExpenseCategory
import com.roma.example.splittap.data.model.ExpenseSplitType
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppDanger
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppSurface

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
    var selectedCategoryName by rememberSaveable { mutableStateOf(ExpenseCategory.FOOD.name) }
    var splitTypeName by rememberSaveable { mutableStateOf(ExpenseSplitType.EQUAL.name) }
    var selectedRoommateIdsText by rememberSaveable { mutableStateOf("") }
    val selectedCategory = ExpenseCategory.valueOf(selectedCategoryName)
    val splitType = ExpenseSplitType.valueOf(splitTypeName)
    val selectedRoommateIds = selectedRoommateIdsText.toIdList()

    val canSubmit = !uiState.isSaving

    val categories = remember {
        listOf(
            ExpenseCategoryOption(R.string.add_expense_category_food, ExpenseCategory.FOOD),
            ExpenseCategoryOption(R.string.add_expense_category_bills, ExpenseCategory.BILLS),
            ExpenseCategoryOption(R.string.add_expense_category_dining, ExpenseCategory.DINING),
            ExpenseCategoryOption(
                R.string.add_expense_category_transport,
                ExpenseCategory.TRANSPORT
            ),
            ExpenseCategoryOption(
                R.string.add_expense_category_shopping,
                ExpenseCategory.SHOPPING
            ),
            ExpenseCategoryOption(
                R.string.add_expense_category_entertainment,
                ExpenseCategory.ENTERTAINMENT
            ),
            ExpenseCategoryOption(R.string.add_expense_category_other, ExpenseCategory.OTHER)
        )
    }

    fun toggleRoommate(uid: String) {
        val currentIds = selectedRoommateIdsText.toIdList()

        selectedRoommateIdsText = if (currentIds.contains(uid)) {
            (currentIds - uid).joinToString(",")
        } else {
            (currentIds + uid).joinToString(",")
        }
    }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            AddExpenseBottomBar(
                canSubmit = canSubmit,
                isSaving = uiState.isSaving,
                onSave = {
                    onSaveExpense(
                        description,
                        amount.toDoubleOrNull(),
                        selectedCategory,
                        splitType,
                        selectedRoommateIds
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
                    title = stringResource(R.string.add_expense_title),
                    onBack = onBack
                )

                Spacer(modifier = Modifier.height(32.dp))

                FormTextField(
                    label = stringResource(R.string.add_expense_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.add_expense_description_hint)
                )

                Spacer(modifier = Modifier.height(24.dp))

                FormTextField(
                    label = stringResource(R.string.add_expense_amount),
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = stringResource(R.string.add_expense_amount_hint),
                    keyboardType = KeyboardType.Decimal
                )

                Spacer(modifier = Modifier.height(28.dp))

                FormSectionLabel(text = stringResource(R.string.add_expense_category))
                CategoryGrid(
                    categories = categories,
                    selectedCategory = categories.first { it.code == selectedCategory },
                    onCategorySelected = { selectedCategoryName = it.code.name }
                )

                Spacer(modifier = Modifier.height(28.dp))

                FormSectionLabel(text = stringResource(R.string.add_expense_paid_by))
                PaidBySelector()

                Spacer(modifier = Modifier.height(28.dp))

                FormSectionLabel(text = stringResource(R.string.add_expense_split_with))

                members.forEach { member ->
                    SplitWithRow(
                        name = member.name,
                        selected = selectedRoommateIds.contains(member.uid),
                        onClick = { toggleRoommate(member.uid) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Spacer(modifier = Modifier.height(28.dp))

                FormSectionLabel(text = stringResource(R.string.add_expense_split_type))
                SplitTypeSelector(
                    splitType = splitType,
                    onSplitTypeSelected = { splitTypeName = it.name }
                )

                uiState.errorMessageRes?.let { errorMessageRes ->
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = stringResource(errorMessageRes),
                        color = AppDanger,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
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
private fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        FormSectionLabel(text = label)
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
private fun CategoryGrid(
    categories: List<ExpenseCategoryOption>,
    selectedCategory: ExpenseCategoryOption,
    onCategorySelected: (ExpenseCategoryOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        categories.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { category ->
                    CategoryButton(
                        text = stringResource(category.titleRes),
                        selected = selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, if (selected) AppPrimary else AppBorder)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = AppInk,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PaidBySelector() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.add_expense_you),
                color = AppInk,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right_24),
                contentDescription = null,
                tint = AppInk,
                modifier = Modifier
                    .size(22.dp)
                    .rotate(90f)
            )
        }
    }
}

@Composable
private fun SplitWithRow(
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = AppBackground,
        border = BorderStroke(1.dp, if (selected) AppPrimary else AppBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(
                        if (selected) AppPrimary else AppInk.copy(alpha = 0.35f),
                        RoundedCornerShape(3.dp)
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                color = AppInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SplitTypeSelector(
    splitType: ExpenseSplitType,
    onSplitTypeSelected: (ExpenseSplitType) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SplitTypeButton(
            text = stringResource(R.string.add_expense_equal_split),
            selected = splitType == ExpenseSplitType.EQUAL,
            onClick = { onSplitTypeSelected(ExpenseSplitType.EQUAL) },
            modifier = Modifier.weight(1f)
        )
        SplitTypeButton(
            text = stringResource(R.string.add_expense_custom_split),
            selected = splitType == ExpenseSplitType.CUSTOM,
            onClick = { onSplitTypeSelected(ExpenseSplitType.CUSTOM) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SplitTypeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) AppPrimary else AppSurface,
        border = BorderStroke(1.dp, if (selected) AppPrimary else AppBorder)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (selected) AppSurface else AppInk,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AddExpenseBottomBar(
    canSubmit: Boolean,
    isSaving: Boolean,
    onSave: () -> Unit
) {
    Surface(
        color = AppBackground,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
        ) {
            HorizontalDivider(color = AppBorder)
            Button(
                onClick = onSave,
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
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
                    text = if (isSaving) {
                        stringResource(R.string.add_expense_saving)
                    } else {
                        stringResource(R.string.add_expense_save)
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun String.toIdList(): List<String> {
    return if (isBlank()) emptyList() else split(IdSeparator)
}

private fun List<String>.toIdText(): String {
    return joinToString(IdSeparator)
}

private fun List<String>.toggle(id: String): List<String> {
    return if (contains(id)) this - id else this + id
}
private const val IdSeparator = ","
