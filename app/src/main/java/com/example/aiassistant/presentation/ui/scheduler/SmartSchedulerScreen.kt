package com.example.aiassistant.presentation.ui.scheduler

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.*
import com.example.aiassistant.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * SmartSchedulerScreen - Module 3: Intelligent task and time management
 * Features:
 * - Visual weekly/daily schedule view
 * - AI-powered scheduling suggestions
 * - Integrated task automation
 * - Priority-based organization
 * - Smart reminders and notifications
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSchedulerScreen(
    onBackClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    // Screen state
    var selectedView by remember { mutableStateOf(SchedulerView.TODAY) }
    var scheduleItems by remember { mutableStateOf(SchedulerSampleData.getSampleScheduleItems()) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
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
        // Header with navigation and view toggles
        SchedulerHeader(
            isVisible = isVisible,
            selectedView = selectedView,
            onViewChange = { selectedView = it },
            onBackClick = onBackClick,
            onChatClick = onChatClick
        )
        
        // Main content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quick stats section
            item {
                QuickStatsSection(
                    scheduleItems = scheduleItems,
                    isVisible = isVisible
                )
            }
            
            // Time slots section
            item {
                TimeSlotsSection(
                    scheduleItems = scheduleItems,
                    selectedView = selectedView,
                    isVisible = isVisible,
                    onItemClick = { item ->
                        item.associatedTask?.let { onTaskClick(it) }
                    }
                )
            }
            
            // AI suggestions section
            item {
                AISuggestionsSection(
                    isVisible = isVisible,
                    onSuggestionClick = { /* Handle AI suggestion */ }
                )
            }
            
            // Upcoming tasks section
            item {
                UpcomingTasksSection(
                    scheduleItems = scheduleItems.filter { !it.isCompleted },
                    isVisible = isVisible,
                    onTaskClick = { item ->
                        item.associatedTask?.let { onTaskClick(it) }
                    }
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
 * SchedulerView - Different view modes for the scheduler
 */
enum class SchedulerView(val displayName: String) {
    TODAY("Today"),
    WEEK("Week"),
    MONTH("Month")
}

/**
 * SchedulerHeader - Header with navigation and view controls
 */
@Composable
private fun SchedulerHeader(
    isVisible: Boolean,
    selectedView: SchedulerView,
    onViewChange: (SchedulerView) -> Unit,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val headerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "header_alpha"
    )
    
    Surface(
        color = DarkSurface.copy(alpha = 0.9f),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(headerAlpha)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top row with back button and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                
                // Title
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Smart Scheduler",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "AI-powered time management",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                
                // Chat button
                IconButton(
                    onClick = onChatClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = NeonGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Text(
                        text = "💬",
                        fontSize = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // View selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SchedulerView.values().forEach { view ->
                    ViewSelectorChip(
                        view = view,
                        isSelected = selectedView == view,
                        onClick = { onViewChange(view) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * ViewSelectorChip - Chip for selecting scheduler view
 */
@Composable
private fun ViewSelectorChip(
    view: SchedulerView,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) ElectricBlue.copy(alpha = 0.3f) else LightSurface.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "bg_color"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) ElectricBlue else TextSecondary,
        animationSpec = tween(200),
        label = "text_color"
    )
    
    Surface(
        onClick = onClick,
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Text(
            text = view.displayName,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )
    }
}

/**
 * QuickStatsSection - Display quick statistics
 */
@Composable
private fun QuickStatsSection(
    scheduleItems: List<ScheduleItem>,
    isVisible: Boolean
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "stats_alpha"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(sectionAlpha),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Today's tasks
        StatCard(
            title = "Today",
            value = scheduleItems.count { !it.isCompleted }.toString(),
            subtitle = "tasks",
            color = NeonGreen,
            modifier = Modifier.weight(1f)
        )
        
        // Completed
        StatCard(
            title = "Done",
            value = scheduleItems.count { it.isCompleted }.toString(),
            subtitle = "completed",
            color = ElectricBlue,
            modifier = Modifier.weight(1f)
        )
        
        // Priority
        StatCard(
            title = "Priority",
            value = scheduleItems.count { it.priority == SchedulePriority.HIGH || it.priority == SchedulePriority.URGENT }.toString(),
            subtitle = "urgent",
            color = AccentRed,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * StatCard - Individual statistic card
 */
@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = value,
                fontSize = 24.sp,
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextTertiary
            )
        }
    }
}

/**
 * TimeSlotsSection - Display time slots with scheduled items
 */
@Composable
private fun TimeSlotsSection(
    scheduleItems: List<ScheduleItem>,
    selectedView: SchedulerView,
    isVisible: Boolean,
    onItemClick: (ScheduleItem) -> Unit
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 300),
        label = "timeslots_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(sectionAlpha),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📅 ${selectedView.displayName} Schedule",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ElectricBlue,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Time slots
            scheduleItems.sortedBy { it.startTime }.forEach { item ->
                ScheduleItemCard(
                    item = item,
                    onClick = { onItemClick(item) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * ScheduleItemCard - Individual schedule item card
 */
@Composable
private fun ScheduleItemCard(
    item: ScheduleItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val startTime = timeFormat.format(Date(item.startTime))
    val endTime = timeFormat.format(Date(item.endTime))
    
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = item.type.color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp)
            ) {
                Text(
                    text = startTime,
                    fontSize = 14.sp,
                    color = item.type.color,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = endTime,
                    fontSize = 12.sp,
                    color = TextTertiary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.type.icon,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                if (item.description.isNotEmpty()) {
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Priority indicator
            Surface(
                color = item.priority.color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = item.priority.displayName,
                    fontSize = 10.sp,
                    color = item.priority.color,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * AISuggestionsSection - AI-powered scheduling suggestions
 */
@Composable
private fun AISuggestionsSection(
    isVisible: Boolean,
    onSuggestionClick: () -> Unit
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "suggestions_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(sectionAlpha),
        colors = CardDefaults.cardColors(
            containerColor = NeonPurple.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🤖",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                
                Text(
                    text = "AI Suggestions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPurple
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Suggestion items
            SuggestionItem(
                icon = "⚡",
                title = "Optimize Schedule",
                description = "I can reschedule your tasks for better productivity",
                onClick = onSuggestionClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            SuggestionItem(
                icon = "🎯",
                title = "Focus Time Block",
                description = "Add 2-hour focus blocks for deep work",
                onClick = onSuggestionClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            SuggestionItem(
                icon = "📧",
                title = "Automate Emails",
                description = "Set up automated responses during focus time",
                onClick = onSuggestionClick
            )
        }
    }
}

/**
 * SuggestionItem - Individual AI suggestion
 */
@Composable
private fun SuggestionItem(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            Text(
                text = "→",
                fontSize = 16.sp,
                color = NeonPurple
            )
        }
    }
}

/**
 * UpcomingTasksSection - Show upcoming tasks with AI integration
 */
@Composable
private fun UpcomingTasksSection(
    scheduleItems: List<ScheduleItem>,
    isVisible: Boolean,
    onTaskClick: (ScheduleItem) -> Unit
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 500),
        label = "upcoming_alpha"
    )
    
    if (scheduleItems.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(sectionAlpha),
            colors = CardDefaults.cardColors(
                containerColor = LightSurface.copy(alpha = 0.8f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "⏰ Upcoming Tasks",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentOrange,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                scheduleItems.take(3).forEach { item ->
                    UpcomingTaskItem(
                        item = item,
                        onClick = { onTaskClick(item) }
                    )
                }
            }
        }
    }
}

/**
 * UpcomingTaskItem - Individual upcoming task item
 */
@Composable
private fun UpcomingTaskItem(
    item: ScheduleItem,
    onClick: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    val timeText = timeFormat.format(Date(item.startTime))
    
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.type.icon,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                
                Text(
                    text = timeText,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            if (item.associatedTask != null) {
                Surface(
                    color = NeonGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "AI",
                        fontSize = 10.sp,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
