package com.example.aiassistant.presentation.ui.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.*
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/**
 * AIChatScreen - Module 4: Intelligent AI conversation interface
 * Features:
 * - Natural language AI conversations
 * - Task suggestions and automation
 * - Voice message support
 * - Contextual quick actions
 * - Multiple AI personalities
 * - Chat history and sessions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen(
    onBackClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onVoiceClick: () -> Unit = {}
) {
    // Screen state
    var messages by remember { mutableStateOf(ChatSampleData.getSampleChatMessages()) }
    var messageText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var selectedPersonality by remember { mutableStateOf(AIPersonality.PROFESSIONAL) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    LaunchedEffect(messages.size) {
        // Auto-scroll to bottom when new message is added
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
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
        // Header with AI personality and navigation
        ChatHeader(
            isVisible = isVisible,
            selectedPersonality = selectedPersonality,
            onPersonalityChange = { selectedPersonality = it },
            onBackClick = onBackClick,
            onVoiceClick = onVoiceClick
        )
        
        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    onTaskClick = onTaskClick,
                    onScheduleClick = onScheduleClick,
                    onQuickActionClick = { action ->
                        // Handle quick action clicks
                        when (action.action) {
                            "suggest_tasks" -> {
                                // Add AI response with task suggestions
                                val aiResponse = ChatMessage(
                                    id = "ai_${System.currentTimeMillis()}",
                                    content = "Here are some AI tasks that could help you:",
                                    isFromUser = false,
                                    timestamp = System.currentTimeMillis(),
                                    type = MessageType.TASK_SUGGESTION
                                )
                                messages = messages + aiResponse
                            }
                            "view_schedule" -> onScheduleClick()
                            "start_task" -> message.metadata?.suggestedTask?.let { onTaskClick(it) }
                        }
                    }
                )
            }
            
            // Typing indicator
            if (isTyping) {
                item {
                    TypingIndicator()
                }
            }
        }
        
        // Message input
        MessageInput(
            message = messageText,
            onMessageChange = { messageText = it },
            onSendMessage = {
                if (messageText.isNotBlank()) {
                    // Add user message
                    val userMessage = ChatMessage(
                        id = "user_${System.currentTimeMillis()}",
                        content = messageText,
                        isFromUser = true,
                        timestamp = System.currentTimeMillis()
                    )
                    messages = messages + userMessage
                    val messageContent = messageText
                    messageText = ""
                    keyboardController?.hide()
                    
                    // Simulate AI response
                    isTyping = true
                }
            },
            isVisible = isVisible
        )
    }
    
    // Handle AI response generation outside of the composable scope
    LaunchedEffect(isTyping) {
        if (isTyping) {
            delay(2000) // Simulate thinking time
            isTyping = false
            
            // Get the last user message to generate response
            val lastUserMessage = messages.lastOrNull { it.isFromUser }
            if (lastUserMessage != null) {
                val aiResponse = ChatMessage(
                    id = "ai_${System.currentTimeMillis()}",
                    content = generateAIResponse(lastUserMessage.content),
                    isFromUser = false,
                    timestamp = System.currentTimeMillis()
                )
                messages = messages + aiResponse
            }
        }
    }
}

/**
 * ChatHeader - Header with AI personality selector and navigation
 */
@Composable
private fun ChatHeader(
    isVisible: Boolean,
    selectedPersonality: AIPersonality,
    onPersonalityChange: (AIPersonality) -> Unit,
    onBackClick: () -> Unit,
    onVoiceClick: () -> Unit
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
            // Top row
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
                
                // AI info
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedPersonality.avatar,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        Text(
                            text = "TaskIt",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    
                    Text(
                        text = selectedPersonality.displayName,
                        fontSize = 12.sp,
                        color = selectedPersonality.color
                    )
                }
                
                // Voice button
                IconButton(
                    onClick = onVoiceClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = NeonGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Text(
                        text = "🎤",
                        fontSize = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Personality selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(AIPersonality.values()) { personality ->
                    PersonalityChip(
                        personality = personality,
                        isSelected = selectedPersonality == personality,
                        onClick = { onPersonalityChange(personality) }
                    )
                }
            }
        }
    }
}

/**
 * PersonalityChip - Chip for selecting AI personality
 */
@Composable
private fun PersonalityChip(
    personality: AIPersonality,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) personality.color.copy(alpha = 0.3f) else LightSurface.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "bg_color"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) personality.color else TextSecondary,
        animationSpec = tween(200),
        label = "text_color"
    )
    
    Surface(
        onClick = onClick,
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = personality.avatar,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 6.dp)
            )
            
            Text(
                text = personality.displayName,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}

