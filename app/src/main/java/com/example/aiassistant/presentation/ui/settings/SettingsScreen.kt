package com.example.aiassistant.presentation.ui.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.aiassistant.ui.theme.*

/**
 * SettingsScreen - App configuration and user preferences
 * Features:
 * - Futuristic settings interface
 * - Animated preference cards
 * - Theme customization options
 * - Account and app information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    // Settings state
    var notificationsEnabled by remember { mutableStateOf(true) }
    var voiceCommandsEnabled by remember { mutableStateOf(true) }
    var autoExecuteEnabled by remember { mutableStateOf(false) }
    var analyticsEnabled by remember { mutableStateOf(true) }
    
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
        // Settings header
        SettingsHeader(
            isVisible = isVisible,
            onBackClick = onBackClick
        )
        
        // Settings content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Profile Section
            item {
                UserProfileSection(isVisible = isVisible)
            }
            
            // Preferences Section
            item {
                PreferencesSection(
                    notificationsEnabled = notificationsEnabled,
                    voiceCommandsEnabled = voiceCommandsEnabled,
                    autoExecuteEnabled = autoExecuteEnabled,
                    analyticsEnabled = analyticsEnabled,
                    onNotificationsToggle = { notificationsEnabled = it },
                    onVoiceCommandsToggle = { voiceCommandsEnabled = it },
                    onAutoExecuteToggle = { autoExecuteEnabled = it },
                    onAnalyticsToggle = { analyticsEnabled = it },
                    isVisible = isVisible
                )
            }
            
            // App Information Section
            item {
                AppInfoSection(isVisible = isVisible)
            }
            
            // Support Section
            item {
                SupportSection(isVisible = isVisible)
            }
            
            // About Section
            item {
                AboutSection(isVisible = isVisible)
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * SettingsHeader - Header with back button and title
 */
@Composable
private fun SettingsHeader(
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
            
            // Title
            Column {
                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Text(
                    text = "Customize your AI experience",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Settings icon
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
                    text = "⚙️",
                    fontSize = 20.sp
                )
            }
        }
    }
}

/**
 * UserProfileSection - User account information
 */
@Composable
private fun UserProfileSection(isVisible: Boolean) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "profile_alpha"
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(ElectricBlue, NeonGreen)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Profile info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TaskIt User",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Text(
                    text = "Premium Account",
                    fontSize = 14.sp,
                    color = NeonGreen,
                    modifier = Modifier.padding(top = 2.dp)
                )
                
                Text(
                    text = "Active since June 2025",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // Edit button
            Surface(
                color = ElectricBlue.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                onClick = { /* Handle edit profile */ }
            ) {
                Text(
                    text = "Edit",
                    fontSize = 12.sp,
                    color = ElectricBlue,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/**
 * PreferencesSection - App preferences and toggles
 */
@Composable
private fun PreferencesSection(
    notificationsEnabled: Boolean,
    voiceCommandsEnabled: Boolean,
    autoExecuteEnabled: Boolean,
    analyticsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    onVoiceCommandsToggle: (Boolean) -> Unit,
    onAutoExecuteToggle: (Boolean) -> Unit,
    onAnalyticsToggle: (Boolean) -> Unit,
    isVisible: Boolean
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 300),
        label = "preferences_alpha"
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
            // Section title
            Text(
                text = "🎛️ Preferences",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeonPurple,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Preference items
            val preferences = listOf(
                PreferenceData("🔔", "Notifications", "Get alerts for task completion", notificationsEnabled, onNotificationsToggle),
                PreferenceData("🎤", "Voice Commands", "Enable voice task activation", voiceCommandsEnabled, onVoiceCommandsToggle),
                PreferenceData("⚡", "Auto Execute", "Run simple tasks automatically", autoExecuteEnabled, onAutoExecuteToggle),
                PreferenceData("📊", "Analytics", "Help improve the app", analyticsEnabled, onAnalyticsToggle)
            )
            
            preferences.forEachIndexed { index, preference ->
                PreferenceItem(
                    icon = preference.icon,
                    title = preference.title,
                    description = preference.description,
                    isEnabled = preference.isEnabled,
                    onToggle = preference.onToggle,
                    animationDelay = index * 100L
                )
                
                if (index < preferences.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

/**
 * PreferenceItem - Individual preference toggle
 */
@Composable
private fun PreferenceItem(
    icon: String,
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    animationDelay: Long
) {
    var isVisible by remember { mutableStateOf(false) }
    
    val itemAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(400, delayMillis = animationDelay.toInt()),
        label = "item_alpha"
    )
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(itemAlpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 12.dp)
        )
        
        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
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
        
        // Switch
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ElectricBlue,
                checkedTrackColor = ElectricBlue.copy(alpha = 0.3f),
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = MediumSurface
            )
        )
    }
}

/**
 * AppInfoSection - App version and technical info
 */
@Composable
private fun AppInfoSection(isVisible: Boolean) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "app_info_alpha"
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
            // Section title
            Text(
                text = "📱 App Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentOrange,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // App info items
            val infoItems = listOf(
                "Version" to "1.0.0",
                "Build" to "2025.06.29",
                "Platform" to "Android",
                "AI Engine" to "n8n Workflows"
            )
            
            infoItems.forEach { (label, value) ->
                InfoRow(label = label, value = value)
            }
        }
    }
}

/**
 * SupportSection - Help and support options
 */
@Composable
private fun SupportSection(isVisible: Boolean) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 500),
        label = "support_alpha"
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
            // Section title
            Text(
                text = "💬 Support",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Support options
            val supportOptions = listOf(
                Triple("📚", "Help & Tutorials", "Learn how to use TaskIt"),
                Triple("🐛", "Report Bug", "Found an issue? Let us know"),
                Triple("💡", "Feature Request", "Suggest new AI capabilities"),
                Triple("📧", "Contact Support", "Get help from our team")
            )
            
            supportOptions.forEach { (icon, title, description) ->
                SupportOption(
                    icon = icon,
                    title = title,
                    description = description,
                    onClick = { /* Handle support option */ }
                )
            }
        }
    }
}

/**
 * SupportOption - Individual support option item
 */
@Composable
private fun SupportOption(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
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
                text = icon,
                fontSize = 20.sp,
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
                color = TextTertiary
            )
        }
    }
}

/**
 * AboutSection - About the app
 */
@Composable
private fun AboutSection(isVisible: Boolean) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 600),
        label = "about_alpha"
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
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(ElectricBlue, NeonGreen)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🤖",
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // App name and description
            Text(
                text = "TaskIt",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Your Personal AI Companion",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Copyright
            Text(
                text = "© 2025 TaskIt. All rights reserved.",
                fontSize = 12.sp,
                color = TextTertiary,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * InfoRow - Reusable info row component
 */
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )
        
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Data class for preference items
 */
private data class PreferenceData(
    val icon: String,
    val title: String,
    val description: String,
    val isEnabled: Boolean,
    val onToggle: (Boolean) -> Unit
)
