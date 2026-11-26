package com.example.costlyticmoneymanagement.model.data

data class DashboardUiState(
    val isLoading: Boolean = true,
    val accountBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val chartData: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f),
    val recentTransactions: List<TransactionRecord> = emptyList()
)