package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.costlyticmoneymanagement.R


/**
 * Replaces IncomeFragment.
 */
@Composable
fun IncomeScreen(
    onBackToDashboard: () -> Unit,
    onContinueClick: (amount: String, category: String, description: String, wallet: String, isRepeat: Boolean) -> Unit = { _, _, _, _, _ -> }
) {
    MoneyTransactionContent(
        title = "Income",
        backgroundColor = colorResource(id = R.color.green),
        onBackClick = onBackToDashboard,
        onContinueClick = onContinueClick
    )
}