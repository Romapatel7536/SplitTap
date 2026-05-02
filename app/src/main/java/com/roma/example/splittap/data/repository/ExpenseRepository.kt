package com.roma.example.splittap.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.roma.example.splittap.data.model.Expense
import kotlinx.coroutines.tasks.await

class ExpenseRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun addExpense(expense: Expense) {
        db.collection(FirestoreCollections.EXPENSES)
            .add(expense)
            .await()
    }
}
