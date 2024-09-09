package com.pp.codechallenge.ui

data class UiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)