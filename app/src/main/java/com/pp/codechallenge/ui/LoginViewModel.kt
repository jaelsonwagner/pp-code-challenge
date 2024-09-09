package com.pp.codechallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    internal val uiState: StateFlow<UiState>
        get() = _uiState

    private var job: Job = Job()

    fun authenticate(userName: String, password: String) {
        if (job.isActive) {
            job.cancel()
        }

        job = viewModelScope.launch {
            _uiState.update { UiState(isLoading = true, success = false, error = null) }

            delay(3000L) // mimic a login request

            if (userName.isBlank() || password.isBlank() || userName != "paypal" || password != "test") {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = false,
                        error = "Something went wrong or wrong credentials!"
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, success = true, error = null) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return LoginViewModel() as T
            }
        }
    }

}
