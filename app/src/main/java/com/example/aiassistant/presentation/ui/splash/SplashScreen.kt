package com.example.aiassistant.presentation.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * SplashScreen - Modern animated splash screen for TaskIt
 * Features:
 * - Sophisticated TaskIt logo animation with text differentiation
 * - Smooth particle effects and transitions
 * - Fast loading with attractive visuals
 * - Professional branding for task management
 */
@Composable
fun SplashScreen(
    onNavigateToTaskLibrary: () -> Unit
) {
    // Animation states for smooth, fast loading
    val infiniteTransition = rememberInfiniteTransition(label = "splash_animation")
    
    // Logo scale animation - quick and smooth
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutQuart),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )
    
    // Text glow animation
    val textGlow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "text_glow"
    )
    
    // Floating particles rotation
    val particleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_rotation"
    )
    
    // Text entrance animation
    var textVisible by remember { mutableStateOf(false) }
    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1f else 0f,
        animationSpec = tween(800, easing = EaseOutQuart),
        label = "text_alpha"
    )
    
    // Auto-navigate after delay
    LaunchedEffect(Unit) {
        delay(500) // Quick entrance
        textVisible = true
        delay(2000) // Show for 2 seconds
        onNavigateToTaskLibrary()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        DarkBackground.copy(alpha = 0.95f),
                        DarkSurface
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background particles
        TaskItParticleBackground(rotation = particleRotation)
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // TaskIt Logo with animated icon
            TaskItLogo(
                scale = logoScale,
                modifier = Modifier.size(100.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // TaskIt brand name with differentiation
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                // "Task" part
                Text(
                    text = "Task",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ElectricBlue.copy(alpha = textAlpha * textGlow),
                    letterSpacing = 1.sp,
                    modifier = Modifier.scale(if (textVisible) 1f else 0.8f)
                )
                
                // "It" part with different styling
                Text(
                    text = "It",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    color = NeonGreen.copy(alpha = textAlpha * textGlow),
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .scale(if (textVisible) 1f else 0.8f)
                        .offset(y = (-2).dp) // Slight offset for visual interest
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Smart Task Management",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary.copy(alpha = textAlpha * 0.8f),
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Modern loading indicator
            ModernLoadingIndicator(alpha = textAlpha)
        }
    }
}

/**
 * TaskItLogo - Modern logo icon for TaskIt
 * Creates a stylish task/checkmark-based logo with smooth animations
 */
@Composable
private fun TaskItLogo(
    scale: Float,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.scale(scale)
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2.5f
        
        // Outer ring with gradient
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricBlue.copy(alpha = 0.3f),
                    ElectricBlue.copy(alpha = 0.1f),
                    Color.Transparent
                ),
                radius = radius * 1.2f
            ),
            radius = radius,
            center = center
        )
        
        // Inner circle background
        drawCircle(
            color = ElectricBlue.copy(alpha = 0.2f),
            radius = radius * 0.7f,
            center = center
        )
        
        // Checkmark/Task completed symbol
        val strokeWidth = size.width * 0.08f
        val checkPath = androidx.compose.ui.graphics.Path()
        
        // Create checkmark path
        val checkStart = Offset(center.x - radius * 0.3f, center.y)
        val checkMid = Offset(center.x - radius * 0.1f, center.y + radius * 0.2f)
        val checkEnd = Offset(center.x + radius * 0.4f, center.y - radius * 0.3f)
        
        checkPath.moveTo(checkStart.x, checkStart.y)
        checkPath.lineTo(checkMid.x, checkMid.y)
        checkPath.lineTo(checkEnd.x, checkEnd.y)
        
        // Draw the checkmark
        drawPath(
            path = checkPath,
            color = NeonGreen,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                join = androidx.compose.ui.graphics.StrokeJoin.Round
            )
        )
        
        // Add subtle glow effect
        drawPath(
            path = checkPath,
            color = NeonGreen.copy(alpha = 0.3f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth * 2f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                join = androidx.compose.ui.graphics.StrokeJoin.Round
            )
        )
    }
}

/**
 * TaskItParticleBackground - Subtle animated background particles
 * Creates a professional, clean background for TaskIt branding
 */
@Composable
private fun TaskItParticleBackground(rotation: Float) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        // Draw subtle floating particles representing tasks/productivity
        repeat(12) { i ->
            val angle = (rotation + i * 30f) * (kotlin.math.PI / 180f)
            val distance = (size.minDimension / 3f) + (i * 25f)
            val x = size.width / 2f + cos(angle).toFloat() * distance
            val y = size.height / 2f + sin(angle).toFloat() * distance
            
            // Only draw if within screen bounds
            if (x in 0f..size.width && y in 0f..size.height) {
                // Small task-like squares with rounded corners
                drawRoundRect(
                    color = ElectricBlue.copy(alpha = 0.1f + (i % 3) * 0.05f),
                    topLeft = Offset(x - 3.dp.toPx(), y - 3.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(6.dp.toPx(), 6.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                )
            }
        }
        
        // Add some scattered checkmarks
        repeat(6) { i ->
            val angle = (rotation * 0.5f + i * 60f) * (kotlin.math.PI / 180f)
            val distance = size.minDimension / 2.5f + (i * 30f)
            val x = size.width / 2f + cos(angle).toFloat() * distance
            val y = size.height / 2f + sin(angle).toFloat() * distance
            
            if (x in 0f..size.width && y in 0f..size.height) {
                drawCircle(
                    color = NeonGreen.copy(alpha = 0.08f + (i % 2) * 0.04f),
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
    }
}

/**
 * ModernLoadingIndicator - Sleek loading indicator for TaskIt
 */
@Composable
private fun ModernLoadingIndicator(alpha: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_indicator")
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val animatedScale by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500, delayMillis = index * 150),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_scale_$index"
            )
            
            Box(
                modifier = Modifier
                    .size((6 * animatedScale).dp)
                    .background(
                        color = when (index) {
                            0 -> ElectricBlue.copy(alpha = alpha)
                            1 -> NeonGreen.copy(alpha = alpha)
                            else -> ElectricBlue.copy(alpha = alpha * 0.7f)
                        },
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}
