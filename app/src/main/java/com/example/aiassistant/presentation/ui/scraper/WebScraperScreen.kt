package com.example.aiassistant.presentation.ui.scraper

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiassistant.domain.model.*
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/**
 * WebScraperScreen - Module 5: Advanced web scraping interface
 * Features:
 * - Create and manage scraping jobs
 * - Real-time monitoring and status
 * - n8n webhook integration
 * - Data filtering and processing
 * - Smart scheduling and automation
 * - Visual data preview
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScraperScreen(
    onBackClick: () -> Unit = {},
    onJobClick: (ScrapeJob) -> Unit = {},
    onCreateJobClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onExecuteTask: (String, Map<String, Any>) -> Unit = { _, _ -> } // Add task execution callback
) {
    // Screen state
    var scrapeJobs by remember { mutableStateOf(WebScraperSampleData.getSampleScrapeJobs()) }
    var selectedCategory by remember { mutableStateOf<ScrapeCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }
    var isMonitoring by remember { mutableStateOf(false) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    LaunchedEffect(Unit) {
        isVisible = true
        // Simulate real-time monitoring
        isMonitoring = true
    }
    
    // Filter jobs based on search and category
    val filteredJobs = remember(scrapeJobs, selectedCategory, searchQuery) {
        scrapeJobs.filter { job ->
            val matchesCategory = selectedCategory == null || job.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() || 
                job.name.contains(searchQuery, ignoreCase = true) ||
                job.url.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
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
        // Header with search and controls
        ScraperHeader(
            isVisible = isVisible,
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            onBackClick = onBackClick,
            onCreateClick = { showCreateDialog = true },
            onSettingsClick = onSettingsClick,
            isMonitoring = isMonitoring,
            onMonitorToggle = { isMonitoring = !isMonitoring }
        )
        
        // Statistics dashboard
        ScraperStats(
            jobs = scrapeJobs,
            isVisible = isVisible
        )
        
        // Category filter
        CategoryFilter(
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = if (selectedCategory == it) null else it },
            isVisible = isVisible
        )
        
        // Jobs list
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(filteredJobs) { index, job ->
                val itemAlpha by animateFloatAsState(
                    targetValue = if (isVisible) 1f else 0f,
                    animationSpec = tween(500, delayMillis = index * 100),
                    label = "job_alpha"
                )
                
                ScrapeJobCard(
                    job = job,
                    onClick = { onJobClick(job) },
                    onToggleActive = { 
                        scrapeJobs = scrapeJobs.map { 
                            if (it.id == job.id) it.copy(isActive = !it.isActive) else it 
                        }
                    },
                    onRunNow = {
                        // Trigger immediate scrape
                        scrapeJobs = scrapeJobs.map { 
                            if (it.id == job.id) it.copy(status = ScrapeStatus.RUNNING) else it 
                        }
                    },
                    modifier = Modifier.alpha(itemAlpha)
                )
            }
            
            // Empty state
            if (filteredJobs.isEmpty()) {
                item {
                    EmptyState(
                        isVisible = isVisible,
                        onCreateClick = { showCreateDialog = true }
                    )
                }
            }
        }
    }        // Create job dialog
        if (showCreateDialog) {
            CreateJobDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { newJob ->
                    scrapeJobs = scrapeJobs + newJob
                    showCreateDialog = false
                },
                onExecuteTask = onExecuteTask
            )
        }
}

/**
 * ScraperHeader - Header with search, controls, and monitoring status
 */
