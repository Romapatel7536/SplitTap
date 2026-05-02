package com.roma.example.splittap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roma.example.splittap.ui.auth.LoginScreen
import com.roma.example.splittap.ui.home.HomeScreen
import com.roma.example.splittap.ui.theme.SplitTapTheme
import com.roma.example.splittap.viewmodel.AuthViewModel
import com.roma.example.splittap.viewmodel.HomeViewModel

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
                    LoginScreen(
                        uiState = authUiState,
                        onLoginClick = authViewModel::login,
                        onRegisterClick = authViewModel::register,
                        onForgotPasswordClick = authViewModel::resetPassword
                    )
                }
            }
        }
    }
}
