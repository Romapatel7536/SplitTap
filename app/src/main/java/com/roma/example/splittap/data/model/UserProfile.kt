package com.roma.example.splittap.data.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val contact: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
