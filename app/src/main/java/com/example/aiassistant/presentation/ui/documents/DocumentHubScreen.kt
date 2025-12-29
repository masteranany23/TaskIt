package com.example.aiassistant.presentation.ui.documents

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.staggeredgrid.*
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
import kotlin.math.roundToInt

/**
 * DocumentHubScreen - Module 6: Intelligent document management
 * Features:
 * - Smart document organization
 * - AI-powered search and categorization
 * - Document processing and analysis
 * - n8n workflow integration
 * - Visual document preview
 * - Collaborative features
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentHubScreen(
    onBackClick: () -> Unit = {},
    onDocumentClick: (Document) -> Unit = {},
    onUploadClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    // Screen state
    var documents by remember { mutableStateOf(DocumentHubSampleData.getSampleDocuments()) }
    var processingJobs by remember { mutableStateOf(DocumentHubSampleData.getSampleProcessingJobs()) }
    var selectedCategory by remember { mutableStateOf<DocumentCategory?>(null) }
    var selectedType by remember { mutableStateOf<DocumentType?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }
    var sortBy by remember { mutableStateOf(SortOption.RECENT) }
    var showProcessingPanel by remember { mutableStateOf(false) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    // Filter and sort documents
    val filteredDocuments = remember(documents, selectedCategory, selectedType, searchQuery, sortBy) {
        documents
            .filter { doc ->
                val matchesCategory = selectedCategory == null || doc.category == selectedCategory
                val matchesType = selectedType == null || doc.type == selectedType
                val matchesSearch = searchQuery.isEmpty() || 
                    doc.title.contains(searchQuery, ignoreCase = true) ||
                    doc.content.contains(searchQuery, ignoreCase = true) ||
                    doc.tags.any { it.contains(searchQuery, ignoreCase = true) }
                matchesCategory && matchesType && matchesSearch
            }
            .sortedWith(
                when (sortBy) {
                    SortOption.RECENT -> compareByDescending { it.updatedAt }
                    SortOption.NAME -> compareBy { it.title }
                    SortOption.SIZE -> compareByDescending { it.size }
                    SortOption.TYPE -> compareBy { it.type.displayName }
                }
            )
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
        DocumentHeader(
            isVisible = isVisible,
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            onBackClick = onBackClick,
            onUploadClick = onUploadClick,
            onSettingsClick = onSettingsClick,
            viewMode = viewMode,
            onViewModeChange = { viewMode = it },
            sortBy = sortBy,
            onSortChange = { sortBy = it },
            showProcessingPanel = showProcessingPanel,
            onToggleProcessingPanel = { showProcessingPanel = !showProcessingPanel }
        )
        
        // Quick stats and processing panel
        if (showProcessingPanel) {
            ProcessingPanel(
                jobs = processingJobs,
                isVisible = isVisible,
                onJobCancel = { jobId ->
                    processingJobs = processingJobs.filter { it.id != jobId }
                }
            )
        } else {
            DocumentStats(
                documents = documents,
                isVisible = isVisible
            )
        }
        
        // Filter chips
        FilterSection(
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = if (selectedCategory == it) null else it },
            selectedType = selectedType,
            onTypeSelect = { selectedType = if (selectedType == it) null else it },
            isVisible = isVisible
        )
        
        // Documents list/grid
        when (viewMode) {
            ViewMode.LIST -> {
                DocumentList(
                    documents = filteredDocuments,
                    onDocumentClick = onDocumentClick,
                    onBookmarkToggle = { docId ->
                        documents = documents.map { 
                            if (it.id == docId) it.copy(isBookmarked = !it.isBookmarked) else it 
                        }
                    },
                    onProcessDocument = { docId, processType ->
                        val newJob = DocumentProcessingJob(
                            id = "job_${System.currentTimeMillis()}",
                            documentId = docId,
                            type = processType,
                            status = ProcessingStatus.QUEUED,
                            startTime = System.currentTimeMillis()
                        )
                        processingJobs = processingJobs + newJob
                    },
                    isVisible = isVisible
                )
            }
            ViewMode.GRID -> {
                DocumentGrid(
                    documents = filteredDocuments,
                    onDocumentClick = onDocumentClick,
                    onBookmarkToggle = { docId ->
                        documents = documents.map { 
                            if (it.id == docId) it.copy(isBookmarked = !it.isBookmarked) else it 
                        }
                    },
                    onProcessDocument = { docId, processType ->
                        val newJob = DocumentProcessingJob(
                            id = "job_${System.currentTimeMillis()}",
                            documentId = docId,
                            type = processType,
                            status = ProcessingStatus.QUEUED,
                            startTime = System.currentTimeMillis()
                        )
                        processingJobs = processingJobs + newJob
                    },
                    isVisible = isVisible
                )
            }
        }
        
        // Empty state
        if (filteredDocuments.isEmpty()) {
            EmptyDocumentState(
                isVisible = isVisible,
                hasDocuments = documents.isNotEmpty(),
                onUploadClick = onUploadClick,
                onClearFilters = {
                    selectedCategory = null
                    selectedType = null
                    searchQuery = ""
                }
            )
        }
    }
}

/**
 * View modes for documents
 */
