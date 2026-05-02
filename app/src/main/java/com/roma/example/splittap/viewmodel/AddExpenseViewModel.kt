package com.roma.example.splittap.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.roma.example.splittap.R
import com.roma.example.splittap.data.model.Expense
import com.roma.example.splittap.data.model.ExpenseCategory
import com.roma.example.splittap.data.model.ExpenseSplitType
import com.roma.example.splittap.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AddExpenseUiState(
    val isSaving: Boolean = false,
    @StringRes val errorMessageRes: Int? = null
)

class AddExpenseViewModel(
    private val repository: ExpenseRepository = ExpenseRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState

    fun saveExpense(
        description: String,
        amount: Double?,
        category: ExpenseCategory,
        splitType: ExpenseSplitType,
        selectedRoommateIds: List<String>,
        onSuccess: () -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            _uiState.value = _uiState.value.copy(
                errorMessageRes = R.string.home_error_not_logged_in
            )
            return
        }

        if (description.isBlank() || amount == null || amount <= 0.0 || selectedRoommateIds.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                errorMessageRes = R.string.add_expense_invalid_input
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessageRes = null
            )

            try {
                val expense = Expense(
                    merchant = description.trim(),
                    amount = amount,
                    category = category.name,
                    paidById = user.uid,
                    paidByEmail = user.email.orEmpty(),
                    splitWithIds = selectedRoommateIds,
                    splitType = splitType.name,
                    createdAt = System.currentTimeMillis()
                )

                repository.addExpense(expense)

                _uiState.value = AddExpenseUiState(isSaving = false)
                onSuccess()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessageRes = R.string.add_expense_save_error
                )
            }
        }
    }
}
