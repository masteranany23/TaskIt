package com.example.aiassistant.presentation.ui.voice

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.Task
import com.example.aiassistant.domain.model.VoiceCommand
import com.example.aiassistant.domain.model.VoiceCommandState
import com.example.aiassistant.presentation.ui.voice.components.VoiceWaveform
import com.example.aiassistant.presentation.ui.voice.components.VoiceCommandCard
import com.example.aiassistant.presentation.ui.voice.components.QuickCommandChip
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay

/**
 * VoiceCommandScreen - Voice-first interface for AI task execution
 * Features:
 * - Animated voice waveform visualization
 * - Voice recognition with real-time feedback
 * - Quick voice commands for common tasks
 * - Voice command history with smart suggestions
 * - Futuristic voice-activated UI
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCommandScreen(
    onTaskExecute: (Task) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavigateToTasks: () -> Unit = {}
) {
    // Voice command state management
    var voiceState by remember { mutableStateOf(VoiceCommandState.IDLE) }
    var recognizedText by remember { mutableStateOf("") }
    var confidenceLevel by remember { mutableStateOf(0f) }
    var voiceCommands by remember { mutableStateOf(listOf<VoiceCommand>()) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    
    // Voice activation simulation
    var isListening by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
        // Load recent voice commands (mock data)
        voiceCommands = VoiceCommand.getMockCommands()
    }
    
    // Handle voice state changes
    LaunchedEffect(isListening) {
        if (isListening) {
            voiceState = VoiceCommandState.LISTENING
            // Simulate voice recognition process
            delay(500)
            voiceState = VoiceCommandState.PROCESSING
            delay(2000)
            // Simulate recognition result
            recognizedText = "Send email to John about the meeting"
            confidenceLevel = 0.95f
            voiceState = VoiceCommandState.RECOGNIZED
            delay(3000)
            // Reset state
            voiceState = VoiceCommandState.IDLE
            isListening = false
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
        // Voice header with back button
        VoiceHeader(
            isVisible = isVisible,
            onBackClick = onBackClick
        )
        
        // Main voice interface
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Central voice activation area
            item {
                VoiceActivationCenter(
                    voiceState = voiceState,
                    recognizedText = recognizedText,
                    confidenceLevel = confidenceLevel,
                    isVisible = isVisible,
                    onVoiceActivate = { isListening = !isListening }
                )
            }
            
            // Quick voice commands
            item {
                QuickCommandsSection(
                    isVisible = isVisible,
                    onCommandSelect = { command ->
                        recognizedText = command
                        voiceState = VoiceCommandState.RECOGNIZED
                    }
                )
            }
            
            // Voice command history
            item {
                VoiceHistorySection(
                    commands = voiceCommands,
                    isVisible = isVisible,
                    onCommandReplay = { command ->
                        recognizedText = command.text
                        voiceState = VoiceCommandState.RECOGNIZED
                    }
                )
            }
            
            // Voice tips and help
            item {
                VoiceTipsSection(
                    isVisible = isVisible,
                    onNavigateToTasks = onNavigateToTasks
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
 * VoiceHeader - Header with back button and voice status
 */
@Composable
private fun VoiceHeader(
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
            
            // Voice status info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Voice Commands",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Text(
                    text = "Speak naturally to your AI",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            // Voice indicator
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
                    text = "🎤",
                    fontSize = 20.sp
                )
            }
        }
    }
}

/**
 * VoiceActivationCenter - Main voice interaction area
 */
