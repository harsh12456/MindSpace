package com.example.mindspace.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindspace.ui.theme.MindSpaceColors
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction

@Composable
fun AnimatedGradientBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val colors = listOf(
                MindSpaceColors.gradientStart,
                MindSpaceColors.gradientMiddle,
                MindSpaceColors.gradientEnd
            )

            drawRect(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(0f, size.height * animatedOffset),
                    end = Offset(size.width, size.height * (1f - animatedOffset))
                )
            )
        }

        // Overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
        )
    }
}

@Composable
fun BreathingLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_alpha"
    )

    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
        ) {
            val radius = size.minDimension / 3f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MindSpaceColors.breathingBlue.copy(alpha = alpha),
                        MindSpaceColors.meditationPurple.copy(alpha = alpha * 0.6f),
                        Color.Transparent
                    ),
                    radius = radius * 2
                ),
                radius = radius,
                center = center
            )
        }

        Text(
            text = "🧘‍♀️",
            fontSize = 32.sp,
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MindSpaceColors.glassBackground)
            .border(
                1.dp,
                MindSpaceColors.glassBorder,
                RoundedCornerShape(24.dp)
            )
            .blur(0.5.dp)
    ) {
        content()
    }
}

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,

    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val breathingAnimation = rememberInfiniteTransition(label = "breathing")
    val breathingAlpha by breathingAnimation.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,  // ✅ Always false to remove red exclamation
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType,imeAction = ImeAction.Done),
            maxLines = 1,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password"
                            else "Show password",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                // ✅ Removed error icon completely
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                focusedBorderColor = MindSpaceColors.breathingBlue.copy(alpha = breathingAlpha),
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                errorBorderColor = Color.White.copy(alpha = 0.5f),  // ✅ Same as unfocused
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                cursorColor = MindSpaceColors.breathingBlue,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent  // ✅ Added this
            ),
            modifier = Modifier.fillMaxWidth()
            // ✅ Removed the extra border - this was causing the double box effect
        )

        // ✅ Show error message below field instead of icon
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    text: String,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Fix: Use proper interaction state handling
    val isPressed by remember {
        derivedStateOf {
            // You can implement custom pressed state logic here if needed
            false
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "button_scale"
    )

    val shadowColor = if (enabled) Color.Black.copy(alpha = 0.3f)
    else Color.Black.copy(alpha = 0.1f)

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = if (enabled) listOf(
                        MindSpaceColors.gradientStart,
                        MindSpaceColors.gradientEnd
                    ) else listOf(
                        Color.Gray.copy(alpha = 0.5f),
                        Color.Gray.copy(alpha = 0.3f)
                    )
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !isLoading
            ) { onClick() }
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                color = if (enabled) Color.White else Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GradientOutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = ButtonDefaults.outlinedButtonBorder(enabled = enabled).copy(
            brush = Brush.horizontalGradient(
                listOf(
                    MindSpaceColors.gradientStart,
                    MindSpaceColors.gradientEnd
                )
            )
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ShakeAnimation(
    shake: Boolean,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (shake) 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shake_offset"
    )

    Box(
        modifier = Modifier.offset(x = shakeOffset.dp)
    ) {
        content()
    }
}

@Composable
fun ParticleSuccessAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            repeat(20) { index ->
                val angle = (index * 18f) * (Math.PI / 180f)
                val radius = 50f + (index * 5f)
                val x = center.x + cos(angle).toFloat() * radius
                val y = center.y + sin(angle).toFloat() * radius

                drawCircle(
                    color = MindSpaceColors.successGreen,
                    radius = 3f,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
fun AuthModeToggle(
    isLoginMode: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedOffset by animateFloatAsState(
        targetValue = if (isLoginMode) 0f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "toggle_offset"
    )

    Box(
        modifier = modifier
            .width(200.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Animated background indicator
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .offset(x = (100 * animatedOffset).dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MindSpaceColors.gradientStart,
                            MindSpaceColors.gradientEnd
                        )
                    )
                )
        )

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onToggle(true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login",
                    color = if (isLoginMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isLoginMode) FontWeight.Bold else FontWeight.Normal
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onToggle(false) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Register",
                    color = if (!isLoginMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (!isLoginMode) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}