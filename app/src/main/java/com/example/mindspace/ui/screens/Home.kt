package com.example.mindspace.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mindspace.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val username = currentUser?.displayName ?: currentUser?.email ?: "User"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome, $username",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true }
                }
            }
        ) {
            Text("Logout")
        }
    }
}