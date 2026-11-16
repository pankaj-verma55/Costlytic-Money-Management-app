package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.costlyticmoneymanagement.R

/**
 * Replaces ExpenseFragment.
 */
@Composable
fun ExpenseScreen(
    onBackToDashboard: () -> Unit,
    onContinueClick: (amount: String, category: String, description: String, wallet: String, isRepeat: Boolean) -> Unit = { _, _, _, _, _ -> }
) {
    MoneyTransactionContent(
        title = "Expense",
        backgroundColor = colorResource(id = R.color.red),
        onBackClick = onBackToDashboard,
        onContinueClick = onContinueClick
    )
}