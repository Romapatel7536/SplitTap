package com.roma.example.splittap.ui.group

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppDropdownList
import com.roma.example.splittap.ui.home.AppDropdownOption
import com.roma.example.splittap.ui.home.AppFormField
import com.roma.example.splittap.ui.home.AppGradientButton
import com.roma.example.splittap.ui.home.AppGradientIconTile
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppScreenTopBar
import com.roma.example.splittap.ui.home.AppSelectionField
import com.roma.example.splittap.ui.home.AppSurface

private data class GroupCategoryOption(
    @StringRes val titleRes: Int,
    val code: GroupCategoryCode
)

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
    var description by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf(GroupCategoryCode.Home.name) }
    var showCategories by rememberSaveable { mutableStateOf(false) }
    val canCreate = groupName.isNotBlank() && !isSaving

    val categories = remember {
        listOf(
            GroupCategoryOption(R.string.create_group_home, GroupCategoryCode.Home),
            GroupCategoryOption(R.string.create_group_trip, GroupCategoryCode.Trip),
            GroupCategoryOption(R.string.create_group_event, GroupCategoryCode.Event),
            GroupCategoryOption(R.string.create_group_office, GroupCategoryCode.Office),
            GroupCategoryOption(R.string.create_group_other, GroupCategoryCode.Other)
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            AppScreenTopBar(
                title = stringResource(R.string.create_group_title),
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                GroupAvatarPicker()

                AppFormField(
                    label = stringResource(R.string.create_group_name),
                    value = groupName,
                    onValueChange = { groupName = it },
                    placeholder = stringResource(R.string.create_group_name_modern_hint),
                    iconRes = R.drawable.ic_groups_24
                )

                GroupCategorySelector(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    showCategories = showCategories,
                    onToggle = { showCategories = !showCategories },
                    onSelected = {
                        selectedCategory = it.code.name
                        showCategories = false
                    }
                )

                AppFormField(
                    label = stringResource(R.string.create_group_description_optional),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.create_group_description_modern_hint),
                    iconRes = R.drawable.ic_receipt_24,
                    keyboardType = KeyboardType.Text,
                    singleLine = false,
                    minHeight = 100.dp
                )

                AppGradientButton(
                    text = if (isSaving) {
                        stringResource(R.string.add_expense_saving)
                    } else {
                        stringResource(R.string.create_group_continue)
                    },
                    onClick = {
                        onCreateGroup(
                            groupName.trim(),
                            description.trim(),
                            selectedCategory
                        )
                    },
                    enabled = canCreate,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private enum class GroupCategoryCode {
    Home, Trip, Event, Office, Other
}

@Composable
private fun GroupAvatarPicker() {
    Box(contentAlignment = Alignment.BottomEnd) {
        AppGradientIconTile(
            iconRes = R.drawable.ic_groups_24,
            size = 82.dp,
            iconSize = 40.dp,
            rounded = 20.dp
        )

        Surface(
            modifier = Modifier.size(34.dp),
            shape = CircleShape,
            color = AppSurface,
            border = BorderStroke(2.dp, AppBackground),
            shadowElevation = 6.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_receipt_24),
                    contentDescription = null,
                    tint = AppMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun GroupCategorySelector(
    categories: List<GroupCategoryOption>,
    selectedCategory: String,
    showCategories: Boolean,
    onToggle: () -> Unit,
    onSelected: (GroupCategoryOption) -> Unit
) {
    val selectedOption = categories.firstOrNull { it.code.name == selectedCategory }
        ?: categories.first()

    Column(modifier = Modifier.fillMaxWidth()) {
        AppSelectionField(
            label = stringResource(R.string.create_group_category_optional),
            title = stringResource(selectedOption.titleRes),
            onClick = onToggle
        )

        if (showCategories) {
            Spacer(modifier = Modifier.height(8.dp))
            AppDropdownList(
                options = categories.map { category ->
                    AppDropdownOption(
                        value = category,
                        label = stringResource(category.titleRes)
                    )
                },
                selectedValue = selectedOption,
                onSelected = onSelected
            )
        }
    }
}
