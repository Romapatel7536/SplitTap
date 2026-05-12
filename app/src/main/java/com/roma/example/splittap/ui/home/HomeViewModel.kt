package com.roma.example.splittap.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.roma.example.splittap.R
import com.roma.example.splittap.data.model.Expense
import com.roma.example.splittap.data.model.UserProfile
import com.roma.example.splittap.data.repository.HomeRepository
import com.roma.example.splittap.ui.expense.SplitMemberUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val roommateBalances: List<RoommateBalanceUi> = emptyList(),
    val friends: List<SplitMemberUi> = emptyList(),
    @StringRes val errorMessageRes: Int? = null
)

data class RoommateBalanceUi(
    val uid: String = "",
    val name: String = "",
    val amount: Double = 0.0
)

class HomeViewModel(
    private val repository: HomeRepository = HomeRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var homeDataJob: Job? = null

    fun loadHomeData() {
        val user = auth.currentUser

        if (user == null) {
            _uiState.value = HomeUiState(
                isLoading = false,
                errorMessageRes = R.string.home_error_not_logged_in
            )
            return
        }

        homeDataJob?.cancel()
        homeDataJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessageRes = null
            )

            try {
                val userId = user.uid
                val email = user.email.orEmpty()
                val profileRequest = async { repository.getUserProfile(userId) }
                val friendsRequest = async { repository.getFriends(userId) }
                val profile = profileRequest.await()
                val friends = friendsRequest.await()

                repository.observeUserExpenses(userId).collect { expenses ->
                    _uiState.value = buildHomeState(
                        userId = userId,
                        email = email,
                        profile = profile,
                        friends = friends,
                        expenses = expenses
                    )
                }
            } catch (e: CancellationException) {
                throw e
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
        email: String,
        profile: UserProfile?,
        friends: List<SplitMemberUi>,
        expenses: List<Expense>
    ): HomeUiState {
        var youAreOwed = 0.0
        var youOwe = 0.0

        val roommateBalanceMap = mutableMapOf<String, Double>()

        expenses.forEach { expense ->
            val totalPeople = expense.splitWithIds.size + 1
            if (totalPeople <= 0) return@forEach

            val perPerson = expense.amount / totalPeople

            if (expense.paidById == userId) {
                youAreOwed += perPerson * expense.splitWithIds.size

                expense.splitWithIds.forEach { roommateId ->
                    roommateBalanceMap[roommateId] =
                        (roommateBalanceMap[roommateId] ?: 0.0) + perPerson
                }
            } else if (expense.splitWithIds.contains(userId)) {
                youOwe += perPerson

                roommateBalanceMap[expense.paidById] =
                    (roommateBalanceMap[expense.paidById] ?: 0.0) - perPerson
            }
        }

        val roommateIds = roommateBalanceMap.keys.toList()
        val roommateProfiles = repository.getUsersByIds(roommateIds)

        val roommateBalances = roommateBalanceMap.map { (uid, amount) ->
            val roommateProfile = roommateProfiles.firstOrNull { it.uid == uid }
            val friendProfile = friends.firstOrNull { it.uid == uid }

            RoommateBalanceUi(
                uid = uid,
                name = roommateProfile?.name
                    ?.ifBlank { roommateProfile.email }
                    ?: friendProfile?.name.orEmpty(),
                amount = amount
            )
        }.sortedByDescending { kotlin.math.abs(it.amount) }

        return HomeUiState(
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
            roommateBalances = roommateBalances,
            friends = friends,
            errorMessageRes = null
        )
    }

    override fun onCleared() {
        homeDataJob?.cancel()
        super.onCleared()
    }
}
