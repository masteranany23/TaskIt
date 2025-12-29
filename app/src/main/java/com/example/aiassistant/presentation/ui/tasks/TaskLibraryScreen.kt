package com.example.aiassistant.presentation.ui.tasks

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.example.aiassistant.domain.model.TaskCategory
import com.example.aiassistant.presentation.ui.tasks.components.PopularTaskCard
import com.example.aiassistant.presentation.ui.tasks.components.TaskItem
import com.example.aiassistant.presentation.ui.tasks.components.PulseDot
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay

/**
 * TaskLibraryScreen - Main screen showing all available AI tasks
 * Features:
 * - Animated category cards with gradients
 * - Popular tasks section
 * - Smooth scroll animations
 * - Interactive task cards with hover effects
 * - Navigation to task details, search, and settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskLibraryScreen(
    onTaskClick: (Task) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onVoiceClick: () -> Unit = {},
    onSchedulerClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onScraperClick: () -> Unit = {},
    onDocumentHubClick: () -> Unit = {}
) {
    // State management
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = remember { TaskCategories.getAllCategories() }
    val popularTasks = remember { TaskCategories.getPopularTasks() }
    
    // Scroll state for animation triggers
    val scrollState = rememberLazyListState()
    
    // Animated appearance of elements
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
            // Header Section with animated title and navigation
            HeaderSection(
                isVisible = isVisible,
                onSearchClick = onSearchClick,
                onSettingsClick = onSettingsClick,
                onVoiceClick = onVoiceClick,
                onSchedulerClick = onSchedulerClick,
                onChatClick = onChatClick,
                onScraperClick = onScraperClick,
                onDocumentHubClick = onDocumentHubClick
            )
            
            // Main content with lazy column
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // Popular Tasks Section
                item {
                    PopularTasksSection(
                        tasks = popularTasks,
                        isVisible = isVisible,
                        onTaskClick = onTaskClick
                    )
                }
                
                // Categories Section
                item {
                    CategoriesHeaderSection(isVisible = isVisible)
                }
                
                // Category Cards
                itemsIndexed(categories) { index, category ->
                    CategoryCard(
                        category = category,
                        isSelected = selectedCategory == category.id,
                        onClick = { 
                            selectedCategory = if (selectedCategory == category.id) null else category.id
                        },
                        onTaskClick = onTaskClick,
                        animationDelay = index * 100L,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
                
                // Bottom padding for last item
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
        
        // Floating Voice Action Button
        VoiceFloatingActionButton(
            isVisible = isVisible,
            onClick = onVoiceClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }
}

/**
 * HeaderSection - Modern futuristic header with compact design
 */
