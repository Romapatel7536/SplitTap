package com.roma.example.splittap.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.roma.example.splittap.data.model.UserProfile
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun saveUserProfile(profile: UserProfile) {
        db.collection(FirestoreCollections.USERS)
            .document(profile.uid)
            .set(profile)
            .await()
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return db.collection(FirestoreCollections.USERS)
            .document(uid)
            .get()
            .await()
            .toObject(UserProfile::class.java)
    }
}
