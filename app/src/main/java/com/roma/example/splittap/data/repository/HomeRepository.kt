package com.roma.example.splittap.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.roma.example.splittap.data.model.Expense
import com.roma.example.splittap.data.model.UserProfile
import com.roma.example.splittap.ui.expense.SplitMemberUi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HomeRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val userRepository: UserRepository = UserRepository()
) {
    suspend fun getUserProfile(userId: String) = userRepository.getUserProfile(userId)

    fun observeUserExpenses(userId: String): Flow<List<Expense>> = callbackFlow {
        var paidByMeExpenses = emptyList<Expense>()
        var splitWithMeExpenses = emptyList<Expense>()
        var paidByMeLoaded = false
        var splitWithMeLoaded = false

        fun sendCombinedExpenses() {
            if (paidByMeLoaded && splitWithMeLoaded) {
                trySend(
                    (paidByMeExpenses + splitWithMeExpenses)
                        .distinctBy { it.id }
                )
            }
        }

        val paidByMeListener = db.collection(FirestoreCollections.EXPENSES)
            .whereEqualTo(FirestoreFields.PAID_BY_ID, userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                paidByMeExpenses = snapshot
                    ?.documents
                    ?.mapNotNull { document ->
                        document.toObject(Expense::class.java)?.copy(id = document.id)
                    }
                    .orEmpty()
                paidByMeLoaded = true
                sendCombinedExpenses()
            }

        val splitWithMeListener = db.collection(FirestoreCollections.EXPENSES)
            .whereArrayContains(FirestoreFields.SPLIT_WITH_IDS, userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                splitWithMeExpenses = snapshot
                    ?.documents
                    ?.mapNotNull { document ->
                        document.toObject(Expense::class.java)?.copy(id = document.id)
                    }
                    .orEmpty()
                splitWithMeLoaded = true
                sendCombinedExpenses()
            }

        awaitClose {
            paidByMeListener.remove()
            splitWithMeListener.remove()
        }
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
    private object FirestoreFields {
        const val PAID_BY_ID = "paidById"
        const val SPLIT_WITH_IDS = "splitWithIds"
    }
}
