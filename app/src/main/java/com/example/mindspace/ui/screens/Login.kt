//package com.example.mindspace.ui.screens
//
//import android.app.Activity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.IntentSenderRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Error
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.Identity
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.example.mindspace.viewmodel.AuthViewModel
//import com.example.mindspace.R
//
//@Composable
//fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
//    val context = LocalContext.current
//    val auth = FirebaseAuth.getInstance()
//    val uiState by viewModel.uiState.collectAsState()
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var emailError by remember { mutableStateOf(false) }
//    var passwordError by remember { mutableStateOf(false) }
//
//    val signInRequest = BeginSignInRequest.builder()
//        .setGoogleIdTokenRequestOptions(
//            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                .setServerClientId(context.getString(R.string.default_web_client_id))
//                .setFilterByAuthorizedAccounts(false)
//                .build()
//        )
//        .build()
//
//    val oneTapClient: SignInClient = remember { Identity.getSignInClient(context) }
//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
//            val googleIdToken = credentials.googleIdToken
//            if (googleIdToken != null) {
//                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
//                auth.signInWithCredential(firebaseCredential)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            navController.navigate("home") { popUpTo("login") { inclusive = true } }
//                        }
//                    }
//            }
//        }
//    }
//
//    LaunchedEffect(uiState.loginSuccess) {
//        if (uiState.loginSuccess) {
//            navController.navigate("home") { popUpTo("login") { inclusive = true } }
//        }
//    }
//
//    fun validateAndLogin() {
//        emailError = email.isBlank()
//        passwordError = password.isBlank()
//
//        if (!emailError && !passwordError) {
//            viewModel.login(email, password)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Email Field
//        OutlinedTextField(
//            value = email,
//            onValueChange = {
//                email = it
//                emailError = false
//            },
//            label = { Text("Email") },
//            isError = emailError,
//            trailingIcon = {
//                if (emailError) {
//                    Icon(
//                        imageVector = Icons.Default.Error,
//                        contentDescription = "Error",
//                        tint = MaterialTheme.colorScheme.error
//                    )
//                }
//            },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        if (emailError) {
//            Text(
//                text = "Email is required",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Password Field
//        OutlinedTextField(
//            value = password,
//            onValueChange = {
//                password = it
//                passwordError = false
//            },
//            label = { Text("Password") },
//            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//            isError = passwordError,
//            trailingIcon = {
//                Row {
//                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                        Icon(
//                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
//                        )
//                    }
//                    if (passwordError) {
//                        Icon(
//                            imageVector = Icons.Default.Error,
//                            contentDescription = "Error",
//                            tint = MaterialTheme.colorScheme.error
//                        )
//                    }
//                }
//            },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        if (passwordError) {
//            Text(
//                text = "Password is required",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Error message from ViewModel
//        uiState.error?.let {
//            Text(
//                text = it,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//
//        AnimatedVisibility(visible = uiState.loading) {
//            CircularProgressIndicator()
//        }
//
//        Button(
//            onClick = { validateAndLogin() },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !uiState.loading
//        ) {
//            Text("Login")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedButton(
//            onClick = {
//                oneTapClient.beginSignIn(signInRequest)
//                    .addOnSuccessListener { result ->
//                        launcher.launch(
//                            IntentSenderRequest.Builder(result.pendingIntent).build()
//                        )
//                    }
//                    .addOnFailureListener {
//                        // Handle error properly
//                    }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Sign in with Google")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        TextButton(onClick = { navController.navigate("register") }) {
//            Text("Don't have an account? Register")
//        }
//    }
//}