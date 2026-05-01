package com.roma.example.splittap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roma.example.splittap.ui.auth.AuthViewModel
import com.roma.example.splittap.ui.auth.AuthUiState
import com.roma.example.splittap.ui.auth.LoginScreen
import com.roma.example.splittap.ui.theme.SplitTapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
//
//        db.collection("firebase_test")
//            .add(
//                mapOf(
//                    "message" to "Firebase is working",
//                    "time" to System.currentTimeMillis()
//                )
//            )
//            .addOnSuccessListener {
//                android.util.Log.d("FirebaseTest", "Firestore working ✅ Document ID: ${it.id}")
//            }
//            .addOnFailureListener {
//                android.util.Log.e("FirebaseTest", "Firestore failed ❌", it)
//            }

        enableEdgeToEdge()
        setContent {
            SplitTapTheme {
                val authViewModel: AuthViewModel = viewModel()
                val uiState by authViewModel.uiState.collectAsState()

                if (uiState.isLoggedIn) {
                    SignedInScreen(
                        uiState = uiState,
                        onLogoutClick = authViewModel::logout
                    )
                } else {
                    LoginScreen(
                        uiState = uiState,
                        onLoginClick = authViewModel::login,
                        onRegisterClick = authViewModel::register,
                        onForgotPasswordClick = authViewModel::resetPassword
                    )
                }
            }
        }
    }
}

@Composable
fun SignedInScreen(
    uiState: AuthUiState,
    onLogoutClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Signed in",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = uiState.currentUserEmail ?: "No email available",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Log out")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SplitTapTheme {
        Greeting("Android")
    }
}
