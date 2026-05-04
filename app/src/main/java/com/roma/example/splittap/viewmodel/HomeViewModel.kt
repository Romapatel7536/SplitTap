package com.roma.example.splittap.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.roma.example.splittap.R
import com.roma.example.splittap.data.model.Expense
import com.roma.example.splittap.data.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userContact: String = "",
    val userEmail: String = "",
    val youAreOwed: Double = 0.0,
    val youOwe: Double = 0.0,
    val totalBalance: Double = 0.0,
    val recentExpenses: List<Expense> = emptyList(),
    @StringRes val errorMessageRes: Int? = null
)

class HomeViewModel(
    private val repository: HomeRepository = HomeRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadHomeData() {
        val user = auth.currentUser

        if (user == null) {
            _uiState.value = HomeUiState(
                isLoading = false,
                errorMessageRes = R.string.home_error_not_logged_in
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessageRes = null
            )

            try {
                _uiState.value = buildHomeState(
                    userId = user.uid,
                    email = user.email.orEmpty()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessageRes = R.string.home_error_load_data
                )
            }
        }
    }

    private suspend fun buildHomeState(
        userId: String,
        email: String
    ): HomeUiState = coroutineScope {
        val profileRequest = async { repository.getUserProfile(userId) }
        val expensesRequest = async { repository.getUserExpenses(userId) }

        val profile = profileRequest.await()
        val expenses = expensesRequest.await()

        var youAreOwed = 0.0
        var youOwe = 0.0

        expenses.forEach { expense ->
            val totalPeople = expense.splitWithIds.size + 1

            if (totalPeople <= 0) return@forEach

            val perPerson = expense.amount / totalPeople

            if (expense.paidById == userId) {
                youAreOwed += perPerson * expense.splitWithIds.size
            } else if (expense.splitWithIds.contains(userId)) {
                youOwe += perPerson
            }
        }

        HomeUiState(
            isLoading = false,
            userName = profile?.name.orEmpty(),
            userContact = profile?.contact.orEmpty(),
            userEmail = profile?.email?.ifBlank { email } ?: email,
            youAreOwed = youAreOwed,
            youOwe = youOwe,
            totalBalance = youAreOwed - youOwe,
            recentExpenses = expenses
                .sortedByDescending { it.createdAt }
                .take(5),
            errorMessageRes = null
        )
    }
}