@Composable
private fun HeaderSection(
    isVisible: Boolean,
    onSearchClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSchedulerClick: () -> Unit,
    onChatClick: () -> Unit,
    onScraperClick: () -> Unit,
    onDocumentHubClick: () -> Unit
) {
    val headerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "header_alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(headerAlpha)
    ) {
        // Compact Top App Bar
        Surface(
            color = DarkSurface.copy(alpha = 0.95f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App name with futuristic style
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Animated logo icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(ElectricBlue, ElectricBlue.copy(alpha = 0.6f))
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚡",
                            fontSize = 18.sp,
                            color = DarkBackground
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "TaskIt",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricBlue,
                        letterSpacing = 1.sp
                    )
                }
                
                // Action buttons row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search button
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MediumSurface.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(text = "🔍", fontSize = 16.sp)
                    }
                    
                    // Settings button
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MediumSurface.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(text = "⚙️", fontSize = 16.sp)
                    }
                }
            }
        }
        
        // Compact Module Grid - 2 rows of 3 cards each
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // First row - 3 compact cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactModuleCard(
                    title = "Voice AI",
                    icon = "🎤",
                    color = ElectricBlue,
                    onClick = onVoiceClick,
                    modifier = Modifier.weight(1f)
                )
                
                CompactModuleCard(
                    title = "AI Chat",
                    icon = "💬",
                    color = NeonPurple,
                    onClick = onChatClick,
                    modifier = Modifier.weight(1f)
                )
                
                CompactModuleCard(
                    title = "Scheduler",
                    icon = "📅",
                    color = NeonGreen,
                    onClick = onSchedulerClick,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Second row - 3 compact cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactModuleCard(
                    title = "Web Scraper",
                    icon = "🕷️",
                    color = AccentBlue,
                    onClick = onScraperClick,
                    modifier = Modifier.weight(1f)
                )
                
                CompactModuleCard(
                    title = "Documents",
                    icon = "📚",
                    color = AccentPurple,
                    onClick = onDocumentHubClick,
                    modifier = Modifier.weight(1f)
                )
                
                ComingSoonCard(
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Elegant separator line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 40.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            ElectricBlue.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

/**
 * PopularTasksSection - Clean horizontal scrolling popular tasks
 */
@Composable
private fun PopularTasksSection(
    tasks: List<Task>,
    isVisible: Boolean,
    onTaskClick: (Task) -> Unit
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "section_alpha"
    )
    
    Column(
        modifier = Modifier
            .alpha(sectionAlpha)
            .padding(horizontal = 20.dp)
    ) {
        // Clean section header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "Popular Tasks",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Simple badge
            Surface(
                color = NeonGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "🔥 ${tasks.size}",
                    fontSize = 12.sp,
                    color = NeonGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        
        // Horizontal scrolling task cards with better spacing
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            itemsIndexed(tasks) { index, task ->
                PopularTaskCard(
                    task = task,
                    animationDelay = index * 100L,
                    onClick = { onTaskClick(task) }
                )
            }
        }
    }
}

/**
 * CategoriesHeaderSection - Clean section header for categories
 */
@Composable
private fun CategoriesHeaderSection(isVisible: Boolean) {
    val headerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 800),
        label = "header_alpha"
    )
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .alpha(headerAlpha)
    ) {
        Text(
            text = "All Categories",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Modern category count badge
        Surface(
            color = ElectricBlue.copy(alpha = 0.2f),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "${TaskCategories.getAllCategories().size} categories",
                color = ElectricBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}

/**
 * CategoryCard - Animated category card with gradient background
 */
@Composable
private fun CategoryCard(
    category: TaskCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    onTaskClick: (Task) -> Unit,
    animationDelay: Long,
    modifier: Modifier = Modifier
) {
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )
    
    val cardOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 50.dp,
        animationSpec = tween(600, delayMillis = animationDelay.toInt()),
        label = "card_offset"
    )
    
    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = animationDelay.toInt()),
        label = "card_alpha"
    )
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Card(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .offset(y = cardOffset)
            .alpha(cardAlpha),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 12.dp else 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = category.gradientColors.map { 
                            it.copy(alpha = if (isSelected) 0.3f else 0.2f) 
                        },
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = category.color.copy(alpha = if (isSelected) 0.8f else 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                // Category header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Category icon with glow effect
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = category.color.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.icon,
                            fontSize = 24.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        
                        Text(
                            text = category.description,
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    
                    // Task count badge
                    Surface(
                        color = category.color.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${category.tasks.size}",
                            color = category.color,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                // Expandable tasks section
                AnimatedVisibility(
                    visible = isSelected,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        category.tasks.forEach { task ->
                            TaskItem(
                                task = task,
                                categoryColor = category.color,
                                onClick = { onTaskClick(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * ModuleCard - Compact navigation card for main app modules
 */
@Composable
private fun ModuleCard(
    title: String,
    subtitle: String,
    icon: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "module_scale"
    )
    
    Card(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier.scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Text(
                text = icon,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Title
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
            
            // Subtitle
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
    
    // Reset pressed state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

/**
 * FeaturedModuleCard - Larger cards for main features
 */
@Composable
private fun FeaturedModuleCard(
    title: String,
    subtitle: String,
    icon: String,
    color: Color,
    onClick: () -> Unit,
    isFeatured: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "featured_scale"
    )
    
    Card(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .scale(scale)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            color.copy(alpha = 0.2f),
                            color.copy(alpha = 0.05f)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Icon with glow effect
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    color.copy(alpha = 0.3f),
                                    color.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 24.sp
                    )
                }
                
                // Text content
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
    
    // Reset pressed state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

/**
 * CompactModuleCard - Smaller cards for secondary features
 */
@Composable
private fun CompactModuleCard(
    title: String,
    icon: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "compact_scale"
    )
    
    Card(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .scale(scale)
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MediumSurface.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Icon
                Text(
                    text = icon,
                    fontSize = 20.sp
                )
                
                // Title
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = color,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
    
    // Reset pressed state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

/**
 * ComingSoonCard - Placeholder for future modules
 */
@Composable
private fun ComingSoonCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MediumSurface.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = TextTertiary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "➕",
                    fontSize = 18.sp,
                    color = TextTertiary.copy(alpha = 0.6f)
                )
                Text(
                    text = "Coming Soon",
                    fontSize = 9.sp,
                    color = TextTertiary.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * VoiceFloatingActionButton - Floating action button for quick voice access
 */
@Composable
private fun VoiceFloatingActionButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fab_scale"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isVisible) 0f else 180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fab_rotation"
    )
    
    // Pulsing effect for attention
    val infiniteTransition = rememberInfiniteTransition(label = "fab_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .scale(scale * pulseScale)
            .rotate(rotation),
        containerColor = ElectricBlue,
        contentColor = DarkBackground,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 12.dp,
            pressedElevation = 16.dp
        )
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ElectricBlue,
                            ElectricBlueVariant
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🎤",
                fontSize = 24.sp
            )
        }
    }
}
