package com.example.mindspace.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mindspace.ui.theme.MindSpaceColors
import kotlin.math.*
import kotlin.random.Random
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun FloatingParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 15,
    colors: List<Color> = listOf(
        MindSpaceColors.breathingBlue,
        MindSpaceColors.meditationPurple,
        Color.White.copy(alpha = 0.6f)
    )
) {
    val density = LocalDensity.current
    var particles by remember {
        mutableStateOf(
            (0 until particleCount).map {
                Particle(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    size = Random.nextFloat() * 4f + 2f,
                    speedX = (Random.nextFloat() - 0.5f) * 0.02f,
                    speedY = (Random.nextFloat() - 0.5f) * 0.02f,
                    color = colors.random(),
                    alpha = Random.nextFloat() * 0.5f + 0.3f
                )
            }
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { _ ->
                particles = particles.map { particle ->
                    particle.copy(
                        x = (particle.x + particle.speedX) % 1f,
                        y = (particle.y + particle.speedY) % 1f
                    )
                }
            }
        }
    }

    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            drawCircle(
                color = particle.color.copy(alpha = particle.alpha),
                radius = particle.size,
                center = Offset(
                    x = size.width * particle.x,
                    y = size.height * particle.y
                )
            )
        }
    }
}

data class Particle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speedX: Float,
    val speedY: Float,
    val color: Color,
    val alpha: Float
)

@Composable
fun PulsingOrb(
    size: Dp = 120.dp,
    color: Color = MindSpaceColors.breathingBlue,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsing_orb")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_alpha"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .alpha(alpha * 0.3f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Inner orb
        Box(
            modifier = Modifier
                .size(size * 0.6f)
                .scale(scale * 0.8f)
                .alpha(alpha)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            color,
                            color.copy(alpha = 0.6f)
                        )
                    )
                )
        )
    }
}

@Composable
fun WaveBackground(
    modifier: Modifier = Modifier,
    waveColor: Color = MindSpaceColors.breathingBlue.copy(alpha = 0.1f),
    animationDuration: Int = 4000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing)
        ),
        label = "wave_phase"
    )

    Canvas(modifier = modifier) {
        val amplitude = size.height * 0.1f
        val frequency = 2f
        val path = Path()

        // Create multiple wave layers
        repeat(3) { layer ->
            path.reset()
            val layerPhase = phase + (layer * PI.toFloat() / 3)
            val layerAmplitude = amplitude * (1f - layer * 0.2f)
            val centerY = size.height * (0.5f + layer * 0.1f)

            path.moveTo(0f, centerY)

            for (x in 0..size.width.toInt() step 4) {
                val normalizedX = x / size.width
                val y = centerY + sin(normalizedX * frequency * 2 * PI + layerPhase) * layerAmplitude
                path.lineTo(x.toFloat(), y.toFloat())
            }

            path.lineTo(size.width, size.height)
            path.lineTo(0f, size.height)
            path.close()

            drawPath(
                path = path,
                color = waveColor.copy(alpha = waveColor.alpha * (1f - layer * 0.3f))
            )
        }
    }
}

@Composable
fun LoadingRipple(
    modifier: Modifier = Modifier,
    color: Color = MindSpaceColors.gradientStart,
    rippleCount: Int = 3
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        repeat(rippleCount) { index ->
            val animationDelay = index * 400
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1200,
                        delayMillis = animationDelay,
                        easing = FastOutSlowInEasing
                    )
                ),
                label = "ripple_scale_$index"
            )

            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1200,
                        delayMillis = animationDelay,
                        easing = FastOutSlowInEasing
                    )
                ),
                label = "ripple_alpha_$index"
            )

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
fun MorphingButton(
    isExpanded: Boolean,
    normalContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedWidth by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 56.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_width"
    )

    val animatedHeight by animateDpAsState(
        targetValue = if (isExpanded) 56.dp else 56.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_height"
    )

    Box(
        modifier = modifier
            .size(width = animatedWidth, height = animatedHeight)
            .clip(CircleShape)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MindSpaceColors.gradientStart,
                        MindSpaceColors.gradientEnd
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.animation.AnimatedContent(
            targetState = isExpanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            },
            label = "button_content"
        ) { expanded ->
            if (expanded) {
                expandedContent()
            } else {
                normalContent()
            }
        }
    }
}

@Composable
fun SmoothProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 4.dp,
    color: Color = MindSpaceColors.gradientStart
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "progress"
    )

    Canvas(modifier = modifier) {
        val strokeWidthPx = strokeWidth.toPx()
        val radius = (size.minDimension - strokeWidthPx) / 2
        val center = Offset(size.width / 2, size.height / 2)

        // Background circle
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidthPx)
        )

        // Progress arc
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(
                    color,
                    color.copy(alpha = 0.7f),
                    color
                )
            ),
            startAngle = -90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            topLeft = Offset(
                center.x - radius,
                center.y - radius
            ),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            style = Stroke(
                width = strokeWidthPx,
                cap = StrokeCap.Round
            )
        )
    }
}