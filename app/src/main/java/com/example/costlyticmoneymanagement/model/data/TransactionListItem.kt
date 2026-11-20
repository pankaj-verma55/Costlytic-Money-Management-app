package com.example.costlyticmoneymanagement.model.data

sealed class TransactionListItem {

    data class Header(val title: String) : TransactionListItem()

    data class Transaction(
        val image: String,
        val title: String,
        val subtitle: String,
        val price: String,
        val time: String
    ) : TransactionListItem()
}