@Composable
private fun ScraperHeader(
    isVisible: Boolean,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isMonitoring: Boolean,
    onMonitorToggle: () -> Unit
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
            // Top row with navigation and actions
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
                
                // Title and monitoring status
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🕷️",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        Text(
                            text = "Web Scraper",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    
                    // Monitoring indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        val pulseAlpha by rememberInfiniteTransition(label = "pulse").animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "pulse_alpha"
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (isMonitoring) NeonGreen.copy(alpha = pulseAlpha) else TextTertiary,
                                    shape = CircleShape
                                )
                        )
                        
                        Text(
                            text = if (isMonitoring) "Monitoring Active" else "Monitoring Paused",
                            fontSize = 12.sp,
                            color = if (isMonitoring) NeonGreen else TextTertiary,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
                
                // Action buttons
                Row {
                    // Monitor toggle
                    IconButton(
                        onClick = onMonitorToggle,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (isMonitoring) NeonGreen.copy(alpha = 0.2f) else MediumSurface,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = if (isMonitoring) "⏸️" else "▶️",
                            fontSize = 16.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Settings
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MediumSurface,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = "⚙️",
                            fontSize = 16.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Create job
                    IconButton(
                        onClick = onCreateClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = ElectricBlue.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = "+",
                            fontSize = 20.sp,
                            color = ElectricBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search scraping jobs...",
                        color = TextTertiary
                    )
                },
                leadingIcon = {
                    Text(
                        text = "🔍",
                        fontSize = 18.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricBlue,
                    unfocusedBorderColor = MediumSurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = ElectricBlue
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }
    }
}

/**
 * ScraperStats - Statistics dashboard
 */
@Composable
private fun ScraperStats(
    jobs: List<ScrapeJob>,
    isVisible: Boolean
) {
    val statsAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 300),
        label = "stats_alpha"
    )
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(statsAlpha),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Total jobs
        item {
            StatCard(
                icon = "📊",
                title = "Total Jobs",
                value = jobs.size.toString(),
                color = ElectricBlue
            )
        }
        
        // Active jobs
        item {
            StatCard(
                icon = "🟢",
                title = "Active",
                value = jobs.count { it.isActive }.toString(),
                color = NeonGreen
            )
        }
        
        // Running jobs
        item {
            StatCard(
                icon = "⚡",
                title = "Running",
                value = jobs.count { it.status == ScrapeStatus.RUNNING }.toString(),
                color = AccentOrange
            )
        }
        
        // Success rate
        item {
            val successRate = if (jobs.isNotEmpty()) {
                (jobs.count { it.status == ScrapeStatus.SUCCESS } * 100) / jobs.size
            } else 0
            
            StatCard(
                icon = "✅",
                title = "Success Rate",
                value = "$successRate%",
                color = NeonGreen
            )
        }
    }
}

/**
 * StatCard - Individual statistic card
 */
@Composable
private fun StatCard(
    icon: String,
    title: String,
    value: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .widthIn(min = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = title,
                fontSize = 10.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * CategoryFilter - Filter by scrape category
 */
@Composable
private fun CategoryFilter(
    selectedCategory: ScrapeCategory?,
    onCategorySelect: (ScrapeCategory) -> Unit,
    isVisible: Boolean
) {
    val filterAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "filter_alpha"
    )
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .alpha(filterAlpha),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ScrapeCategory.values()) { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelect(category) }
            )
        }
    }
}

/**
 * CategoryChip - Individual category filter chip
 */
@Composable
private fun CategoryChip(
    category: ScrapeCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) category.color.copy(alpha = 0.3f) else LightSurface.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "bg_color"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) category.color else TextSecondary,
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
                text = category.icon,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 6.dp)
            )
            
            Text(
                text = category.displayName,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}

/**
 * ScrapeJobCard - Individual scraping job card
 */
