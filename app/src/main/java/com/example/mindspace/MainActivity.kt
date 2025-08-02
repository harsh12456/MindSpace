package com.example.mindspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.example.mindspace.ui.theme.MindSpaceTheme
import com.example.mindspace.viewmodel.AuthViewModel
import com.example.mindspace.utils.NavGraph

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            MindSpaceTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    googleSignInClient = googleSignInClient
                )
            }
        }
    }
}
