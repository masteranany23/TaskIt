package com.example.aiassistant.presentation.ui.voice.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.aiassistant.ui.theme.*
import kotlin.math.*
import kotlin.random.Random

/**
 * VoiceWaveform - Animated voice visualization component
 * Creates a futuristic waveform animation that responds to voice state
 */
@Composable
fun VoiceWaveform(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    amplitude: Float = 1f,
    color: Color = ElectricBlue,
    strokeWidth: Float = 4f,
    waveCount: Int = 5
) {
    // Animation values for wave motion
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    
    val amplitudeAnim by animateFloatAsState(
        targetValue = if (isActive) amplitude else 0.3f,
        animationSpec = tween(300),
        label = "amplitude"
    )
    
    Canvas(modifier = modifier.fillMaxWidth().height(120.dp)) {
        val centerY = size.height / 2
        val waveLength = size.width / 3f
        
        // Draw multiple wave layers for depth
        repeat(waveCount) { index ->
            val layerAlpha = 1f - (index * 0.15f)
            val layerAmplitude = amplitudeAnim * (1f - index * 0.2f)
            val phaseOffset = index * 0.3f
            
            drawWave(
                centerY = centerY,
                amplitude = layerAmplitude * 30f,
                wavelength = waveLength,
                phase = phase + phaseOffset,
                color = color.copy(alpha = layerAlpha),
                strokeWidth = strokeWidth - index
            )
        }
        
        // Draw base line
        drawLine(
            color = color.copy(alpha = 0.3f),
            start = Offset(0f, centerY),
            end = Offset(size.width, centerY),
            strokeWidth = 1f
        )
    }
}

/**
 * Draw a single wave on the canvas
 */
private fun DrawScope.drawWave(
    centerY: Float,
    amplitude: Float,
    wavelength: Float,
    phase: Float,
    color: Color,
    strokeWidth: Float
) {
    val points = mutableListOf<Offset>()
    val steps = size.width.toInt() / 2
    
    for (i in 0..steps) {
        val x = (i.toFloat() / steps) * size.width
        val normalizedX = x / wavelength
        
        // Create complex wave with multiple harmonics
        val wave1 = sin(normalizedX * 2 * PI + phase)
        val wave2 = sin(normalizedX * 4 * PI + phase * 1.5f) * 0.5f
        val wave3 = sin(normalizedX * 6 * PI + phase * 2f) * 0.25f
        
        val combinedWave = wave1 + wave2 + wave3
        val y = centerY + combinedWave * amplitude
        
        points.add(Offset(x.toFloat(), y.toFloat()))
    }
    
    // Draw the wave as connected lines
    for (i in 0 until points.size - 1) {
        drawLine(
            color = color,
            start = points[i],
            end = points[i + 1],
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

/**
 * VoicePulse - Simple pulsing animation for voice activation
 */
@Composable
fun VoicePulse(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    color: Color = ElectricBlue,
    maxRadius: Float = 50f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val radius by infiniteTransition.animateFloat(
        initialValue = maxRadius * 0.3f,
        targetValue = maxRadius,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse  
        ),
        label = "radius"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Canvas(modifier = modifier.size((maxRadius * 2).dp)) {
        if (isActive) {
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = radius,
                center = center
            )
        }
        
        // Always draw the center dot
        drawCircle(
            color = color,
            radius = maxRadius * 0.2f,
            center = center
        )
    }
}
