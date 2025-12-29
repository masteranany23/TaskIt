package com.example.aiassistant.presentation.ui.tasks

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.Task
import com.example.aiassistant.domain.model.TaskCategories
import com.example.aiassistant.domain.model.TaskDifficulty
import com.example.aiassistant.presentation.ui.tasks.components.TaskItem
import com.example.aiassistant.presentation.ui.tasks.components.DifficultyBadge
import com.example.aiassistant.presentation.ui.tasks.components.DifficultyBadgeSize
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay

/**
 * TaskSearchScreen - Advanced search and filtering for AI tasks
 * Features:
 * - Real-time search across task titles, descriptions, and tags
 * - Multi-criteria filtering (difficulty, category, tags)
 * - Search history and suggestions
 * - Animated search results with smooth transitions
 * - Empty states with helpful tips
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSearchScreen(
    onBackClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {}
) {
    // Search state management
    var searchQuery by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf<TaskDifficulty?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    
    // Get all tasks and filter based on search criteria
    val allTasks = remember { TaskCategories.getAllCategories().flatMap { it.tasks } }
    val allCategories = remember { TaskCategories.getAllCategories() }
    
    val filteredTasks = remember(searchQuery, selectedDifficulty, selectedCategory) {
        allTasks.filter { task ->
            val matchesSearch = if (searchQuery.isBlank()) true else {
                task.title.contains(searchQuery, ignoreCase = true) ||
                task.description.contains(searchQuery, ignoreCase = true) ||
                task.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }
            
            val matchesDifficulty = selectedDifficulty?.let { task.difficulty == it } ?: true
            
            val matchesCategory = selectedCategory?.let { categoryId ->
                allCategories.any { category -> 
                    category.id == categoryId && category.tasks.contains(task)
                }
            } ?: true
            
            matchesSearch && matchesDifficulty && matchesCategory
        }
    }
    
    LaunchedEffect(Unit) {
        isVisible = true
        delay(300)
        focusRequester.requestFocus()
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
        // Search Header
        SearchHeader(
            isVisible = isVisible,
            onBackClick = onBackClick
        )
        
        // Search Input and Filters
        SearchInputSection(
            searchQuery = searchQuery,
            onSearchQueryChange = { 
                searchQuery = it
                isSearchActive = it.isNotBlank()
            },
            selectedDifficulty = selectedDifficulty,
            onDifficultyChange = { selectedDifficulty = it },
            selectedCategory = selectedCategory,
            onCategoryChange = { selectedCategory = it },
            categories = allCategories,
            isVisible = isVisible,
            focusRequester = focusRequester
        )
        
        // Search Results
        SearchResultsSection(
            filteredTasks = filteredTasks,
            searchQuery = searchQuery,
            isSearchActive = isSearchActive,
            isVisible = isVisible,
            onTaskClick = onTaskClick
        )
    }
}

/**
 * SearchHeader - Header with back button and search title
 */
@Composable
private fun SearchHeader(
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
            
            // Search title
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Search Tasks",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Text(
                    text = "Find the perfect AI task for you",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            // Search icon
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
                    text = "🔍",
                    fontSize = 20.sp
                )
            }
        }
    }
}

/**
 * SearchInputSection - Search input field and filter chips
 */
