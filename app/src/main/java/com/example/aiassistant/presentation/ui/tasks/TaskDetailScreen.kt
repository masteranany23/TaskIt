package com.example.aiassistant.presentation.ui.tasks

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.Task
import com.example.aiassistant.domain.model.TaskCategories
import com.example.aiassistant.presentation.ui.tasks.components.DifficultyBadge
import com.example.aiassistant.presentation.ui.tasks.components.DifficultyBadgeSize
import com.example.aiassistant.ui.theme.*

/**
 * TaskDetailScreen - Detailed view of a specific AI task
 * Features:
 * - Comprehensive task information
 * - Execution parameters and settings
 * - Preview of expected output
 * - Animated UI elements with futuristic design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    onBackClick: () -> Unit,
    onExecuteTask: () -> Unit
) {
    // Find the task by ID
    val task = remember(taskId) {
        TaskCategories.getAllCategories()
            .flatMap { it.tasks }
            .find { it.id == taskId }
    }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    if (task == null) {
        // Task not found - show error state
        TaskNotFoundScreen(onBackClick = onBackClick)
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, MediumBackground)
                )
            )
    ) {
        // Header with back button and task title
        TaskDetailHeader(
            task = task,
            isVisible = isVisible,
            onBackClick = onBackClick
        )
        
        // Scrollable content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Task overview card
            item {
                TaskOverviewCard(
                    task = task,
                    isVisible = isVisible
                )
            }
            
            // Task specifications
            item {
                TaskSpecificationsCard(
                    task = task,
                    isVisible = isVisible
                )
            }
            
            // Expected output preview
            item {
                ExpectedOutputCard(
                    task = task,
                    isVisible = isVisible
                )
            }
            
            // Execution parameters
            item {
                ExecutionParametersCard(
                    task = task,
                    isVisible = isVisible
                )
            }
            
            // Execute button
            item {
                ExecuteTaskButton(
                    task = task,
                    isVisible = isVisible,
                    onExecuteTask = onExecuteTask
                )
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * TaskDetailHeader - Header section with back button and task title
 */
@Composable
private fun TaskDetailHeader(
    task: Task,
    isVisible: Boolean,
    onBackClick: () -> Unit
) {
    val headerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "header_alpha"
    )
    
    val headerOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else (-30).dp,
        animationSpec = tween(600, delayMillis = 100),
        label = "header_offset"
    )
    
    Surface(
        color = DarkSurface.copy(alpha = 0.9f),
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = headerOffset)
            .alpha(headerAlpha)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MediumSurface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = ElectricBlue
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Task icon and title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Task icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = ElectricBlue.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = task.icon,
                        fontSize = 20.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = task.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "Task Details",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

/**
 * TaskOverviewCard - Main overview of the task
 */
@Composable
private fun TaskOverviewCard(
    task: Task,
    isVisible: Boolean
) {
    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "overview_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Card title
            Text(
                text = "📋 Task Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ElectricBlue,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Task description
            Text(
                text = task.description,
                fontSize = 16.sp,
                color = TextPrimary,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Metadata row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Estimated time
                InfoChip(
                    icon = "⏱️",
                    label = "Duration",
                    value = task.estimatedTime,
                    color = NeonGreen
                )
                
                // Difficulty
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Difficulty",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    DifficultyBadge(
                        difficulty = task.difficulty,
                        size = DifficultyBadgeSize.MEDIUM
                    )
                }
            }
        }
    }
}

/**
 * TaskSpecificationsCard - Technical specifications
 */
@Composable
private fun TaskSpecificationsCard(
    task: Task,
    isVisible: Boolean
) {
    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 300),
        label = "specs_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Card title
            Text(
                text = "🔧 Specifications",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeonPurple,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Tags
            if (task.tags.isNotEmpty()) {
                Text(
                    text = "Tags",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    task.tags.forEach { tag ->
                        Surface(
                            color = NeonPurple.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                color = NeonPurple,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            
            // Workflow ID if available
            task.workflowId?.let { workflowId ->
                InfoChip(
                    icon = "🔗",
                    label = "Workflow ID",
                    value = workflowId,
                    color = ElectricBlue
                )
            }
        }
    }
}

/**
 * ExpectedOutputCard - Preview of expected results
 */
@Composable
private fun ExpectedOutputCard(
    task: Task,
    isVisible: Boolean
) {
    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "output_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Card title
            Text(
                text = "📊 Expected Output",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Output preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = DarkSurface.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = getExpectedOutputPreview(task),
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * ExecutionParametersCard - Parameters and settings
 */
@Composable
private fun ExecutionParametersCard(
    task: Task,
    isVisible: Boolean
) {
    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 500),
        label = "params_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Card title
            Text(
                text = "⚙️ Execution Parameters",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentOrange,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Parameter list
            val parameters = getTaskParameters(task)
            parameters.forEach { param ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = param.first,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = param.second,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/**
 * ExecuteTaskButton - Main action button
 */
@Composable
private fun ExecuteTaskButton(
    task: Task,
    isVisible: Boolean,
    onExecuteTask: () -> Unit
) {
    val buttonAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 600),
        label = "button_alpha"
    )
    
    val buttonScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(buttonAlpha)
            .scale(buttonScale),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onExecuteTask,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricBlue,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🚀",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Execute Task",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * TaskNotFoundScreen - Error state when task is not found
 */
@Composable
private fun TaskNotFoundScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, MediumBackground)
                )
            )
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🔍",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "Task Not Found",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "The requested task could not be found.",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricBlue,
                contentColor = DarkBackground
            )
        ) {
            Text(text = "← Go Back")
        }
    }
}

/**
 * InfoChip - Reusable information chip component
 */
@Composable
private fun InfoChip(
    icon: String,
    label: String,
    value: String,
    color: Color
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Surface(
            color = color.copy(alpha = 0.2f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = icon,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = value,
                    fontSize = 12.sp,
                    color = color,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Helper functions for generating task-specific content
 */
private fun getExpectedOutputPreview(task: Task): String {
    return when (task.id) {
        "send_email" -> "📧 Professional email draft\n✓ Subject line optimized\n✓ Proper greeting and closing\n✓ Clear call-to-action"
        "create_report" -> "📊 Comprehensive business report\n✓ Executive summary\n✓ Data analysis and charts\n✓ Actionable recommendations"
        "summarize_video" -> "📝 Structured video summary\n✓ Key points extracted\n✓ Time-stamped highlights\n✓ Action items identified"
        else -> "🎯 AI-generated output based on your input\n✓ Professionally formatted\n✓ Accurate and relevant\n✓ Ready to use"
    }
}

private fun getTaskParameters(task: Task): List<Pair<String, String>> {
    return when (task.id) {
        "send_email" -> listOf(
            "Recipient" to "Required",
            "Subject" to "Auto-generated",
            "Tone" to "Professional",
            "Length" to "Medium"
        )
        "create_report" -> listOf(
            "Data Source" to "Required",
            "Format" to "PDF/DOCX",
            "Charts" to "Included",
            "Language" to "English"
        )
        else -> listOf(
            "Input" to "Required",
            "Quality" to "High",
            "Format" to "Optimized",
            "Language" to "Auto-detect"
        )
    }
}
