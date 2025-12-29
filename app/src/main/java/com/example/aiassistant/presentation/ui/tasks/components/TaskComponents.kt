package com.example.aiassistant.presentation.ui.tasks.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.Task
import com.example.aiassistant.domain.model.TaskDifficulty
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay

/**
 * PopularTaskCard - Compact card for popular tasks in horizontal scroll
 * Features gradient background and hover animations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularTaskCard(
    task: Task,
    animationDelay: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.95f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "popular_task_scale"
    )
    
    val cardOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 30.dp,
        animationSpec = tween(500, delayMillis = animationDelay.toInt()),
        label = "popular_task_offset"
    )
    
    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(500, delayMillis = animationDelay.toInt()),
        label = "popular_task_alpha"
    )
    
    LaunchedEffect(Unit) {
        delay(animationDelay)
        isVisible = true
    }
    
    Card(
        onClick = onClick,
        modifier = modifier
            .width(160.dp)
            .scale(scale)
            .offset(y = cardOffset)
            .alpha(cardAlpha),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MediumSurface,
                            LightSurface.copy(alpha = 0.8f)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = ElectricBlue.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        isPressed = true
                        onClick()
                    }
                )
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Task icon with glow background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = NeonGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = task.icon,
                        fontSize = 20.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Task title
                Text(
                    text = task.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Task metadata row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Estimated time
                    Text(
                        text = task.estimatedTime,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    
                    // Difficulty indicator
                    DifficultyBadge(
                        difficulty = task.difficulty,
                        size = DifficultyBadgeSize.SMALL
                    )
                }
            }
        }
    }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

/**
 * TaskItem - Individual task item within expanded category
 * Shows detailed information about each task
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    categoryColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) categoryColor.copy(alpha = 0.1f) else Color.Transparent,
        animationSpec = tween(200),
        label = "task_item_background"
    )
    
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isHovered) 4.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Task icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = categoryColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = task.icon,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Task details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                
                Text(
                    text = task.description,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
                
                // Tags row
                if (task.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        task.tags.take(2).forEach { tag ->
                            Surface(
                                color = MediumSurface,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 10.sp,
                                    color = TextTertiary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Right side metadata
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = task.estimatedTime,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                DifficultyBadge(
                    difficulty = task.difficulty,
                    size = DifficultyBadgeSize.SMALL
                )
            }
        }
    }
}

/**
 * DifficultyBadge - Shows task difficulty with color coding
 */
@Composable
fun DifficultyBadge(
    difficulty: TaskDifficulty,
    size: DifficultyBadgeSize,
    modifier: Modifier = Modifier
) {
    val (fontSize, padding) = when (size) {
        DifficultyBadgeSize.SMALL -> 10.sp to PaddingValues(horizontal = 6.dp, vertical = 2.dp)
        DifficultyBadgeSize.MEDIUM -> 12.sp to PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    }
    
    Surface(
        color = difficulty.color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = difficulty.displayName,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            color = difficulty.color,
            modifier = Modifier.padding(padding)
        )
    }
}

enum class DifficultyBadgeSize {
    SMALL, MEDIUM
}

/**
 * PulseDot - Animated pulsing dot for indicating activity
 */
@Composable
fun PulseDot(
    color: Color = NeonGreen,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_dot")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_dot_scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_dot_alpha"
    )
    
    Box(
        modifier = modifier
            .size(8.dp)
            .scale(scale)
            .background(
                color = color.copy(alpha = alpha),
                shape = androidx.compose.foundation.shape.CircleShape
            )
    )
}
