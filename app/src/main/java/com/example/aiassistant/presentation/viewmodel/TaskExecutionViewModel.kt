package com.example.aiassistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiassistant.domain.model.*
import com.example.aiassistant.domain.repository.TaskExecutionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TaskExecutionViewModel - Manages task execution state and business logic
 * Features:
 * - Form validation and parameter management
 * - Task execution with progress tracking
 * - Error handling and user feedback
 * - State management for UI updates
 */
@HiltViewModel
class TaskExecutionViewModel @Inject constructor(
    private val taskExecutionRepository: TaskExecutionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskExecutionUiState())
    val uiState: StateFlow<TaskExecutionUiState> = _uiState.asStateFlow()

    private val _executionResult = MutableStateFlow<TaskExecutionResult?>(null)
    val executionResult: StateFlow<TaskExecutionResult?> = _executionResult.asStateFlow()

    private val _formData = MutableStateFlow<Map<String, String>>(emptyMap())
    val formData: StateFlow<Map<String, String>> = _formData.asStateFlow()

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()

    /**
     * Initialize the ViewModel with task parameters
     */
    fun initializeTask(taskId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val parameters = taskExecutionRepository.getTaskParameters(taskId)
                _uiState.value = _uiState.value.copy(
                    taskId = taskId,
                    parameters = parameters,
                    isLoading = false
                )
                
                // Initialize form data with default values
                val initialFormData = parameters.associate { param ->
                    param.key to (param.defaultValue ?: "")
                }
                _formData.value = initialFormData
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load task: ${e.message}"
                )
            }
        }
    }

    /**
     * Update form field value and validate
     */
    fun updateFormField(key: String, value: String) {
        _formData.value = _formData.value.toMutableMap().apply {
            put(key, value)
        }
        
        // Clear validation error for this field
        _validationErrors.value = _validationErrors.value.toMutableMap().apply {
            remove(key)
        }
        
        // Validate field if it has validation rules
        val parameter = _uiState.value.parameters.find { it.key == key }
        parameter?.validation?.let { validation ->
            val error = validateField(value, validation)
            if (error != null) {
                _validationErrors.value = _validationErrors.value.toMutableMap().apply {
                    put(key, error)
                }
            }
        }
    }

    /**
     * Validate all form fields
     */
    private fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()
        val currentFormData = _formData.value
        
        _uiState.value.parameters.forEach { parameter ->
            val value = currentFormData[parameter.key] ?: ""
            
            // Check required fields
            if (parameter.required && value.isBlank()) {
                errors[parameter.key] = "${parameter.displayName} is required"
                return@forEach
            }
            
            // Validate field with validation rules
            parameter.validation?.let { validation ->
                val error = validateField(value, validation)
                if (error != null) {
                    errors[parameter.key] = error
                }
            }
        }
        
        _validationErrors.value = errors
        return errors.isEmpty()
    }

    /**
     * Validate individual field
     */
    private fun validateField(value: String, validation: ParameterValidation): String? {
        if (value.isBlank()) return null
        
        // Check pattern
        validation.pattern?.let { pattern ->
            if (!value.matches(Regex(pattern))) {
                return validation.errorMessage ?: "Invalid format"
            }
        }
        
        // Check length constraints
        validation.minLength?.let { minLength ->
            if (value.length < minLength) {
                return "Minimum length is $minLength characters"
            }
        }
        
        validation.maxLength?.let { maxLength ->
            if (value.length > maxLength) {
                return "Maximum length is $maxLength characters"
            }
        }
        
        return null
    }

    /**
     * Execute the task with current form data
     */
    fun executeTask() {
        if (!validateForm()) {
            return
        }
        
        viewModelScope.launch {
            try {
                val taskId = _uiState.value.taskId
                val taskType = getTaskTypeFromId(taskId)
                
                val request = TaskExecutionRequest(
                    taskId = taskId,
                    taskType = taskType,
                    parameters = _formData.value
                )
                
                _uiState.value = _uiState.value.copy(isExecuting = true, error = null)
                
                taskExecutionRepository.executeTask(request).collect { result ->
                    _executionResult.value = result
                    
                    when (result) {
                        is TaskExecutionResult.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                executionProgress = 0.5f,
                                executionStatus = result.status
                            )
                        }
                        is TaskExecutionResult.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isExecuting = false,
                                executionProgress = 1.0f,
                                executionStatus = "Completed successfully"
                            )
                        }
                        is TaskExecutionResult.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isExecuting = false,
                                executionProgress = 0f,
                                error = result.message,
                                executionStatus = "Failed"
                            )
                        }
                    }
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExecuting = false,
                    executionProgress = 0f,
                    error = "Execution failed: ${e.message}",
                    executionStatus = "Failed"
                )
            }
        }
    }

    /**
     * Clear execution result and reset form
     */
    fun clearResult() {
        _executionResult.value = null
        _uiState.value = _uiState.value.copy(
            executionProgress = 0f,
            executionStatus = null,
            error = null
        )
    }

    /**
     * Get task type from task ID
     */
    private fun getTaskTypeFromId(taskId: String): TaskType {
        return when (taskId) {
            "send_email" -> TaskType.EMAIL
            "create_report", "generate_report" -> TaskType.REPORT
            "market_analysis", "business_plan", "create_invoice" -> TaskType.BUSINESS
            "set_reminder" -> TaskType.REMINDER
            "schedule_meeting" -> TaskType.CALENDAR
            "summarize_day", "summarize_video" -> TaskType.SUMMARY
            "research_topic", "create_notes" -> TaskType.RESEARCH
            "create_presentation", "write_blog" -> TaskType.CONTENT
            "design_logo" -> TaskType.DESIGN
            "social_media" -> TaskType.SOCIAL_MEDIA
            else -> TaskType.OTHER
        }
    }

    /**
     * Retry failed execution
     */
    fun retryExecution() {
        clearResult()
        executeTask()
    }
}

/**
 * UI State for TaskExecutionScreen
 */
data class TaskExecutionUiState(
    val taskId: String = "",
    val parameters: List<TaskParameter> = emptyList(),
    val isLoading: Boolean = false,
    val isExecuting: Boolean = false,
    val executionProgress: Float = 0f,
    val executionStatus: String? = null,
    val error: String? = null
)
