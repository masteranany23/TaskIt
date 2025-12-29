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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.VoiceCommand
import com.example.aiassistant.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * VoiceCommandCard - Displays a voice command from history
 * Shows command text, execution status, and timestamp
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCommandCard(
    command: VoiceCommand,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val dateFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val timeText = dateFormat.format(Date(command.timestamp))
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = LightSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with status and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Success/failure indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (command.success) AccentGreen else AccentRed,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    
                    Text(
                        text = if (command.success) "Executed" else "Failed",
                        color = if (command.success) AccentGreen else AccentRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Timestamp
                Text(
                    text = timeText,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Command text
            Text(
                text = command.text,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            // Confidence indicator
            if (command.confidence > 0f) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Confidence:",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    
                    LinearProgressIndicator(
                        progress = command.confidence,
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = when {
                            command.confidence >= 0.9f -> AccentGreen
                            command.confidence >= 0.7f -> AccentYellow
                            else -> AccentRed
                        },
                        trackColor = MediumSurface
                    )
                    
                    Text(
                        text = "${(command.confidence * 100).toInt()}%",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
            
            // Task information if available
            command.taskExecuted?.let { task ->
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    ElectricBlue.copy(alpha = 0.1f),
                                    ElectricBlue.copy(alpha = 0.05f)
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = task.icon,
                        fontSize = 16.sp
                    )
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = task.title,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = task.description,
                            color = TextSecondary,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * VoiceCommandSummaryCard - Compact version for summary views
 */
@Composable
fun VoiceCommandSummaryCard(
    command: VoiceCommand,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status indicator
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = if (command.success) AccentGreen else AccentRed,
                        shape = RoundedCornerShape(3.dp)
                    )
            )
            
            // Command text
            Text(
                text = command.text,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            
            // Task icon if available
            command.taskExecuted?.let { task ->
                Text(
                    text = task.icon,
                    fontSize = 14.sp
                )
            }
        }
    }
}
