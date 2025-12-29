package com.example.aiassistant.presentation.ui.tasks

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aiassistant.domain.model.*
import com.example.aiassistant.presentation.viewmodel.TaskExecutionViewModel
import com.example.aiassistant.ui.theme.*
import kotlinx.coroutines.delay

/**
 * TaskExecutionScreen - Dynamic form for task parameter input and execution
 * Features:
 * - Dynamic form generation based on task parameters
 * - Real-time validation with ViewModel integration
 * - Smooth execution with progress tracking
 * - Beautiful animations and modern UI
 * - Proper error handling and user feedback
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskExecutionScreen(
    taskId: String,
    onBackClick: () -> Unit,
    viewModel: TaskExecutionViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    
    // Collect ViewModel states
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val executionResult by viewModel.executionResult.collectAsStateWithLifecycle()
    val formData by viewModel.formData.collectAsStateWithLifecycle()
    val validationErrors by viewModel.validationErrors.collectAsStateWithLifecycle()
    
    // Initialize task when screen loads
    LaunchedEffect(taskId) {
        viewModel.initializeTask(taskId)
    }
    
    // Find the task by ID
    val task = remember(taskId) {
        TaskCategories.getAllCategories()
            .flatMap { it.tasks }
            .find { it.id == taskId }
    }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    if (task == null) {
        TaskNotFoundScreen(onBackClick = onBackClick)
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(DarkBackground, MediumBackground)
                )
            )
    ) {
        // Header
        TaskExecutionHeader(
            task = task,
            isVisible = isVisible,
            onBackClick = onBackClick
        )
        
        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    LoadingView(isVisible = isVisible)
                }
                uiState.isExecuting -> {
                    ExecutionProgressView(
                        task = task,
                        progress = uiState.executionProgress,
                        status = uiState.executionStatus,
                        isVisible = isVisible
                    )
                }
                executionResult != null -> {
                    ExecutionResultView(
                        task = task,
                        result = executionResult!!,
                        isVisible = isVisible,
                        onStartNew = {
                            viewModel.clearResult()
                        },
                        onRetry = {
                            viewModel.retryExecution()
                        }
                    )
                }
                uiState.error != null -> {
                    ErrorView(
                        task = task,
                        error = uiState.error!!,
                        isVisible = isVisible,
                        onRetry = {
                            viewModel.initializeTask(taskId)
                        },
                        onBack = onBackClick
                    )
                }
                else -> {
                    DynamicTaskForm(
                        task = task,
                        parameters = uiState.parameters,
                        formData = formData,
                        validationErrors = validationErrors,
                        isVisible = isVisible,
                        onFormDataChange = { key, value ->
                            viewModel.updateFormField(key, value)
                        },
                        onExecute = {
                            keyboardController?.hide()
                            viewModel.executeTask()
                        }
                    )
                }
            }
        }
    }
}

/**
 * TaskExecutionHeader - Header with task info and back button
 */