@Composable
private fun ScrapeJobCard(
    job: ScrapeJob,
    onClick: () -> Unit,
    onToggleActive: () -> Unit,
    onRunNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = LightSurface.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.category.icon,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    Column {
                        Text(
                            text = job.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            text = job.url,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                
                // Status indicator
                StatusBadge(status = job.status)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Job details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Frequency: ${job.frequency.displayName}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    
                    if (job.lastRun != null) {
                        Text(
                            text = "Last run: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(job.lastRun))}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    
                    Text(
                        text = "Data: ${job.extractedData.size} items",
                        fontSize = 11.sp,
                        color = job.category.color
                    )
                }
                
                // Action buttons
                Row {
                    // Toggle active
                    IconButton(
                        onClick = onToggleActive,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = if (job.isActive) NeonGreen.copy(alpha = 0.2f) else MediumSurface,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = if (job.isActive) "⏸️" else "▶️",
                            fontSize = 12.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Run now
                    IconButton(
                        onClick = onRunNow,
                        enabled = job.isActive && job.status != ScrapeStatus.RUNNING,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = if (job.isActive && job.status != ScrapeStatus.RUNNING) 
                                    ElectricBlue.copy(alpha = 0.2f) else MediumSurface,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = "🚀",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * StatusBadge - Status indicator badge
 */
@Composable
private fun StatusBadge(status: ScrapeStatus) {
    Surface(
        color = status.color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = status.displayName,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = status.color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * EmptyState - Shown when no jobs are available
 */
@Composable
private fun EmptyState(
    isVisible: Boolean,
    onCreateClick: () -> Unit
) {
    val emptyAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 500),
        label = "empty_alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .alpha(emptyAlpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🕸️",
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "No Scraping Jobs",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Create your first web scraping job to start collecting data automatically",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        
        Button(
            onClick = onCreateClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricBlue.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "+ Create Scraping Job",
                color = ElectricBlue,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * CreateJobDialog - Dialog for creating new scraping jobs
 */
@Composable
private fun CreateJobDialog(
    onDismiss: () -> Unit,
    onConfirm: (ScrapeJob) -> Unit,
    onExecuteTask: (String, Map<String, Any>) -> Unit
) {
    var jobName by remember { mutableStateOf("") }
    var jobUrl by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ScrapeCategory.GENERAL) }
    var selectedFrequency by remember { mutableStateOf(ScrapeFrequency.DAILY) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🕷️",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Create Scraping Job",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Job name
                OutlinedTextField(
                    value = jobName,
                    onValueChange = { jobName = it },
                    label = { Text("Job Name", color = TextSecondary) },
                    placeholder = { Text("Enter job name...", color = TextTertiary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = MediumSurface,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // URL
                OutlinedTextField(
                    value = jobUrl,
                    onValueChange = { jobUrl = it },
                    label = { Text("Website URL", color = TextSecondary) },
                    placeholder = { Text("https://example.com", color = TextTertiary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = MediumSurface,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Category selection
                Text(
                    text = "Category",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ScrapeCategory.values()) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
                
                // Frequency selection
                Text(
                    text = "Run Frequency",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ScrapeFrequency.values()) { frequency ->
                        Surface(
                            onClick = { selectedFrequency = frequency },
                            color = if (selectedFrequency == frequency) 
                                ElectricBlue.copy(alpha = 0.3f) else LightSurface.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = frequency.icon,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = frequency.displayName,
                                    fontSize = 11.sp,
                                    color = if (selectedFrequency == frequency) ElectricBlue else TextSecondary,
                                    fontWeight = if (selectedFrequency == frequency) FontWeight.SemiBold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (jobName.isNotBlank() && jobUrl.isNotBlank()) {
                        val newJob = ScrapeJob(
                            id = "scrape_${System.currentTimeMillis()}",
                            name = jobName,
                            url = jobUrl,
                            category = selectedCategory,
                            frequency = selectedFrequency,
                            status = ScrapeStatus.PENDING,
                            isActive = true
                        )
                        
                        // Execute the web scraping task
                        val taskParameters = mapOf(
                            "url" to jobUrl,
                            "jobName" to jobName,
                            "category" to selectedCategory.name.lowercase(),
                            "extractionType" to when (selectedCategory) {
                                ScrapeCategory.NEWS -> "news"
                                ScrapeCategory.ECOMMERCE -> "product"
                                ScrapeCategory.RESEARCH -> "research"
                                else -> "general"
                            }
                        )
                        
                        onExecuteTask("scrape_url", taskParameters)
                        onConfirm(newJob)
                    }
                },
                enabled = jobName.isNotBlank() && jobUrl.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricBlue.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create & Execute Job", color = ElectricBlue, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TextSecondary
                )
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
