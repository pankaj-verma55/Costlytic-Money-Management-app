package com.example.costlyticmoneymanagement.view.screen


import androidx.compose.runtime.Composable
@Composable
fun ProfileDetailItemScreen(
    key: String,
    value: String,
    onBackClick: () -> Unit,
    onNavigateToDetailAccount: () -> Unit = {}
) {
    when {
        key == "account" || value == "account" -> {
            AccountInfoScreen(
                onBackClick = onBackClick,
                onAddWalletClick = onNavigateToDetailAccount
            )
        }
        key == "setting" || value == "setting" -> {
            SettingScreen(
                onBackClick = onBackClick
            )
        }
    }
}
