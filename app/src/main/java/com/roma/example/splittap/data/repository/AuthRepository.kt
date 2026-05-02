package com.roma.example.splittap.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.roma.example.splittap.data.model.UserProfile
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val userRepository: UserRepository = UserRepository()
) {
    fun getCurrentUser() = auth.currentUser

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(
        name: String,
        contact: String,
        email: String,
        password: String
    ) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: return
        val now = System.currentTimeMillis()

        userRepository.saveUserProfile(
            UserProfile(
                uid = user.uid,
                name = name,
                contact = contact,
                email = email,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun logout() {
        auth.signOut()
    }
}
