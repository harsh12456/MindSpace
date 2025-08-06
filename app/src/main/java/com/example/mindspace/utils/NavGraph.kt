package com.example.mindspace.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mindspace.ui.screens.HomeScreen
import com.example.mindspace.ui.screens.AuthScreen
import com.example.mindspace.viewmodel.AuthViewModel
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(  // Using the renamed function
                navController = navController,
                viewModel = authViewModel,
                initialLoginMode = true
            )
        }
        composable("home") {
            HomeScreen(navController, authViewModel)
        }
    }
}

