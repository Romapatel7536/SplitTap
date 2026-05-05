package com.roma.example.splittap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roma.example.splittap.ui.auth.AuthScreen
import com.roma.example.splittap.ui.auth.AuthViewModel
import com.roma.example.splittap.ui.home.HomeScreen
import com.roma.example.splittap.ui.home.HomeViewModel
import com.roma.example.splittap.ui.theme.SplitTapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SplitTapTheme {
                val authViewModel: AuthViewModel = viewModel()
                val authUiState by authViewModel.uiState.collectAsState()

                if (authUiState.isLoggedIn) {
                    val homeViewModel: HomeViewModel = viewModel()
                    val homeUiState by homeViewModel.uiState.collectAsState()

                    LaunchedEffect(Unit) {
                        homeViewModel.loadHomeData()
                    }

                    HomeScreen(
                        uiState = homeUiState,
                        onLogoutClick = authViewModel::logout,
                        onRefreshHome = homeViewModel::loadHomeData
                    )
                } else {
                    AuthScreen(
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
