package com.roma.example.splittap.data.model

enum class ExpenseCategory {
    FOOD, BILLS, DINING, TRANSPORT, SHOPPING, ENTERTAINMENT, OTHER
}

enum class ExpenseSplitType {
    EQUAL, CUSTOM
}

data class Expense(
    val id: String = "",
    val merchant: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val paidById: String = "",
    val paidByEmail: String = "",
    val splitWithIds: List<String> = emptyList(),
    val splitType: String = ExpenseSplitType.EQUAL.name,
    val createdAt: Long = System.currentTimeMillis()
)
