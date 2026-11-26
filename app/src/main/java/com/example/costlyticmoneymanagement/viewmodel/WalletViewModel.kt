package com.example.costlyticmoneymanagement.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.costlyticmoneymanagement.model.data.WalletItem
import com.example.costlyticmoneymanagement.model.firebaseRepo.WalletRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AccountUiState(
    val isLoading: Boolean = true,
    val totalBalance: Double = 0.0,
    val wallets: List<WalletItem> = emptyList()
)

class WalletViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    val uiState: StateFlow<AccountUiState> = repository.observeWallets()
        .map { walletList ->
            AccountUiState(
                isLoading = false,
                totalBalance = walletList.sumOf { it.balance },
                wallets = walletList
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AccountUiState()
        )

    // Live bank options from Firebase
    val banks: StateFlow<List<String>> = repository.observeBanks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf(
                "Chase", "PayPal", "Citi", "Bank of America",
                "Jago", "Mandiri", "BCA", "See Other"
            )
        )

    var isSavingWallet by mutableStateOf(false)
        private set

    var walletError by mutableStateOf<String?>(null)
        private set

    fun clearError() {
        walletError = null
    }

    fun createWallet(
        name: String,
        type: String,
        rawBalance: String,
        onSuccess: () -> Unit
    ) {
        walletError = null
        val trimmedName = name.trim()
        val numericBalance = rawBalance.replace("₹", "").replace(",", "").trim().toDoubleOrNull() ?: 0.0

        if (trimmedName.isEmpty()) {
            walletError = "Please enter wallet name"
            return
        }
        if (type.isBlank()) {
            walletError = "Please select account type"
            return
        }

        isSavingWallet = true
        val newWallet = WalletItem(
            name = trimmedName,
            type = type,
            balance = numericBalance
        )

        repository.addWallet(newWallet) { result ->
            isSavingWallet = false
            result.onSuccess {
                Log.d("WalletStatus", "SUCCESS: Wallet saved -> $newWallet")
                onSuccess()
            }
            result.onFailure { error ->
                Log.e("WalletStatus", "FAILED: ${error.message}", error)
                walletError = error.localizedMessage ?: "Failed to add wallet"
            }
        }
    }
}