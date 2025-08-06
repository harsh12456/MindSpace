
package com.example.mindspace.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.mindspace.viewmodel.AuthViewModel
import com.example.mindspace.ui.components.*
import com.example.mindspace.ui.theme.MindSpaceColors
import com.example.mindspace.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    initialLoginMode: Boolean = true
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var isLoginMode by remember { mutableStateOf(initialLoginMode) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var shakeForm by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    var isGoogleExpanded by remember { mutableStateOf(false) }

    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    val oneTapClient: SignInClient = remember { Identity.getSignInClient(context) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                if (googleIdToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showSuccess = true
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Authentication failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Toast.makeText(
                    context,
                    "Google Sign-In failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(uiState.loginSuccess, uiState.registerSuccess) {
        if (uiState.loginSuccess || uiState.registerSuccess) {
            showSuccess = true
            delay(1500)
            navController.navigate("home") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    LaunchedEffect(shakeForm) {
        if (shakeForm) {
            delay(500)
            shakeForm = false
        }
    }

    fun validateAndSubmit() {
        val isNameValid = isLoginMode || name.isNotBlank()
        val isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        nameError = !isNameValid
        emailError = !isEmailValid
        passwordError = !isPasswordValid

        if (isNameValid && isEmailValid && isPasswordValid) {
            if (isLoginMode) {
                viewModel.login(email, password)
            } else {
                viewModel.register(name, email, password)
            }
        } else {
            shakeForm = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedGradientBackground()
        FloatingParticles(
            modifier = Modifier.fillMaxSize(),
            particleCount = 20,
            colors = listOf(
                MindSpaceColors.breathingBlue.copy(alpha = 0.3f),
                MindSpaceColors.meditationPurple.copy(alpha = 0.2f),
                Color.White.copy(alpha = 0.1f)
            )
        )
        WaveBackground(
            modifier = Modifier.fillMaxSize(),
            waveColor = MindSpaceColors.breathingBlue.copy(alpha = 0.05f),
            animationDuration = 6000
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            BreathingLogo()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "MindSpace",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Find your inner peace",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            AuthModeToggle(
                isLoginMode = isLoginMode,
                onToggle = {
                    isLoginMode = it
                    name = ""
                    email = ""
                    password = ""
                    nameError = false
                    emailError = false
                    passwordError = false
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            val formProgress = when {
                !isLoginMode && name.isNotBlank() && email.isNotBlank() && password.isNotBlank() -> 1f
                isLoginMode && email.isNotBlank() && password.isNotBlank() -> 1f
                email.isNotBlank() && password.isNotBlank() -> 0.66f
                email.isNotBlank() || password.isNotBlank() -> 0.33f
                else -> 0f
            }

            SmoothProgressIndicator(
                progress = formProgress,
                modifier = Modifier
                    .size(40.dp)
                    .alpha(if (formProgress > 0f) 0.7f else 0.3f),
                strokeWidth = 3.dp,
                color = MindSpaceColors.breathingBlue
            )

            Spacer(modifier = Modifier.height(32.dp))

            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                ShakeAnimation(shake = shakeForm) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = if (isLoginMode) "Welcome Back" else "Create Account",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        AnimatedVisibility(
                            visible = !isLoginMode,
                            enter = slideInVertically { -it } + fadeIn(),
                            exit = slideOutVertically { -it } + fadeOut()
                        ) {
                            ModernTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    nameError = false
                                },
                                label = "Full Name",
                                isError = nameError,
                                errorMessage = "Name is required"
                            )
                        }

                        ModernTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = false
                            },
                            label = "Email Address",
                            keyboardType = KeyboardType.Email,
                            isError = emailError,
                            errorMessage = "Enter a valid email address"
                        )

                        ModernTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                passwordError = false
                            },
                            label = "Password",
                            isPassword = true,
                            isError = passwordError,
                            errorMessage = if (isLoginMode) "Password is required" else "Password must be at least 6 characters"
                        )

                        AnimatedVisibility(
                            visible = uiState.error != null,
                            enter = slideInVertically() + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) {
                            uiState.error?.let { error ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Red.copy(alpha = 0.1f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "⚠️ $error",
                                        color = Color.Red.copy(alpha = 0.9f),
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.loading) {
                                LoadingRipple(
                                    modifier = Modifier.size(60.dp),
                                    color = MindSpaceColors.gradientStart,
                                    rippleCount = 3
                                )
                            } else {
                                NeumorphicButton(
                                    onClick = { validateAndSubmit() },
                                    text = if (isLoginMode) "Sign In" else "Create Account",
                                    isLoading = uiState.loading
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = Color.White.copy(alpha = 0.3f)
                            )
                            Text(
                                text = "  or  ",
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = Color.White.copy(alpha = 0.3f)
                            )
                        }

                        GradientOutlinedButton(
                            onClick = {
                                oneTapClient.beginSignIn(signInRequest)
                                    .addOnSuccessListener { result ->
                                        try {
                                            launcher.launch(
                                                IntentSenderRequest.Builder(result.pendingIntent).build()
                                            )
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                "Error launching Google Sign-In",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Google Sign-In setup failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            },
                            text = if (isLoginMode) "Continue with Google" else "Sign up with Google",
                            modifier = Modifier.fillMaxWidth()
                        )

                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(40.dp))
        }

        ParticleSuccessAnimation(
            visible = showSuccess,
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        )
    }
}