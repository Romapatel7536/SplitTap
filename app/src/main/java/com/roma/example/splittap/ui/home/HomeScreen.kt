package com.roma.example.splittap.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.roma.example.splittap.R
import com.roma.example.splittap.data.repository.UserRepository
import com.roma.example.splittap.ui.expense.AddExpenseScreen
import com.roma.example.splittap.ui.expense.AddExpenseViewModel
import com.roma.example.splittap.ui.expense.ExpensesScreen
import com.roma.example.splittap.ui.group.CreateGroupScreen
import com.roma.example.splittap.ui.group.GroupsScreen
import com.roma.example.splittap.ui.home.dashboard.DashboardScreen
import com.roma.example.splittap.ui.home.settings.SettingsScreen
import com.roma.example.splittap.ui.payment.PaymentDetectionSheet
import com.roma.example.splittap.ui.roommate.AddRoommateScreen
import com.roma.example.splittap.ui.roommate.RoommatesScreen
import com.roma.example.splittap.ui.settlement.SettlementScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onLogoutClick: () -> Unit,
    onRefreshHome: () -> Unit
) {
    var activeRoute by remember { mutableStateOf(HomeRoute.Main) }
    var selectedDestination by remember { mutableStateOf(HomeDestination.Dashboard) }
    var showPaymentDetection by remember { mutableStateOf(false) }
    var addRoommateEmail by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val addExpenseViewModel: AddExpenseViewModel = viewModel()
    val addUiState by addExpenseViewModel.uiState.collectAsState()
    val userRepository = UserRepository()

    Scaffold(
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppBackground)
        ) {
            when (activeRoute) {
                HomeRoute.AddExpense -> {
                    LaunchedEffect(Unit) {
                        addExpenseViewModel.loadMembers()
                    }

                    AddExpenseScreen(
                        uiState = addUiState,
                        members = addUiState.members,
                        onBack = { activeRoute = HomeRoute.Main },
                        onSaveExpense = { description, amount, category, splitType, selectedRoommateIds ->
                            addExpenseViewModel.saveExpense(
                                description = description,
                                amount = amount,
                                category = category,
                                splitType = splitType,
                                selectedRoommateIds = selectedRoommateIds,
                                onSuccess = {
                                    activeRoute = HomeRoute.Main
                                    onRefreshHome()
                                }
                            )
                        }
                    )
                }

                HomeRoute.CreateGroup -> CreateGroupScreen(
                    isSaving = false,
                    onBack = { activeRoute = HomeRoute.Main },
                    onCreateGroup = { _, _, _ -> activeRoute = HomeRoute.Main }
                )

                HomeRoute.SettleUp -> SettlementScreen(
                    uiState = uiState,
                    onBack = { activeRoute = HomeRoute.Main }
                )

                HomeRoute.AddRoommate -> AddRoommateScreen(
                    email = addRoommateEmail,
                    onEmailChange = { addRoommateEmail = it },
                    onBack = { activeRoute = HomeRoute.Main },
                    onSendInvitation = {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                            ?: return@AddRoommateScreen

                        scope.launch {
                            val friend = userRepository.findUserByEmail(addRoommateEmail)

                            if (friend != null) {
                                userRepository.addFriend(
                                    currentUserId = currentUser.uid,
                                    friend = friend
                                )

                                onRefreshHome()
                                addRoommateEmail = ""
                                selectedDestination = HomeDestination.Roommates
                                activeRoute = HomeRoute.Main
                            } else {
                                // later we will show error in UI
                                Log.d("AddRoommate", "User not found")
                                Log.d("AddRoommate", "Entered: $addRoommateEmail")
                                Log.d("AddRoommate", "Found: ${friend?.email}")
                            }
                        }
                    }
                )

                HomeRoute.Main -> {
                    when (selectedDestination) {
                        HomeDestination.Dashboard -> DashboardScreen(
                            uiState = uiState,
                            onOpenProfile = { selectedDestination = HomeDestination.Settings },
                            onAddExpense = { activeRoute = HomeRoute.AddExpense },
                            onCreateGroup = { activeRoute = HomeRoute.CreateGroup },
                            onOpenGroups = { selectedDestination = HomeDestination.Groups },
                            onOpenRoommates = { selectedDestination = HomeDestination.Roommates },
                            onOpenExpenses = { selectedDestination = HomeDestination.Expenses },
                            onSettleUp = { activeRoute = HomeRoute.SettleUp },
                            onShowPaymentDetection = { showPaymentDetection = true }
                        )

                        HomeDestination.Groups -> GroupsScreen(
                            onBack = { selectedDestination = HomeDestination.Dashboard },
                            onCreateGroup = { activeRoute = HomeRoute.CreateGroup }
                        )

                        HomeDestination.Roommates -> RoommatesScreen(
                            roommates = uiState.roommateBalances,
                            friends = uiState.friends,
                            onBack = { selectedDestination = HomeDestination.Dashboard },
                            onAddRoommate = { activeRoute = HomeRoute.AddRoommate }
                        )

                        HomeDestination.Expenses -> ExpensesScreen(
                            expenses = uiState.recentExpenses,
                            userEmail = uiState.userEmail,
                            onBack = { selectedDestination = HomeDestination.Dashboard },
                            onAddExpense = { activeRoute = HomeRoute.AddExpense }
                        )

                        HomeDestination.Activity -> EmptyFeatureScreen(
                            destination = HomeDestination.Activity,
                            titleRes = R.string.home_no_activity_yet,
                            bodyRes = R.string.home_no_activity_body,
                            actionRes = R.string.home_open_dashboard,
                            onBack = { selectedDestination = HomeDestination.Dashboard }
                        )

                        HomeDestination.Settings -> SettingsScreen(
                            uiState = uiState,
                            onBack = { selectedDestination = HomeDestination.Dashboard },
                            onLogoutClick = onLogoutClick,
                            onOpenGroups = { selectedDestination = HomeDestination.Groups },
                            onOpenRoommates = { selectedDestination = HomeDestination.Roommates },
                            onOpenExpenses = { selectedDestination = HomeDestination.Expenses }
                        )
                    }
                }
            }

            if (showPaymentDetection) {
                PaymentDetectionSheet(
                    onAddExpense = {
                        showPaymentDetection = false
                        activeRoute = HomeRoute.AddExpense
                    },
                    onDismiss = { showPaymentDetection = false }
                )
            }
        }
    }
}

private enum class HomeRoute {
    Main, AddExpense, CreateGroup, AddRoommate, SettleUp
}
