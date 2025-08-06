//package com.example.mindspace.ui.screens
//
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.clickable
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
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.example.mindspace.viewmodel.AuthViewModel
//import com.example.mindspace.R
//
//@Composable
//fun Register(
//    navController: NavController,
//    authViewModel: AuthViewModel
//) {
//    val context = LocalContext.current
//    val auth = FirebaseAuth.getInstance()
//    val uiState by authViewModel.uiState.collectAsState()
//
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var nameError by remember { mutableStateOf(false) }
//    var emailError by remember { mutableStateOf(false) }
//    var passwordError by remember { mutableStateOf(false) }
//
//    // Modern Google Sign-In setup
//    val oneTapClient = remember { Identity.getSignInClient(context) }
//    val signUpRequest = remember {
//        BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setServerClientId(context.getString(R.string.default_web_client_id))
//                    .setFilterByAuthorizedAccounts(false)
//                    .build()
//            )
//            .build()
//    }
//
//    val googleLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult()
//    ) { result ->
//        try {
//            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
//            val googleIdToken = credential.googleIdToken
//            if (googleIdToken != null) {
//                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
//                auth.signInWithCredential(firebaseCredential).addOnCompleteListener { authResult ->
//                    if (authResult.isSuccessful) {
//                        Toast.makeText(context, "Google Sign Up Successful!", Toast.LENGTH_SHORT).show()
//                        navController.navigate("login") { popUpTo("register") { inclusive = true } }
//                    } else {
//                        Toast.makeText(context, "Google Sign Up Failed!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        } catch (e: ApiException) {
//            Toast.makeText(context, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Handle registration success
//    LaunchedEffect(uiState.registerSuccess) {
//        if (uiState.registerSuccess) {
//            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
//            navController.navigate("login") {
//                popUpTo("register") { inclusive = true }
//            }
//        }
//    }
//
//    fun validateAndRegister() {
//        nameError = name.isBlank()
//        emailError = email.isBlank()
//        passwordError = password.isBlank()
//
//        if (!nameError && !emailError && !passwordError) {
//            authViewModel.register(name, email, password)
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Column(
//            modifier = Modifier
//                .padding(24.dp)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Create Account", style = MaterialTheme.typography.headlineMedium)
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Name Field
//            OutlinedTextField(
//                value = name,
//                onValueChange = {
//                    name = it
//                    nameError = false
//                },
//                label = { Text("Full Name") },
//                isError = nameError,
//                trailingIcon = {
//                    if (nameError) {
//                        Icon(
//                            imageVector = Icons.Default.Error,
//                            contentDescription = "Error",
//                            tint = MaterialTheme.colorScheme.error
//                        )
//                    }
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedTextColor = Color.Black,
//                    unfocusedTextColor = Color.Black
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//            if (nameError) {
//                Text(
//                    text = "Name is required",
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Email Field
//            OutlinedTextField(
//                value = email,
//                onValueChange = {
//                    email = it
//                    emailError = false
//                },
//                label = { Text("Email") },
//                isError = emailError,
//                trailingIcon = {
//                    if (emailError) {
//                        Icon(
//                            imageVector = Icons.Default.Error,
//                            contentDescription = "Error",
//                            tint = MaterialTheme.colorScheme.error
//                        )
//                    }
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedTextColor = Color.Black,
//                    unfocusedTextColor = Color.Black
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//            if (emailError) {
//                Text(
//                    text = "Email is required",
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Password Field
//            OutlinedTextField(
//                value = password,
//                onValueChange = {
//                    password = it
//                    passwordError = false
//                },
//                label = { Text("Password") },
//                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                isError = passwordError,
//                trailingIcon = {
//                    Row {
//                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                            Icon(
//                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
//                            )
//                        }
//                        if (passwordError) {
//                            Icon(
//                                imageVector = Icons.Default.Error,
//                                contentDescription = "Error",
//                                tint = MaterialTheme.colorScheme.error
//                            )
//                        }
//                    }
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedTextColor = Color.Black,
//                    unfocusedTextColor = Color.Black
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//            if (passwordError) {
//                Text(
//                    text = "Password is required",
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Error message from ViewModel
//            uiState.error?.let {
//                Text(
//                    text = it,
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // Loading indicator
//            if (uiState.loading) {
//                CircularProgressIndicator()
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            Button(
//                onClick = { validateAndRegister() },
//                enabled = !uiState.loading,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Register")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedButton(
//                onClick = {
//                    oneTapClient.beginSignIn(signUpRequest)
//                        .addOnSuccessListener { result ->
//                            try {
//                                googleLauncher.launch(
//                                    androidx.activity.result.IntentSenderRequest.Builder(result.pendingIntent).build()
//                                )
//                            } catch (e: Exception) {
//                                Toast.makeText(context, "Error launching Google Sign-In", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                        .addOnFailureListener { e ->
//                            Toast.makeText(context, "Google Sign-In setup failed: ${e.message}", Toast.LENGTH_SHORT).show()
//                        }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Sign up with Google")
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                "Already have an account? Login",
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.clickable {
//                    navController.navigate("login")
//                }
//            )
//        }
//    }
//}