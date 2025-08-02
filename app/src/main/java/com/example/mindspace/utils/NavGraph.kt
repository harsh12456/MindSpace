package com.moodboardai.app.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.moodboardai.app.ui.screens.HomeScreen
import com.example.mindspace.ui.screens.LoginScreen
import com.moodboardai.app.ui.screens.Register
import com.example.mindspace.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    googleSignInClient: GoogleSignInClient
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, authViewModel, googleSignInClient)
        }
        composable("register") {
            Register(navController, authViewModel, googleSignInClient)
        }
        composable("home") {
            HomeScreen(navController)
        }
    }
}
