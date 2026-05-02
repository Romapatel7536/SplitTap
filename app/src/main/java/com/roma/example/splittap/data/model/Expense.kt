package com.roma.example.splittap.data.model

enum class ExpenseCategory {
    Food,
    Bills,
    Dining,
    Transport,
    Shopping,
    Entertainment,
    Other
}

enum class ExpenseSplitType {
    Equal,
    Custom
}

data class Expense(
    val id: String = "",
    val merchant: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val paidById: String = "",
    val paidByEmail: String = "",
    val splitWithIds: List<String> = emptyList(),
    val splitType: String = ExpenseSplitType.Equal.name,
    val createdAt: Long = System.currentTimeMillis()
)
