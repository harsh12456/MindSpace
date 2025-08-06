package com.example.mindspace.model

data class AuthUiState(
    val loading: Boolean = false,
    val registerSuccess: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null
)


// AuthViewModel.kt (viewmodel)