enum class ViewMode(val displayName: String, val icon: String) {
    LIST("List", "☰"),
    GRID("Grid", "⊞")
}

/**
 * Sort options
 */
enum class SortOption(val displayName: String, val icon: String) {
    RECENT("Recent", "🕐"),
    NAME("Name", "🔤"),
    SIZE("Size", "📏"),
    TYPE("Type", "📁")
}

/**
 * DocumentHeader - Header with search, controls, and view options
 */
@Composable
private fun DocumentHeader(
    isVisible: Boolean,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onUploadClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    sortBy: SortOption,
    onSortChange: (SortOption) -> Unit,
    showProcessingPanel: Boolean,
    onToggleProcessingPanel: () -> Unit
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
                
                // Title
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📚",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        Text(
                            text = "Document Hub",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    
                    Text(
                        text = "Intelligent Document Management",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                
                // Action buttons
                Row {
                    // Processing panel toggle
                    IconButton(
                        onClick = onToggleProcessingPanel,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (showProcessingPanel) AccentPurple.copy(alpha = 0.2f) else MediumSurface,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = "⚙️",
                            fontSize = 16.sp,
                            color = if (showProcessingPanel) AccentPurple else TextSecondary
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
                    
                    // Upload
                    IconButton(
                        onClick = onUploadClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = ElectricBlue.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = "📤",
                            fontSize = 16.sp
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
                        text = "Search documents, content, tags...",
                        color = TextTertiary
                    )
                },
                leadingIcon = {
                    Text(
                        text = "🔍",
                        fontSize = 18.sp
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchChange("") }
                        ) {
                            Text(
                                text = "❌",
                                fontSize = 14.sp,
                                color = TextTertiary
                            )
                        }
                    }
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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // View controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // View mode toggle
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ViewMode.values().forEach { mode ->
                        IconButton(
                            onClick = { onViewModeChange(mode) },
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    color = if (viewMode == mode) ElectricBlue.copy(alpha = 0.3f) else MediumSurface,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = mode.icon,
                                fontSize = 14.sp,
                                color = if (viewMode == mode) ElectricBlue else TextSecondary
                            )
                        }
                    }
                }
                
                // Sort options
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(SortOption.values()) { option ->
                        SortChip(
                            option = option,
                            isSelected = sortBy == option,
                            onClick = { onSortChange(option) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * SortChip - Sort option chip
 */
@Composable
private fun SortChip(
    option: SortOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) ElectricBlue.copy(alpha = 0.3f) else LightSurface.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "bg_color"
    )
    
    Surface(
        onClick = onClick,
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option.icon,
                fontSize = 10.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
            
            Text(
                text = option.displayName,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) ElectricBlue else TextSecondary
            )
        }
    }
}

/**
 * ProcessingPanel - Shows active document processing jobs
 */
