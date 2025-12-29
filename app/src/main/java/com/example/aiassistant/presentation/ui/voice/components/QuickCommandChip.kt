package com.example.aiassistant.presentation.ui.voice.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.ui.theme.*

/**
 * QuickCommandChip - Interactive chip for quick voice commands
 * Provides one-tap access to common voice commands
 */
@Composable
fun QuickCommandChip(
    text: String,
    icon: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    color: Color = ElectricBlue,
    onClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.95f
            isSelected -> 1.05f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val containerColor by animateColorAsState(
        targetValue = when {
            isSelected -> color.copy(alpha = 0.2f)
            else -> LightSurface.copy(alpha = 0.8f)
        },
        animationSpec = tween(200),
        label = "containerColor"
    )
    
    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> color
            else -> color.copy(alpha = 0.3f)
        },
        animationSpec = tween(200),
        label = "borderColor"
    )
    
    Card(
        modifier = modifier
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Icon
            Text(
                text = icon,
                fontSize = 16.sp
            )
            
            // Text
            Text(
                text = text,
                color = if (isSelected) color else TextPrimary,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
    
    // Reset pressed state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

/**
 * QuickCommandGroup - Group of related quick commands
 */
@Composable
fun QuickCommandGroup(
    title: String,
    commands: List<Pair<String, String>>, // Pair of (text, icon)
    modifier: Modifier = Modifier,
    maxItemsPerRow: Int = 2,
    onCommandClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Group title
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        // Commands in rows
        val chunkedCommands = commands.chunked(maxItemsPerRow)
        chunkedCommands.forEach { rowCommands ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCommands.forEach { (text, icon) ->
                    QuickCommandChip(
                        text = text,
                        icon = icon,
                        modifier = Modifier.weight(1f),
                        onClick = { onCommandClick(text) }
                    )
                }
                
                // Fill remaining space if odd number of items
                if (rowCommands.size < maxItemsPerRow) {
                    repeat(maxItemsPerRow - rowCommands.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/**
 * PulsatingChip - Chip with pulsating animation for attention
 */
@Composable
fun PulsatingChip(
    text: String,
    icon: String,
    modifier: Modifier = Modifier,
    color: Color = ElectricBlue,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Card(
        modifier = modifier
            .scale(scale)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = alpha * 0.2f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    color.copy(alpha = alpha),
                    color.copy(alpha = alpha * 0.7f)
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = icon,
                fontSize = 18.sp
            )
            
            Text(
                text = text,
                color = color.copy(alpha = alpha),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