@Composable
private fun VoiceActivationCenter(
    voiceState: VoiceCommandState,
    recognizedText: String,
    confidenceLevel: Float,
    isVisible: Boolean,
    onVoiceActivate: () -> Unit
) {
    val centerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(800, delayMillis = 200),
        label = "center_alpha"
    )
    
    val centerScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "center_scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(centerAlpha)
            .scale(centerScale),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Voice state indicator
            Text(
                text = getVoiceStateTitle(voiceState),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = getVoiceStateColor(voiceState),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Voice waveform visualization
            VoiceWaveform(
                isActive = voiceState == VoiceCommandState.LISTENING,
                amplitude = if (voiceState == VoiceCommandState.LISTENING) 1f else 0.3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 16.dp)
            )
            
            // Voice activation button
            VoiceActivationButton(
                voiceState = voiceState,
                onClick = onVoiceActivate,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            
            // Recognition result
            if (recognizedText.isNotEmpty() && voiceState == VoiceCommandState.RECOGNIZED) {
                RecognitionResult(
                    text = recognizedText,
                    confidence = confidenceLevel,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            // Voice instruction
            Text(
                text = getVoiceInstruction(voiceState),
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * VoiceActivationButton - Main voice control button
 */
@Composable
private fun VoiceActivationButton(
    voiceState: VoiceCommandState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = getVoiceStateColor(voiceState)
    val buttonScale by animateFloatAsState(
        targetValue = when (voiceState) {
            VoiceCommandState.LISTENING -> 1.1f
            VoiceCommandState.PROCESSING -> 0.95f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "button_scale"
    )
    
    Button(
        onClick = onClick,
        modifier = modifier
            .size(80.dp)
            .scale(buttonScale),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor.copy(alpha = 0.2f),
            contentColor = buttonColor
        ),
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        )
    ) {
        Text(
            text = getVoiceButtonIcon(voiceState),
            fontSize = 32.sp
        )
    }
}

/**
 * RecognitionResult - Shows recognized text with confidence
 */
@Composable
private fun RecognitionResult(
    text: String,
    confidence: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NeonGreen.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Confidence indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎯 Recognized",
                    fontSize = 14.sp,
                    color = NeonGreen,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "${(confidence * 100).toInt()}% confident",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Recognized text
            Text(
                text = "\"$text\"",
                fontSize = 16.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Execute button
                Button(
                    onClick = { /* Execute voice command */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonGreen,
                        contentColor = DarkBackground
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "✓ Execute", fontSize = 12.sp)
                }
                
                // Try again button
                OutlinedButton(
                    onClick = { /* Try voice recognition again */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextSecondary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "🔄 Retry", fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * QuickCommandsSection - Pre-defined quick voice commands
 */
@Composable
private fun QuickCommandsSection(
    isVisible: Boolean,
    onCommandSelect: (String) -> Unit
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "quick_commands_alpha"
    )
    
    Column(
        modifier = Modifier.alpha(sectionAlpha)
    ) {
        // Section header
        Text(
            text = "⚡ Quick Commands",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = ElectricBlue,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Quick command chips
        val quickCommands = listOf(
            "Send an email" to "📧",
            "Create a report" to "📊", 
            "Schedule a meeting" to "📅",
            "Summarize this document" to "📄",
            "Write a blog post" to "✍️",
            "Set a reminder" to "⏰"
        )
        
        // Grid layout for quick commands
        val chunkedCommands = quickCommands.chunked(2)
        chunkedCommands.forEach { rowCommands ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCommands.forEach { (command, icon) ->
                    QuickCommandChip(
                        text = command,
                        icon = icon,
                        onClick = { onCommandSelect(command) },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Fill remaining space if odd number
                if (rowCommands.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * VoiceHistorySection - Recent voice commands history
 */
@Composable
private fun VoiceHistorySection(
    commands: List<VoiceCommand>,
    isVisible: Boolean,
    onCommandReplay: (VoiceCommand) -> Unit
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 600),
        label = "history_alpha"
    )
    
    Column(
        modifier = Modifier.alpha(sectionAlpha)
    ) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🕒 Recent Commands",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeonPurple
            )
            
            Surface(
                color = MediumSurface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${commands.size} commands",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        
        // Command history cards
        commands.take(3).forEach { command ->
            VoiceCommandCard(
                command = command,
                onClick = { onCommandReplay(command) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
        
        // View all button
        if (commands.size > 3) {
            TextButton(
                onClick = { /* Navigate to full history */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "View all ${commands.size} commands →",
                    color = ElectricBlue,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * VoiceTipsSection - Help and tips for voice commands
 */
@Composable
private fun VoiceTipsSection(
    isVisible: Boolean,
    onNavigateToTasks: () -> Unit
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 800),
        label = "tips_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(sectionAlpha),
        colors = CardDefaults.cardColors(
            containerColor = AccentOrange.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Tips header
            Text(
                text = "💡 Voice Tips",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentOrange,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Tips list
            val tips = listOf(
                "Speak clearly and at normal pace",
                "Use specific task names for better recognition",
                "Try \"Create report\" or \"Send email to [name]\"",
                "Tap the microphone to start listening"
            )
            
            tips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = AccentOrange,
                        modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                    )
                    
                    Text(
                        text = tip,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
            
            // Browse tasks button
            Spacer(modifier = Modifier.height(12.dp))
            
            TextButton(
                onClick = onNavigateToTasks,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "📋 Browse All Tasks →",
                    color = AccentOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Helper functions for voice state management
 */
private fun getVoiceStateTitle(state: VoiceCommandState): String {
    return when (state) {
        VoiceCommandState.IDLE -> "🎤 Ready to Listen"
        VoiceCommandState.LISTENING -> "👂 Listening..."
        VoiceCommandState.PROCESSING -> "🧠 Processing..."
        VoiceCommandState.RECOGNIZED -> "✅ Command Recognized"
        VoiceCommandState.ERROR -> "❌ Recognition Error"
    }
}

private fun getVoiceStateColor(state: VoiceCommandState): Color {
    return when (state) {
        VoiceCommandState.IDLE -> ElectricBlue
        VoiceCommandState.LISTENING -> NeonGreen
        VoiceCommandState.PROCESSING -> NeonPurple
        VoiceCommandState.RECOGNIZED -> NeonGreen
        VoiceCommandState.ERROR -> AccentRed
    }
}

private fun getVoiceButtonIcon(state: VoiceCommandState): String {
    return when (state) {
        VoiceCommandState.IDLE -> "🎤"
        VoiceCommandState.LISTENING -> "🔴"
        VoiceCommandState.PROCESSING -> "⏳"
        VoiceCommandState.RECOGNIZED -> "✅"
        VoiceCommandState.ERROR -> "❌"
    }
}

private fun getVoiceInstruction(state: VoiceCommandState): String {
    return when (state) {
        VoiceCommandState.IDLE -> "Tap the microphone and speak your command"
        VoiceCommandState.LISTENING -> "Speak now... I'm listening"
        VoiceCommandState.PROCESSING -> "Processing your voice command..."
        VoiceCommandState.RECOGNIZED -> "Command ready to execute"
        VoiceCommandState.ERROR -> "Please try speaking again"
    }
}
