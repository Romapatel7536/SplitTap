package com.roma.example.splittap.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roma.example.splittap.R
import com.roma.example.splittap.ui.expense.AddExpenseScreen
import com.roma.example.splittap.ui.expense.SplitMemberUi
import com.roma.example.splittap.ui.group.CreateGroupScreen
import com.roma.example.splittap.viewmodel.AddExpenseViewModel
import com.roma.example.splittap.viewmodel.HomeUiState
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val addExpenseViewModel: AddExpenseViewModel = viewModel()
    val addUiState by addExpenseViewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SplitTapDrawer(
                selectedDestination = selectedDestination,
                uiState = uiState,
                onClose = { scope.launch { drawerState.close() } },
                onDestinationSelected = { destination ->
                    selectedDestination = destination
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(containerColor = AppBackground) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(AppBackground)
            ) {
                when (activeRoute) {
                    HomeRoute.AddExpense -> AddExpenseScreen(
                        uiState = addUiState,
                        members = listOf(
                            SplitMemberUi(
                                uid = 1.toString(),
                                name = stringResource(R.string.add_expense_roommate_a)
                            ),
                            SplitMemberUi(
                                uid = 2.toString(),
                                name = stringResource(R.string.add_expense_roommate_b)
                            ),
                            SplitMemberUi(
                                uid = 3.toString(),
                                name = stringResource(R.string.add_expense_roommate_c)
                            )
                        ),
                        onBack = { activeRoute = HomeRoute.Main },
                        onSaveExpense = { description, amount, category, splitType, selectedRoommateIds ->
                            addExpenseViewModel.saveExpense(
                                description = description,
                                amount = amount,
                                category = category,
                                splitType = splitType,
                                selectedRoommateIds = selectedRoommateIds
                            ) {
                                activeRoute = HomeRoute.Main
                                onRefreshHome()
                            }
                        }
                    )

                    HomeRoute.CreateGroup -> CreateGroupScreen(
                        isSaving = false,
                        onBack = { activeRoute = HomeRoute.Main },
                        onCreateGroup = { _, _, _ -> activeRoute = HomeRoute.Main }
                    )

                    HomeRoute.Main -> {
                        when (selectedDestination) {
                            HomeDestination.Dashboard -> DashboardScreen(
                                uiState = uiState,
                                onOpenDrawer = { scope.launch { drawerState.open() } },
                                onAddExpense = { activeRoute = HomeRoute.AddExpense },
                                onCreateGroup = { activeRoute = HomeRoute.CreateGroup },
                                onAddRoommate = { selectedDestination = HomeDestination.Roommates },
                                onShowPaymentDetection = { showPaymentDetection = true }
                            )

                            HomeDestination.Groups -> EmptyFeatureScreen(
                                destination = HomeDestination.Groups,
                                titleRes = R.string.home_no_groups_yet,
                                bodyRes = R.string.home_no_groups_body,
                                actionRes = R.string.home_create_group,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )

                            HomeDestination.Roommates -> EmptyFeatureScreen(
                                destination = HomeDestination.Roommates,
                                titleRes = R.string.home_no_roommates_yet,
                                bodyRes = R.string.home_no_roommates_feature_body,
                                actionRes = R.string.home_add_roommate,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )

                            HomeDestination.Expenses -> EmptyFeatureScreen(
                                destination = HomeDestination.Expenses,
                                titleRes = R.string.home_no_expenses_yet,
                                bodyRes = R.string.home_no_expenses_body,
                                actionRes = R.string.home_add_expense,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )

                            HomeDestination.Activity -> EmptyFeatureScreen(
                                destination = HomeDestination.Activity,
                                titleRes = R.string.home_no_activity_yet,
                                bodyRes = R.string.home_no_activity_body,
                                actionRes = R.string.home_open_dashboard,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )

                            HomeDestination.Settings -> SettingsScreen(
                                uiState = uiState,
                                onOpenDrawer = { scope.launch { drawerState.open() } },
                                onLogoutClick = onLogoutClick
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
}

private enum class HomeRoute {
    Main, AddExpense, CreateGroup
}
