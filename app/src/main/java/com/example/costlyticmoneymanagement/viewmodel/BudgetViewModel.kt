package com.example.costlyticmoneymanagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.costlyticmoneymanagement.model.data.BudgetRecord
import com.example.costlyticmoneymanagement.model.firebaseRepo.BudgetRepository
import com.example.costlyticmoneymanagement.model.firebaseRepo.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class BudgetUiState(
    val isLoading: Boolean = true,
    val budgets: List<BudgetRecord> = emptyList()
)

class BudgetViewModel(
    private val budgetRepository: BudgetRepository = BudgetRepository(),
    private val dashboardRepository: DashboardRepository = DashboardRepository()
) : ViewModel() {

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    // Defaults to current month (or May = index 4)
    private val initialMonthIndex = Calendar.getInstance().get(Calendar.MONTH)
    var monthIndex by mutableIntStateOf(initialMonthIndex)
        private set

    private val _selectedMonth = MutableStateFlow(months[initialMonthIndex])

    val currentMonthName: String
        get() = months[monthIndex]

    fun previousMonth() {
        monthIndex = if (monthIndex > 0) monthIndex - 1 else months.lastIndex
        _selectedMonth.value = months[monthIndex]
    }

    fun nextMonth() {
        monthIndex = (monthIndex + 1) % months.size
        _selectedMonth.value = months[monthIndex]
    }

    // Live UI State combining Budgets + Expenses for the selected month
    val uiState: StateFlow<BudgetUiState> = combine(
        budgetRepository.observeBudgets(),
        dashboardRepository.observeTransactions("table_Expense"),
        _selectedMonth
    ) { allBudgets, allExpenses, selectedMonth ->

        val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)

        // Filter expenses belonging to the selected month
        val monthExpenses = allExpenses.filter { expense ->
            if (expense.timestamp <= 0L) true
            else monthFormat.format(Date(expense.timestamp)).equals(selectedMonth, ignoreCase = true)
        }

        // Filter budgets for the selected month and compute live spent amounts
        val mappedBudgets = allBudgets
            .filter { it.month.equals(selectedMonth, ignoreCase = true) }
            .map { record ->
                val spentForCategory = monthExpenses
                    .filter { it.category.equals(record.category, ignoreCase = true) }
                    .sumOf { it.numericAmount() }

                BudgetRecord(
                    category = record.category,
                    limitAmount = record.limitAmount,
                    alertPercentage = spentForCategory.toInt()
                )
            }

        BudgetUiState(
            isLoading = false,
            budgets = mappedBudgets
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = BudgetUiState()
    )

    // Create Budget State & Action (for CreateBudgetScreen)
    var isSavingBudget by mutableStateOf(false)
        private set

    var budgetError by mutableStateOf<String?>(null)
        private set

    fun createBudget(
        rawLimitAmount: String,
        category: String,
        receiveAlert: Boolean,
        alertPercentage: Int,
        onSuccess: () -> Unit
    ) {
        budgetError = null
        val numericLimit = rawLimitAmount.replace("₹", "").replace(",", "").trim().toDoubleOrNull() ?: 0.0

        if (numericLimit <= 0.0) {
            budgetError = "Please enter a valid budget amount"
            return
        }
        if (category.isBlank()) {
            budgetError = "Please select a category"
            return
        }

        isSavingBudget = true
        val newRecord = BudgetRecord(
            category = category,
            limitAmount = numericLimit,
            alertPercentage = alertPercentage,
            receiveAlert = receiveAlert,
            month = currentMonthName,
            year = Calendar.getInstance().get(Calendar.YEAR)
        )

        budgetRepository.saveBudget(newRecord) { result ->
            isSavingBudget = false
            result.onSuccess { onSuccess() }
            result.onFailure { budgetError = it.localizedMessage ?: "Failed to create budget" }
        }
    }
}