/**
 * MessageBubble - Individual chat message bubble
 */
@Composable
private fun MessageBubble(
    message: ChatMessage,
    onTaskClick: (Task) -> Unit,
    onScheduleClick: () -> Unit,
    onQuickActionClick: (QuickAction) -> Unit
) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isFromUser) ElectricBlue.copy(alpha = 0.2f) else LightSurface.copy(alpha = 0.8f)
    val textColor = if (message.isFromUser) TextPrimary else TextPrimary
    
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                bottomEnd = if (message.isFromUser) 4.dp else 16.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Message content
                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = textColor,
                    lineHeight = 20.sp
                )
                
                // Timestamp
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                    fontSize = 10.sp,
                    color = TextTertiary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                )
                
                // Metadata content (task suggestions, quick actions, etc.)
                message.metadata?.let { metadata ->
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Suggested task
                    metadata.suggestedTask?.let { task ->
                        TaskSuggestionCard(
                            task = task,
                            onClick = { onTaskClick(task) }
                        )
                    }
                    
                    // Quick actions
                    metadata.actionButtons?.let { actions ->
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(actions) { action ->
                                QuickActionButton(
                                    action = action,
                                    onClick = { onQuickActionClick(action) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * TaskSuggestionCard - Card showing suggested task
 */
@Composable
private fun TaskSuggestionCard(
    task: Task,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = NeonGreen.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.icon,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                
                Text(
                    text = task.estimatedTime,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
            
            Text(
                text = "→",
                fontSize = 12.sp,
                color = NeonGreen
            )
        }
    }
}

/**
 * QuickActionButton - Quick action button in chat
 */
@Composable
private fun QuickActionButton(
    action: QuickAction,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = ElectricBlue.copy(alpha = 0.2f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = action.icon,
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
            
            Text(
                text = action.label,
                fontSize = 11.sp,
                color = ElectricBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * TypingIndicator - Shows when AI is typing
 */
@Composable
private fun TypingIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Card(
            modifier = Modifier.widthIn(max = 80.dp),
            colors = CardDefaults.cardColors(containerColor = LightSurface.copy(alpha = 0.8f)),
            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val alpha by rememberInfiniteTransition(label = "typing").animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot_alpha"
                    )
                    
                    Text(
                        text = "●",
                        fontSize = 12.sp,
                        color = TextSecondary.copy(alpha = alpha),
                        modifier = Modifier.padding(horizontal = 1.dp)
                    )
                    
                    if (index < 2) {
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        }
    }
}

/**
 * MessageInput - Input field for typing messages
 */
@Composable
private fun MessageInput(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isVisible: Boolean
) {
    val inputAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "input_alpha"
    )
    
    Surface(
        color = DarkSurface.copy(alpha = 0.9f),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(inputAlpha)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Message input field
            OutlinedTextField(
                value = message,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Ask your task assistant...",
                        color = TextTertiary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricBlue,
                    unfocusedBorderColor = MediumSurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = ElectricBlue
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSendMessage() })
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Send button
            IconButton(
                onClick = onSendMessage,
                enabled = message.isNotBlank(),
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (message.isNotBlank()) ElectricBlue.copy(alpha = 0.3f) else MediumSurface,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Text(
                    text = "➤",
                    fontSize = 20.sp,
                    color = if (message.isNotBlank()) ElectricBlue else TextTertiary
                )
            }
        }
    }
}

/**
 * Generate AI response based on user input
 */
private fun generateAIResponse(userMessage: String): String {
    return when {
        userMessage.lowercase().contains("schedule") -> 
            "I can help you organize your schedule! I see you have some upcoming tasks. Would you like me to suggest optimal time slots or create automated reminders?"
        
        userMessage.lowercase().contains("task") -> 
            "I can suggest AI tasks that might help you be more productive. Would you like me to analyze your current workload and recommend automation opportunities?"
        
        userMessage.lowercase().contains("email") -> 
            "I can help you set up email automation! This could save you significant time each day. Would you like me to create smart filters and auto-responses?"
        
        userMessage.lowercase().contains("help") -> 
            "I'm here to help! I can assist with scheduling, task automation, email management, and much more. What specific area would you like to focus on?"
        
        else -> 
            "That's interesting! I'm always learning from our conversations. Is there a specific task or schedule item I can help you with today?"
    }
}
