package com.example.mindspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindspace.model.AuthUiState
import com.example.mindspace.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "All fields are required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null, registerSuccess = false) }
            try {
                val success = repository.register(name, email, password)
                _uiState.update {
                    it.copy(
                        loading = false,
                        registerSuccess = success,
                        error = if (!success) "Registration failed" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loading = false,
                        registerSuccess = false,
                        error = e.message ?: "Registration failed"
                    )
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "Email and password are required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null, loginSuccess = false) }
            try {
                val success = repository.login(email, password)
                _uiState.update {
                    it.copy(
                        loading = false,
                        loginSuccess = success,
                        error = if (!success) "Invalid credentials" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loading = false,
                        loginSuccess = false,
                        error = e.message ?: "Login failed"
                    )
                }
            }
        }
    }

    fun clearState() {
        _uiState.update { AuthUiState() }
    }

    fun logout() {
        repository.logout()
        clearState()
    }
}