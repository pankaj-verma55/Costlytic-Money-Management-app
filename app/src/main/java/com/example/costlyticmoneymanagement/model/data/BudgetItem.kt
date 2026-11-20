package com.example.costlyticmoneymanagement.model.data

import androidx.compose.ui.graphics.Color


data class BudgetItem(
    val category: String,
    val categoryColor: Color,
    val remainingAmount: String,
    val spentAmount: String,
    val totalAmount: String,
    val progress: Float,
    val isExceeded: Boolean
)
