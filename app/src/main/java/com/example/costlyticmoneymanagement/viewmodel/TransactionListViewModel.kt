package com.example.costlyticmoneymanagement.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.costlyticmoneymanagement.model.data.TransactionListItem
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

data class TransactionScreenUiState(
    val isLoading: Boolean = true,
    val items: List<TransactionListItem> = emptyList()
)

class TransactionListViewModel(
    private val repository: DashboardRepository = DashboardRepository()
) : ViewModel() {

    private val _selectedMonth = MutableStateFlow("Month")
    var selectedMonth by mutableStateOf("Month")
        private set

    private val _appliedFilterBy = MutableStateFlow<String?>(null)
    private val _appliedSortBy = MutableStateFlow<String?>(null)

    // Temporary bottom-sheet selections before "Apply" is tapped
    var tempFilterBy by mutableStateOf<String?>(null)
        private set

    var tempSortBy by mutableStateOf<String?>(null)
        private set

    fun selectMonth(month: String) {
        selectedMonth = month
        _selectedMonth.value = month
    }

    fun selectTempFilterBy(filter: String?) {
        tempFilterBy = filter
    }

    fun selectTempSortBy(sort: String?) {
        tempSortBy = sort
    }

    fun resetFilters() {
        tempFilterBy = null
        tempSortBy = null
        _appliedFilterBy.value = null
        _appliedSortBy.value = null
    }

    fun applyFilters() {
        _appliedFilterBy.value = tempFilterBy
        _appliedSortBy.value = tempSortBy
    }

    val uiState: StateFlow<TransactionScreenUiState> = combine(
        repository.observeTransactions("table_Income"),
        repository.observeTransactions("table_Expense"),
        _selectedMonth,
        _appliedFilterBy,
        _appliedSortBy
    ) { incomes, expenses, month, filterBy, sortBy ->

        val allRecords = incomes.map { it.copy(type = "Income") } +
                expenses.map { it.copy(type = "Expense") }

        val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)

        // 1. Apply Month & Type ("Income" / "Expense") filters
        val filtered = allRecords.filter { record ->
            val matchesMonth = if (month == "Month" || month == "Yearly") {
                true
            } else if (record.timestamp > 0L) {
                monthFormat.format(Date(record.timestamp)).equals(month, ignoreCase = true)
            } else {
                true
            }

            val matchesType = when {
                filterBy.isNullOrBlank() || filterBy.equals("Month", ignoreCase = true) -> true
                filterBy.equals("Income", ignoreCase = true) -> record.type.equals("Income", true)
                filterBy.equals("Expense", ignoreCase = true) -> record.type.equals("Expense", true)
                filterBy.equals("Transfer", ignoreCase = true) -> record.type.equals("Transfer", true)
                else -> true
            }

            matchesMonth && matchesType
        }

        // 2. Apply Sort ("Highest", "Lowest", "Newest")
        val sorted = when (sortBy) {
            "Highest" -> filtered.sortedByDescending { it.numericAmount() }
            "Lowest" -> filtered.sortedBy { it.numericAmount() }
            else -> filtered.sortedByDescending { it.timestamp } // Default & "Newest"
        }

        // 3. Group into Header + Transaction items (when sorting by Highest/Lowest, show flat list under a summary header)
        val groupedItems = if (sortBy == "Highest" || sortBy == "Lowest") {
            if (sorted.isEmpty()) emptyList()
            else listOf(TransactionListItem.Header("$sortBy Transactions")) + sorted.map { it.toUiItem() }
        } else {
            groupRecordsByDateHeader(sorted)
        }

        TransactionScreenUiState(
            isLoading = false,
            items = groupedItems
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TransactionScreenUiState()
    )

    private fun groupRecordsByDateHeader(records: List<TransactionRecord>): List<TransactionListItem> {
        if (records.isEmpty()) return emptyList()

        val result = mutableListOf<TransactionListItem>()
        val todayCal = Calendar.getInstance()
        val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val headerDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

        var lastHeader: String? = null

        records.forEach { record ->
            val headerTitle = if (record.timestamp <= 0L) {
                "Recent"
            } else {
                val cal = Calendar.getInstance().apply { timeInMillis = record.timestamp }
                when {
                    cal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR) &&
                            cal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR) -> "Today"

                    cal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
                            cal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR) -> "Yesterday"

                    else -> headerDateFormat.format(Date(record.timestamp))
                }
            }

            if (headerTitle != lastHeader) {
                result.add(TransactionListItem.Header(headerTitle))
                lastHeader = headerTitle
            }
            result.add(record.toUiItem())
        }

        return result
    }

    private fun TransactionRecord.toUiItem(): TransactionListItem.Transaction {
        val isIncome = type.equals("Income", ignoreCase = true)
        val prefix = if (isIncome) "+₹" else "-₹"
        val formattedTime = if (timestamp > 0L) {
            SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(timestamp))
        } else {
            "Just now"
        }

        return TransactionListItem.Transaction(
            image = "", // Handled cleanly via local category/type icon fallback
            title = category.ifBlank { type },
            subtitle = description.ifBlank { wallet.ifBlank { type } },
            price = "$prefix${numericAmount().toInt()}",
            time = formattedTime
        )
    }
}