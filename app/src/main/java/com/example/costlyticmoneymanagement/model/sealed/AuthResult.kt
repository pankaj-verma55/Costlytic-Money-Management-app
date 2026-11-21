package com.example.costlyticmoneymanagement.model.sealed

sealed class AuthResult {
    data object Success : AuthResult()
    data class EmailError(val message: String) : AuthResult()
    data class PasswordError(val message: String) : AuthResult()
    data class GeneralError(val message: String) : AuthResult()
}