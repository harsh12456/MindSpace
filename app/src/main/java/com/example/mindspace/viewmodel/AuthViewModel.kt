package com.example.mindspace.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mindspace.model.AuthUiState
import com.example.mindspace.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val success = repository.register(name, email, password)
            _uiState.update { it.copy(loading = false, registerSuccess = success) }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                val success = repository.login(email, password)
                _uiState.update { it.copy(loading = false, loginSuccess = success, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, loginSuccess = false, error = e.message) }
            }
        }
    }

    fun loginWithGoogle(context: Context, navController: NavController) {
        repository.loginWithGoogle(context, navController)
    }
}

