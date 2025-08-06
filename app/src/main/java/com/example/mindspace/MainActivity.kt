package com.example.mindspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.mindspace.ui.theme.MindSpaceTheme
import com.example.mindspace.viewmodel.AuthViewModel
import com.example.mindspace.utils.NavGraph
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MindSpaceTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()

                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}