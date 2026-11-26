package com.example.costlyticmoneymanagement.model.data

data class TransactionRecord(
    val id: String = "",
    val type: String = "",          // "Income" or "Expense"
    val amount: String = "0",
    val category: String = "",
    val description: String = "",
    val wallet: String = "",
    val isRepeat: Boolean = false,
    val timestamp: Long = 0L
) {
    fun numericAmount(): Double {
        return amount.replace("₹", "").replace(",", "").trim().toDoubleOrNull() ?: 0.0
    }
}