@Composable
private fun ProcessingPanel(
    jobs: List<DocumentProcessingJob>,
    isVisible: Boolean,
    onJobCancel: (String) -> Unit
) {
    val panelAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 300),
        label = "panel_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .alpha(panelAlpha),
        colors = CardDefaults.cardColors(
            containerColor = AccentPurple.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AccentPurple.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔄 Processing Jobs",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentPurple
                )
                
                Text(
                    text = "${jobs.size} active",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            if (jobs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                jobs.forEach { job ->
                    ProcessingJobItem(
                        job = job,
                        onCancel = { onJobCancel(job.id) }
                    )
                    
                    if (job != jobs.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No active processing jobs",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * ProcessingJobItem - Individual processing job item
 */
@Composable
private fun ProcessingJobItem(
    job: DocumentProcessingJob,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = job.type.displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            
            // Progress bar
            LinearProgressIndicator(
                progress = job.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = job.status.color,
                trackColor = MediumSurface
            )
            
            Text(
                text = "${(job.progress * 100).roundToInt()}% - ${job.status.displayName}",
                fontSize = 10.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        
        if (job.status == ProcessingStatus.PROCESSING || job.status == ProcessingStatus.QUEUED) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier.size(32.dp)
            ) {
                Text(
                    text = "❌",
                    fontSize = 12.sp,
                    color = AccentRed
                )
            }
        }
    }
}

/**  
 * DocumentStats - Quick statistics about documents
 */
@Composable
private fun DocumentStats(
    documents: List<Document>,
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
        // Total documents
        item {
            StatCard(
                icon = "📄",
                title = "Total",
                value = documents.size.toString(),
                color = ElectricBlue
            )
        }
        
        // Bookmarked
        item {
            StatCard(
                icon = "⭐",
                title = "Bookmarked",
                value = documents.count { it.isBookmarked }.toString(),
                color = AccentOrange
            )
        }
        
        // Shared
        item {
            StatCard(
                icon = "🔗",
                title = "Shared",
                value = documents.count { it.isShared }.toString(),
                color = NeonGreen
            )
        }
        
        // Total size
        item {
            val totalSize = documents.sumOf { it.size }
            val sizeText = when {
                totalSize > 1024 * 1024 * 1024 -> "${(totalSize / (1024 * 1024 * 1024.0)).roundToInt()}GB"
                totalSize > 1024 * 1024 -> "${(totalSize / (1024 * 1024.0)).roundToInt()}MB"
                totalSize > 1024 -> "${(totalSize / 1024.0).roundToInt()}KB"
                else -> "${totalSize}B"
            }
            
            StatCard(
                icon = "💾",
                title = "Size",
                value = sizeText,
                color = AccentPurple
            )
        }
    }
}

/**
 * StatCard - Individual statistic card (reused from WebScraperScreen)
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
 * FilterSection - Category and type filters
 */
@Composable
private fun FilterSection(
    selectedCategory: DocumentCategory?,
    onCategorySelect: (DocumentCategory) -> Unit,
    selectedType: DocumentType?,
    onTypeSelect: (DocumentType) -> Unit,
    isVisible: Boolean
) {
    val filterAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "filter_alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(filterAlpha)
    ) {
        // Categories
        Text(
            text = "Categories",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            items(DocumentCategory.values()) { category ->
                FilterChip(
                    label = category.displayName,
                    icon = category.icon,
                    color = category.color,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelect(category) }
                )
            }
        }
        
        // Types
        Text(
            text = "Types",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            items(DocumentType.values()) { type ->
                FilterChip(
                    label = type.displayName,
                    icon = type.icon,
                    color = ElectricBlue,
                    isSelected = selectedType == type,
                    onClick = { onTypeSelect(type) }
                )
            }
        }
    }
}

/**
 * FilterChip - Individual filter chip
 */
@Composable
private fun FilterChip(
    label: String,
    icon: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) color.copy(alpha = 0.3f) else LightSurface.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "bg_color"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) color else TextSecondary,
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
                text = icon,
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 6.dp)
            )
            
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}

/**
 * DocumentList - List view of documents
 */
@Composable
private fun DocumentList(
    documents: List<Document>,
    onDocumentClick: (Document) -> Unit,
    onBookmarkToggle: (String) -> Unit,
    onProcessDocument: (String, ProcessingType) -> Unit,
    isVisible: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(documents) { index, document ->
            val itemAlpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(500, delayMillis = index * 50),
                label = "doc_alpha"
            )
            
            DocumentListItem(
                document = document,
                onClick = { onDocumentClick(document) },
                onBookmarkToggle = { onBookmarkToggle(document.id) },
                onProcessDocument = { processType -> onProcessDocument(document.id, processType) },
                modifier = Modifier.alpha(itemAlpha)
            )
        }
    }
}

/**
 * DocumentGrid - Grid view of documents
 */
@Composable
private fun DocumentGrid(
    documents: List<Document>,
    onDocumentClick: (Document) -> Unit,
    onBookmarkToggle: (String) -> Unit,
    onProcessDocument: (String, ProcessingType) -> Unit,
    isVisible: Boolean
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp
    ) {
        itemsIndexed(documents) { index, document ->
            val itemAlpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(500, delayMillis = index * 50),
                label = "doc_alpha"
            )
            
            DocumentGridItem(
                document = document,
                onClick = { onDocumentClick(document) },
                onBookmarkToggle = { onBookmarkToggle(document.id) },
                onProcessDocument = { processType -> onProcessDocument(document.id, processType) },
                modifier = Modifier.alpha(itemAlpha)
            )
        }
    }
}

