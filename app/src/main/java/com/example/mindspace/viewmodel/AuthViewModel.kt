package com.moodboardai.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.moodboardai.app.model.AuthUiState
import com.moodboardai.app.repository.AuthRepository
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
            val success = repository.login(email, password)
            _uiState.update { it.copy(loading = false, loginSuccess = success) }
        }
    }

    fun loginWithGoogle(context: Context, navController: NavController) {
        repository.loginWithGoogle(context, navController)
    }
}

