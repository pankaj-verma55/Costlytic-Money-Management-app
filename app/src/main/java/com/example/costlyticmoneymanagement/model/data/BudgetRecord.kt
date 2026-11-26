package com.example.costlyticmoneymanagement.model.data


data class BudgetRecord(
    val id: String = "",
    val category: String = "",
    val limitAmount: Double = 0.0,
    val alertPercentage: Int = 80,
    val receiveAlert: Boolean = false,
    val month: String = "",
    val year: Int = 2026
)