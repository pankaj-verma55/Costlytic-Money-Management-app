package com.example.costlyticmoneymanagement.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.costlyticmoneymanagement.model.AuthRepository
import com.example.costlyticmoneymanagement.model.sealed.AuthResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var generalError by mutableStateOf<String?>(null)
        private set
    var phoneError by mutableStateOf<String?>(null)
        private set

    fun clearEmailError() {
        emailError = null
    }

    fun clearPasswordError() {
        passwordError = null
    }

    fun clearNameError() { nameError = null }
    fun clearPhoneError() { phoneError = null }
    fun clearGeneralError() { generalError = null }

    private fun isValidEmail(target: String): Boolean {
        return target.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun login(
        rawEmail: String,
        password: String,
        onLoginSuccess: () -> Unit
    ) {
        emailError = null
        passwordError = null
        generalError = null

        val trimmedEmail = rawEmail.trim()

        if (trimmedEmail.isEmpty()) {
            emailError = "Email is required"
            return
        }
        if (!isValidEmail(trimmedEmail)) {
            emailError = "Invalid email address"
            return
        }
        if (password.isEmpty()) {
            passwordError = "Password is required"
            return
        }
        if (password.length !in 8..16) {
            passwordError = "Password must be at least 8 to 16 characters long"
            return
        }

        isLoading = true

        // 15-second timeout guard for slow/IPv6 Wi-Fi networks
        val timeoutJob = viewModelScope.launch {
            delay(15_000L)
            if (isLoading) {
                isLoading = false
                passwordError = "Network timeout. Check your connection and try again."
            }
        }

        repository.loginUser(trimmedEmail, password) { result ->
            timeoutJob.cancel()
            isLoading = false
            when (result) {
                is AuthResult.Success -> onLoginSuccess()
                is AuthResult.EmailError -> emailError = result.message
                is AuthResult.PasswordError -> passwordError = result.message
                is AuthResult.GeneralError -> passwordError = result.message
                else -> {

                }
            }
        }
    }

    fun signUp(
        rawName: String,
        rawEmail: String,
        rawPhone: String,
        password: String,
        onSignUpSuccess: (otp: String) -> Unit
    ) {
        nameError = null
        emailError = null
        phoneError = null
        passwordError = null
        generalError = null

        val trimmedName = rawName.trim()
        val trimmedEmail = rawEmail.trim()
        val trimmedPhone = rawPhone.trim()

        when {
            trimmedName.isEmpty() -> {
                nameError = "Name is required"
                return
            }
            trimmedEmail.isEmpty() -> {
                emailError = "Email is required"
                return
            }
            !isValidEmail(trimmedEmail) -> {
                emailError = "Invalid email address"
                return
            }
            trimmedPhone.isEmpty() -> {
                phoneError = "Phone number is required"
                return
            }
            trimmedPhone.length != 10 -> {
                phoneError = "Phone number must be 10 digits"
                return
            }
            password.isEmpty() -> {
                passwordError = "Password is required"
                return
            }
            password.length !in 8..16 -> {
                passwordError = "Password must be at least 8 to 16 characters long"
                return
            }
        }

        isLoading = true
        val timeoutJob = viewModelScope.launch {
            delay(15_000L)
            if (isLoading) {
                isLoading = false
                generalError = "Network timeout. Check your connection and try again."
            }
        }

        repository.signUpUser(trimmedName, trimmedEmail, trimmedPhone, password) { result ->
            timeoutJob.cancel()
            isLoading = false
            when (result) {
                is AuthResult.Success -> {
                    val otp = (100000..999999).random().toString()
                    onSignUpSuccess(otp)
                }
                is AuthResult.EmailError -> emailError = result.message
                is AuthResult.PasswordError -> passwordError = result.message
                is AuthResult.GeneralError -> generalError = result.message
                else -> {}
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}