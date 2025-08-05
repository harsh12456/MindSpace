package com.example.mindspace.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindspace.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    // Replace with actual username from your ViewModel or state
    val username = "User"

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome, $username",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}