@Composable
private fun TaskExecutionHeader(
    task: Task,
    isVisible: Boolean,
    onBackClick: () -> Unit
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
            
            // Task info
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "Configure & Execute",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

/**
 * DynamicTaskForm - Main form with dynamic parameter inputs
 */
@Composable
private fun DynamicTaskForm(
    task: Task,
    parameters: List<TaskParameter>,
    formData: Map<String, String>,
    validationErrors: Map<String, String>,
    isVisible: Boolean,
    onFormDataChange: (String, String) -> Unit,
    onExecute: () -> Unit
) {
    val formAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "form_alpha"
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .alpha(formAlpha),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Task description card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LightSurface.copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "✨ Task Overview",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ElectricBlue,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = task.description,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // Dynamic parameter inputs
        items(parameters) { parameter ->
            AnimatedParameterInput(
                parameter = parameter,
                value = formData[parameter.key] ?: "",
                error = validationErrors[parameter.key],
                onValueChange = { newValue ->
                    onFormDataChange(parameter.key, newValue)
                }
            )
        }
        
        // Execute button
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onExecute,
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
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Execute ${task.title}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * AnimatedParameterInput - Individual parameter input with animation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedParameterInput(
    parameter: TaskParameter,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        isExpanded = true
    }
    
    val inputAlpha by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(400),
        label = "input_alpha"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(inputAlpha),
        colors = CardDefaults.cardColors(
            containerColor = if (error != null) AccentRed.copy(alpha = 0.1f) else LightSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Parameter label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = parameter.displayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                
                if (parameter.required) {
                    Text(
                        text = " *",
                        fontSize = 14.sp,
                        color = AccentRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Input field based on parameter type
            when (parameter.type) {
                ParameterType.TEXT -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            parameter.placeholder?.let {
                                Text(text = it, color = TextSecondary)
                            }
                        },
                        isError = error != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
                
                ParameterType.MULTILINE_TEXT -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = {
                            parameter.placeholder?.let {
                                Text(text = it, color = TextSecondary)
                            }
                        },
                        maxLines = 5,
                        isError = error != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
                
                ParameterType.EMAIL -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            parameter.placeholder?.let {
                                Text(text = it, color = TextSecondary)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = error != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
                
                ParameterType.NUMBER -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            // Only allow numeric input
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*$"))) {
                                onValueChange(newValue)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            parameter.placeholder?.let {
                                Text(text = it, color = TextSecondary)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = error != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
                
                ParameterType.SELECT -> {
                    var expanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = value,
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            placeholder = {
                                Text(text = "Select an option", color = TextSecondary)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            isError = error != null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
                            )
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            parameter.options?.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option) },
                                    onClick = {
                                        onValueChange(option)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                ParameterType.BOOLEAN -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = value.lowercase() == "true",
                            onCheckedChange = { checked ->
                                onValueChange(checked.toString())
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ElectricBlue,
                                uncheckedColor = TextSecondary
                            )
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = parameter.placeholder ?: "Enable this option",
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    }
                }
                
                else -> {
                    // Fallback for other types
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            parameter.placeholder?.let {
                                Text(text = it, color = TextSecondary)
                            }
                        },
                        isError = error != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
            }
            
            // Error message
            error?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = AccentRed,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * ExecutionProgressView - Shows task execution progress
 */
@Composable
private fun ExecutionProgressView(
    task: Task,
    progress: Float,
    status: String?,
    isVisible: Boolean
) {
    val progressAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600),
        label = "progress_alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(progressAlpha)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Task icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = ElectricBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = task.icon,
                fontSize = 40.sp
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Executing ${task.title}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = status ?: "AI is processing your request...",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Progress indicator
        if (progress > 0) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(64.dp),
                color = ElectricBlue,
                strokeWidth = 6.dp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = ElectricBlue
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = ElectricBlue,
                strokeWidth = 6.dp
            )
        }
    }
}

/**
 * ExecutionResultView - Shows task execution results
 */
@Composable
private fun ExecutionResultView(
    task: Task,
    result: TaskExecutionResult,
    isVisible: Boolean,
    onStartNew: () -> Unit,
    onRetry: () -> Unit
) {
    val resultAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600),
        label = "result_alpha"
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .alpha(resultAlpha),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (result) {
            is TaskExecutionResult.Success -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = NeonGreen.copy(alpha = 0.1f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "✅",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Text(
                                text = "Task Completed Successfully!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreen,
                                textAlign = TextAlign.Center
                            )
                            
                            Text(
                                text = task.title,
                                fontSize = 16.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
                
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = LightSurface.copy(alpha = 0.8f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "📊 Results",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ElectricBlue,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            
                            result.result.forEach { (key, value) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = key.replaceFirstChar { it.uppercase() },
                                        fontSize = 14.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = value.toString(),
                                        fontSize = 14.sp,
                                        color = TextPrimary,
                                        modifier = Modifier.weight(2f),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Action buttons for success
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onStartNew,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricBlue,
                                contentColor = DarkBackground
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Start New Task",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            is TaskExecutionResult.Error -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = AccentRed.copy(alpha = 0.1f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "❌",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Text(
                                text = "Task Failed",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentRed,
                                textAlign = TextAlign.Center
                            )
                            
                            Text(
                                text = result.message,
                                fontSize = 14.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
                
                // Action buttons for error
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onStartNew,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, TextSecondary.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "Start New",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                        
                        Button(
                            onClick = onRetry,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Retry",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            else -> {
                // Loading state - should not happen here, but fallback
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ElectricBlue)
                    }
                }
            }
        }
    }
}


/**
 * LoadingView - Shows loading state while initializing
 */
@Composable
private fun LoadingView(
    isVisible: Boolean
) {
    val loadingAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600),
        label = "loading_alpha"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(loadingAlpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = ElectricBlue,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Loading task details...",
                fontSize = 16.sp,
                color = TextSecondary
            )
        }
    }
}

/**
 * ErrorView - Shows error state with retry options
 */
@Composable
private fun ErrorView(
    task: Task,
    error: String,
    isVisible: Boolean,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val errorAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600),
        label = "error_alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(errorAlpha)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "Something went wrong",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentRed,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = error,
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Go Back")
            }
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricBlue,
                    contentColor = DarkBackground
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Try Again")
            }
        }
    }
}

/**
 * TaskNotFoundScreen - Shows when task is not found
 */
@Composable
private fun TaskNotFoundScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🔍",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "Task Not Found",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "The requested task could not be found. Please check the task ID and try again.",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricBlue,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Go Back")
        }
    }
}


