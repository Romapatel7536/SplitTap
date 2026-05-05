package com.roma.example.splittap.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.roma.example.splittap.data.model.Expense
import com.roma.example.splittap.data.model.UserProfile
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class HomeRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val userRepository: UserRepository = UserRepository()
) {
    suspend fun getUserProfile(userId: String) = userRepository.getUserProfile(userId)

    suspend fun getUserExpenses(userId: String): List<Expense> = coroutineScope {
        val paidByMe = async {
            db.collection(FirestoreCollections.EXPENSES)
                .whereEqualTo(FirestoreFields.PAID_BY_ID, userId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.toObject(Expense::class.java)?.copy(id = document.id)
                }
        }

        val splitWithMe = async {
            db.collection(FirestoreCollections.EXPENSES)
                .whereArrayContains(FirestoreFields.SPLIT_WITH_IDS, userId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.toObject(Expense::class.java)?.copy(id = document.id)
                }
        }

        (paidByMe.await() + splitWithMe.await()).distinctBy { it.id }
    }

    suspend fun getUsersByIds(userIds: List<String>): List<UserProfile> {
        if (userIds.isEmpty()) return emptyList()

        return db.collection(FirestoreCollections.USERS)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(UserProfile::class.java) }
            .filter { it.uid in userIds }
    }
    private object FirestoreFields {
        const val PAID_BY_ID = "paidById"
        const val SPLIT_WITH_IDS = "splitWithIds"
    }
}
