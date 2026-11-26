package com.example.costlyticmoneymanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.costlyticmoneymanagement.model.data.UserProfile
import com.example.costlyticmoneymanagement.model.firebaseRepo.ProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProfileUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile = UserProfile()
)

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = repository.observeUserProfile()
        .map { profile ->
            ProfileUiState(
                isLoading = false,
                userProfile = profile
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileUiState()
        )

    fun logout(onLogoutSuccess: () -> Unit) {
        repository.logout()
        onLogoutSuccess()
    }
}