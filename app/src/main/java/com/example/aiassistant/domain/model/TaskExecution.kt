package com.example.aiassistant.domain.model

sealed class TaskExecutionResult {
    data class Success(
        val executionId: String,
        val result: Map<String, Any>
    ) : TaskExecutionResult()
    
    data class Loading(
        val executionId: String,
        val status: String
    ) : TaskExecutionResult()
    
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : TaskExecutionResult()
}

data class TaskExecutionRequest(
    val taskId: String,
    val taskType: TaskType,
    val parameters: Map<String, Any>,
    val userId: String? = null
)

enum class TaskType {
    EMAIL,
    REPORT,
    SUMMARY,
    TRANSLATION,
    CONTENT,
    DATA_ANALYSIS,
    CALENDAR,
    REMINDER,
    FILE_PROCESSING,
    SOCIAL_MEDIA,
    RESEARCH,
    AUTOMATION,
    OPTIMIZATION,
    MONITORING,
    BACKUP,
    BUSINESS,
    DESIGN,
    SCHEDULING,
    OTHER
}

data class TaskParameter(
    val key: String,
    val displayName: String,
    val type: ParameterType,
    val required: Boolean = true,
    val placeholder: String? = null,
    val defaultValue: String? = null,
    val options: List<String>? = null,
    val validation: ParameterValidation? = null
)

enum class ParameterType {
    TEXT,
    EMAIL,
    URL,
    NUMBER,
    BOOLEAN,
    SELECT,
    MULTILINE_TEXT,
    FILE,
    DATE,
    TIME
}

data class ParameterValidation(
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val pattern: String? = null,
    val errorMessage: String? = null
)

data class TaskFormData(
    val taskId: String,
    val parameters: List<TaskParameter>,
    val values: MutableMap<String, Any> = mutableMapOf()
)