@Composable
private fun SearchInputSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedDifficulty: TaskDifficulty?,
    onDifficultyChange: (TaskDifficulty?) -> Unit,
    selectedCategory: String?,
    onCategoryChange: (String?) -> Unit,
    categories: List<com.example.aiassistant.domain.model.TaskCategory>,
    isVisible: Boolean,
    focusRequester: FocusRequester
) {
    val sectionAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "input_alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .alpha(sectionAlpha)
    ) {
        // Search input field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = { 
                Text(
                    text = "Search tasks, descriptions, or tags...",
                    color = TextSecondary
                ) 
            },
            leadingIcon = {
                Text(text = "🔍", fontSize = 20.sp)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Text(text = "✕", fontSize = 16.sp, color = TextSecondary)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricBlue,
                unfocusedBorderColor = MediumSurface,
                cursorColor = ElectricBlue,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter chips section
        Text(
            text = "🎯 Filters",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Difficulty filter chips
        Text(
            text = "Difficulty:",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            item {
                FilterChip(
                    onClick = { onDifficultyChange(null) },
                    label = { Text("All", fontSize = 12.sp) },
                    selected = selectedDifficulty == null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBlue.copy(alpha = 0.2f),
                        selectedLabelColor = ElectricBlue
                    )
                )
            }
            
            items(TaskDifficulty.values()) { difficulty ->
                FilterChip(
                    onClick = { 
                        onDifficultyChange(if (selectedDifficulty == difficulty) null else difficulty)
                    },
                    label = { Text(difficulty.displayName, fontSize = 12.sp) },
                    selected = selectedDifficulty == difficulty,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = difficulty.color.copy(alpha = 0.2f),
                        selectedLabelColor = difficulty.color
                    )
                )
            }
        }
        
        // Category filter chips
        Text(
            text = "Category:",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    onClick = { onCategoryChange(null) },
                    label = { Text("All", fontSize = 12.sp) },
                    selected = selectedCategory == null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBlue.copy(alpha = 0.2f),
                        selectedLabelColor = ElectricBlue
                    )
                )
            }
            
            items(categories) { category ->
                FilterChip(
                    onClick = { 
                        onCategoryChange(if (selectedCategory == category.id) null else category.id)
                    },
                    label = { 
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(category.icon, fontSize = 12.sp)
                            Text(category.title, fontSize = 12.sp)
                        }
                    },
                    selected = selectedCategory == category.id,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = category.color.copy(alpha = 0.2f),
                        selectedLabelColor = category.color
                    )
                )
            }
        }
    }
}

/**
 * SearchResultsSection - Display filtered search results
 */
@Composable
private fun SearchResultsSection(
    filteredTasks: List<Task>,
    searchQuery: String,
    isSearchActive: Boolean,
    isVisible: Boolean,
    onTaskClick: (Task) -> Unit
) {
    val resultsAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "results_alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(resultsAlpha)
    ) {
        // Results header
        if (isSearchActive || filteredTasks.isNotEmpty()) {
            Surface(
                color = MediumSurface.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (searchQuery.isBlank()) {
                        "Showing all ${filteredTasks.size} tasks"
                    } else {
                        "Found ${filteredTasks.size} tasks matching \"$searchQuery\""
                    },
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        
        // Results list or empty state
        if (filteredTasks.isEmpty()) {
            SearchEmptyState(
                searchQuery = searchQuery,
                isSearchActive = isSearchActive
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTasks) { task ->
                    SearchTaskItem(
                        task = task,
                        searchQuery = searchQuery,
                        onClick = { onTaskClick(task) }
                    )
                }
                
                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

/**
 * SearchTaskItem - Enhanced task item for search results
 */
@Composable
private fun SearchTaskItem(
    task: Task,
    searchQuery: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Task header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                            fontSize = 18.sp
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        
                        Text(
                            text = task.estimatedTime,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
                
                // Difficulty badge
                DifficultyBadge(
                    difficulty = task.difficulty,
                    size = DifficultyBadgeSize.SMALL
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Task description
            Text(
                text = task.description,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            
            // Tags
            if (task.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(task.tags) { tag ->
                        Surface(
                            color = MediumSurface,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                fontSize = 10.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * SearchEmptyState - Empty state with helpful tips
 */
@Composable
private fun SearchEmptyState(
    searchQuery: String,
    isSearchActive: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty state icon
        Text(
            text = if (isSearchActive) "🔍" else "📋",
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Empty state title
        Text(
            text = if (isSearchActive) {
                "No tasks found"
            } else {
                "Start searching"
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Empty state description
        Text(
            text = if (isSearchActive) {
                "No tasks match \"$searchQuery\". Try adjusting your search or filters."
            } else {
                "Type in the search box above to find AI tasks by name, description, or tags."
            },
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Search tips
        if (!isSearchActive) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AccentOrange.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "💡 Search Tips",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentOrange,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    val tips = listOf(
                        "Try \"email\" to find email-related tasks",
                        "Search \"report\" for reporting tasks",
                        "Use difficulty filters to narrow results",
                        "Browse by category for specific types"
                    )
                    
                    tips.forEach { tip ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "• ",
                                fontSize = 12.sp,
                                color = AccentOrange
                            )
                            Text(
                                text = tip,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}