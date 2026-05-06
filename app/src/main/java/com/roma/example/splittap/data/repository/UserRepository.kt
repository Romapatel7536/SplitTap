package com.roma.example.splittap.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.roma.example.splittap.data.model.UserProfile
import com.roma.example.splittap.ui.expense.SplitMemberUi
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getAllUsersExcept(currentUserId: String): List<UserProfile> {
        return db.collection(FirestoreCollections.USERS)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(UserProfile::class.java) }
            .filter { it.uid != currentUserId }
    }

    suspend fun saveUserProfile(user: UserProfile) {
        db.collection(FirestoreCollections.USERS)
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        return db.collection(FirestoreCollections.USERS)
            .document(userId)
            .get()
            .await()
            .toObject(UserProfile::class.java)
    }

    suspend fun findUserByEmail(email: String): UserProfile? {
        return db.collection(FirestoreCollections.USERS)
            .whereEqualTo("email", email.trim().lowercase())
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.toObject(UserProfile::class.java)
    }

    suspend fun addFriend(
        currentUserId: String,
        friend: UserProfile
    ) {
        val friendData = mapOf(
            "uid" to friend.uid,
            "name" to friend.name,
            "email" to friend.email,
            "contact" to friend.contact,
            "addedAt" to System.currentTimeMillis()
        )

        db.collection(FirestoreCollections.USERS)
            .document(currentUserId)
            .collection(FirestoreCollections.FRIENDS)
            .document(friend.uid)
            .set(friendData)
            .await()
    }

    suspend fun getFriends(currentUserId: String): List<SplitMemberUi> {
        return db.collection(FirestoreCollections.USERS)
            .document(currentUserId)
            .collection(FirestoreCollections.FRIENDS)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                val uid = document.getString("uid").orEmpty()
                val name = document.getString("name").orEmpty()
                val email = document.getString("email").orEmpty()

                if (uid.isBlank()) {
                    null
                } else {
                    SplitMemberUi(
                        uid = uid,
                        name = name.ifBlank { email }
                    )
                }
            }
    }
}