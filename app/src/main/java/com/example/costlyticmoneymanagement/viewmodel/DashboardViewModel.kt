package com.example.costlyticmoneymanagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.costlyticmoneymanagement.model.data.DashboardUiState
import com.example.costlyticmoneymanagement.model.data.TransactionRecord
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

class DashboardViewModel(
    private val repository: DashboardRepository = DashboardRepository()
) : ViewModel() {

    // Bottom bar tab state (0 = Home, 1 = Transaction, 2 = Add, 3 = Budget, 4 = Profile)
    var selectedBottomTab by mutableIntStateOf(0)
        private set

    // Filter states
    private val _selectedMonth = MutableStateFlow("Month")
    var selectedMonth by mutableStateOf("Month")
        private set

    private val _selectedFilterTab = MutableStateFlow("Today")
    var selectedFilterTab by mutableStateOf("Today")
        private set

    fun selectBottomTab(index: Int) {
        selectedBottomTab = index
    }

    fun selectMonth(month: String) {
        selectedMonth = month
        _selectedMonth.value = month
    }

    fun selectFilterTab(tab: String) {
        selectedFilterTab = tab
        _selectedFilterTab.value = tab
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.observeTransactions("table_Income"),
        repository.observeTransactions("table_Expense"),
        _selectedMonth,
        _selectedFilterTab
    ) { incomes, expenses, month, rangeTab ->

        val filteredIncomes = filterByMonthAndRange(incomes, month, rangeTab)
        val filteredExpenses = filterByMonthAndRange(expenses, month, rangeTab)

        val incomeSum = filteredIncomes.sumOf { it.numericAmount() }
        val expenseSum = filteredExpenses.sumOf { it.numericAmount() }
        val balance = incomeSum - expenseSum

        // Build chart points from filtered expenses (fallback to zeros so LineChartView renders smoothly)
        val expensePoints = filteredExpenses
            .sortedBy { it.timestamp }
            .map { it.numericAmount().toFloat() }
            .let { points ->
                when {
                    points.isEmpty() -> listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
                    points.size == 1 -> listOf(0f, points[0])
                    else -> points
                }
            }

        val combinedHistory = (filteredIncomes.map { it.copy(type = "Income") } +
                filteredExpenses.map { it.copy(type = "Expense") })
            .sortedByDescending { it.timestamp }

        DashboardUiState(
            isLoading = false,
            accountBalance = balance,
            totalIncome = incomeSum,
            totalExpense = expenseSum,
            chartData = expensePoints,
            recentTransactions = combinedHistory
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardUiState()
    )

    private fun filterByMonthAndRange(
        records: List<TransactionRecord>,
        selectedMonth: String,
        rangeTab: String
    ): List<TransactionRecord> {
        val now = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)

        return records.filter { record ->
            if (record.timestamp <= 0L) return@filter true

            val recordCal = Calendar.getInstance().apply { timeInMillis = record.timestamp }

            // 1. Month Pill Filter
            val matchesMonth = if (selectedMonth == "Month" || selectedMonth == "Yearly") {
                true
            } else {
                monthFormat.format(Date(record.timestamp)).equals(selectedMonth, ignoreCase = true)
            }

            // 2. Today / Week / Month / Year Filter
            val matchesRange = when (rangeTab) {
                "Today" -> {
                    now.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR) &&
                            now.get(Calendar.DAY_OF_YEAR) == recordCal.get(Calendar.DAY_OF_YEAR)
                }
                "Week" -> {
                    now.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR) &&
                            now.get(Calendar.WEEK_OF_YEAR) == recordCal.get(Calendar.WEEK_OF_YEAR)
                }
                "Month" -> {
                    now.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR) &&
                            now.get(Calendar.MONTH) == recordCal.get(Calendar.MONTH)
                }
                "Year" -> {
                    now.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR)
                }
                else -> true
            }

            matchesMonth && matchesRange
        }
    }
}