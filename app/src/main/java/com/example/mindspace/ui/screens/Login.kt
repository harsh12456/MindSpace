package com.example.mindspace.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.mindspace.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import com.example.mindspace.R


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val oneTapClient: SignInClient = remember { Identity.getSignInClient(context) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
            val googleIdToken = credentials.googleIdToken
            if (googleIdToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("home") { popUpTo("login") { inclusive = true } }
                        } else {
                            errorMessage = "Google Sign-In failed."
                        }
                    }
            }
        }
    }

    val signInRequest = remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).build()
    }

    // No need to collect uiState if not using it for error
    // val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = loading) {
            CircularProgressIndicator()
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                loading = true
                errorMessage = null
                viewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        // Listen for login result and set errorMessage if failed
        LaunchedEffect(email, password) {
            // You may want to observe a LiveData or StateFlow for login result here
            // For now, just stop loading after some time or on result
            // Set errorMessage = "Login failed" if needed
            loading = false
            // errorMessage = "Login failed" // Set this if login fails
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener { result ->
                        launcher.launch(
                            IntentSenderRequest.Builder(result.pendingIntent).build()
                        )
                    }
                    .addOnFailureListener {
                        errorMessage = "Unable to launch Google Sign-In."
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have an account? Register")
        }
    }
}