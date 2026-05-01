package com.roma.example.splittap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roma.example.splittap.ui.theme.SplitTapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        db.collection("firebase_test")
            .add(
                mapOf(
                    "message" to "Firebase is working",
                    "time" to System.currentTimeMillis()
                )
            )
            .addOnSuccessListener {
                android.util.Log.d("FirebaseTest", "Firestore working ✅ Document ID: ${it.id}")
            }
            .addOnFailureListener {
                android.util.Log.e("FirebaseTest", "Firestore failed ❌", it)
            }

        enableEdgeToEdge()
        setContent {
            SplitTapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
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