/**
 * DocumentListItem - Document item in list view
 */
@Composable
private fun DocumentListItem(
    document: Document,
    onClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onProcessDocument: (ProcessingType) -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Document icon and type
            Surface(
                color = document.color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = document.type.icon,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Document info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = document.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (document.aiSummary != null) {
                    Text(
                        text = document.aiSummary,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category badge
                    Surface(
                        color = document.category.color.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = document.category.displayName,
                            fontSize = 10.sp,
                            color = document.category.color,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    
                    // Size and date
                    Text(
                        text = "• ${formatFileSize(document.size)} • ${formatDate(document.updatedAt)}",
                        fontSize = 10.sp,
                        color = TextTertiary
                    )
                }
                
                // Tags
                if (document.tags.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(document.tags.take(3)) { tag ->
                            Surface(
                                color = MediumSurface,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    fontSize = 9.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // Action buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Bookmark
                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.size(32.dp)
                ) {
                    Text(
                        text = if (document.isBookmarked) "⭐" else "☆",
                        fontSize = 16.sp,
                        color = if (document.isBookmarked) AccentOrange else TextTertiary
                    )
                }
                
                // Process menu
                var showProcessMenu by remember { mutableStateOf(false) }
                
                Box {
                    IconButton(
                        onClick = { showProcessMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(
                            text = "⚙️",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showProcessMenu,
                        onDismissRequest = { showProcessMenu = false }
                    ) {
                        ProcessingType.values().forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = type.icon,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Text(
                                            text = type.displayName,
                                            fontSize = 12.sp
                                        )
                                    }
                                },
                                onClick = {
                                    onProcessDocument(type)
                                    showProcessMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * DocumentGridItem - Document item in grid view
 */
@Composable
private fun DocumentGridItem(
    document: Document,
    onClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onProcessDocument: (ProcessingType) -> Unit,
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
            modifier = Modifier.padding(12.dp)
        ) {
            // Header with icon and bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = document.color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = document.type.icon,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                
                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.size(24.dp)
                ) {
                    Text(
                        text = if (document.isBookmarked) "⭐" else "☆",
                        fontSize = 12.sp,
                        color = if (document.isBookmarked) AccentOrange else TextTertiary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Title
            Text(
                text = document.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Summary
            if (document.aiSummary != null) {
                Text(
                    text = document.aiSummary,
                    fontSize = 10.sp,
                    color = TextSecondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
                
                Spacer(modifier = Modifier.height(6.dp))
            }
            
            // Category and size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = document.category.color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = document.category.displayName,
                        fontSize = 9.sp,
                        color = document.category.color,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                
                Text(
                    text = formatFileSize(document.size),
                    fontSize = 9.sp,
                    color = TextTertiary
                )
            }
            
            // Tags
            if (document.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(document.tags.take(2)) { tag ->
                        Surface(
                            color = MediumSurface,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                fontSize = 8.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * EmptyDocumentState - Shown when no documents are available
 */
@Composable
private fun EmptyDocumentState(
    isVisible: Boolean,
    hasDocuments: Boolean,
    onUploadClick: () -> Unit,
    onClearFilters: () -> Unit
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
            text = if (hasDocuments) "🔍" else "📁",
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = if (hasDocuments) "No Matching Documents" else "No Documents Yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = if (hasDocuments) 
                "Try adjusting your search criteria or filters" 
            else 
                "Upload your first document to get started with intelligent document management",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        
        if (hasDocuments) {
            Button(
                onClick = onClearFilters,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricBlue.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Clear Filters",
                    color = ElectricBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Button(
                onClick = onUploadClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricBlue.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "📤 Upload Document",
                    color = ElectricBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Helper functions
 */
private fun formatFileSize(bytes: Long): String {
    return when {
        bytes >= 1024 * 1024 * 1024 -> "${(bytes / (1024 * 1024 * 1024.0)).roundToInt()}GB"
        bytes >= 1024 * 1024 -> "${(bytes / (1024 * 1024.0)).roundToInt()}MB"
        bytes >= 1024 -> "${(bytes / 1024.0).roundToInt()}KB"
        else -> "${bytes}B"
    }
}

private fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}m ago"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}h ago"
        diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}d ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
    }
}
