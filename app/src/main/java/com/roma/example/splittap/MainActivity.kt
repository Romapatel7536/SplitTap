package com.roma.example.splittap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roma.example.splittap.ui.auth.AuthScreen
import com.roma.example.splittap.ui.auth.AuthViewModel
import com.roma.example.splittap.ui.home.HomeScreen
import com.roma.example.splittap.ui.home.HomeViewModel
import com.roma.example.splittap.ui.onboarding.GetStartedScreen
import com.roma.example.splittap.ui.onboarding.OnboardingDestination
import com.roma.example.splittap.ui.onboarding.OnboardingScreen
import com.roma.example.splittap.ui.onboarding.OnboardingViewModel
import com.roma.example.splittap.ui.theme.SplitTapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SplitTapTheme {
                val authViewModel: AuthViewModel = viewModel()
                val authUiState by authViewModel.uiState.collectAsState()
                var onboardingDestinationName by rememberSaveable {
                    mutableStateOf(OnboardingDestination.GetStarted.name)
                }

                if (authUiState.isLoggedIn) {
                    val homeViewModel: HomeViewModel = viewModel()
                    val homeUiState by homeViewModel.uiState.collectAsState()

                    LaunchedEffect(Unit) {
                        homeViewModel.loadHomeData()
                    }

                    HomeScreen(
                        uiState = homeUiState,
                        onLogoutClick = {
                            authViewModel.logout()
                            onboardingDestinationName = OnboardingDestination.GetStarted.name
                        },
                        onRefreshHome = homeViewModel::loadHomeData
                    )
                } else {
                    val onboardingDestination = OnboardingDestination.valueOf(onboardingDestinationName)

                    when (onboardingDestination) {
                        OnboardingDestination.GetStarted -> {
                            val onboardingViewModel: OnboardingViewModel = viewModel()

                            GetStartedScreen(
                                onGetStartedClick = {
                                    onboardingViewModel.reset()
                                    onboardingDestinationName = OnboardingDestination.Onboarding.name
                                },
                                onLoginClick = {
                                    authViewModel.showLogin()
                                    onboardingDestinationName = OnboardingDestination.Auth.name
                                }
                            )
                        }

                        OnboardingDestination.Onboarding -> {
                            val onboardingViewModel: OnboardingViewModel = viewModel()
                            val onboardingUiState by onboardingViewModel.uiState.collectAsState()

                            OnboardingScreen(
                                uiState = onboardingUiState,
                                onSkipClick = {
                                    authViewModel.showRegister()
                                    onboardingDestinationName = OnboardingDestination.Auth.name
                                },
                                onNextClick = {
                                    val isComplete = onboardingViewModel.showNextPage()
                                    if (isComplete) {
                                        authViewModel.showRegister()
                                        onboardingDestinationName = OnboardingDestination.Auth.name
                                    }
                                }
                            )
                        }

                        OnboardingDestination.Auth -> AuthScreen(
                            uiState = authUiState,
                            onFullNameChange = authViewModel::updateFullName,
                            onContactChange = authViewModel::updateContact,
                            onEmailChange = authViewModel::updateEmail,
                            onPasswordChange = authViewModel::updatePassword,
                            onConfirmPasswordChange = authViewModel::updateConfirmPassword,
                            onTogglePasswordVisibility = authViewModel::togglePasswordVisibility,
                            onLoginClick = authViewModel::submitLogin,
                            onRegisterClick = authViewModel::submitRegister,
                            onForgotPasswordClick = authViewModel::resetPasswordForCurrentEmail,
                            onShowLogin = authViewModel::showLogin,
                            onShowRegister = authViewModel::showRegister
                        )
                    }
                }
            }
        }
    }
}
