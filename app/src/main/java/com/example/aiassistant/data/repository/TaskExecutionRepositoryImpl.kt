package com.example.aiassistant.data.repository

import com.example.aiassistant.data.remote.api.N8nApiService
import com.example.aiassistant.data.remote.dto.*
import com.example.aiassistant.domain.model.*
import com.example.aiassistant.domain.repository.TaskExecutionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskExecutionRepositoryImpl @Inject constructor(
    private val n8nApiService: N8nApiService
) : TaskExecutionRepository {

    private fun mapToSummarizeDayRequest(parameters: Map<String, Any>): SummarizeDayRequest {
        return SummarizeDayRequest(
            userEmail = parameters["userEmail"]?.toString() ?: "",
            date = parameters["date"]?.toString() ?: getCurrentDate(),
            includeSchedule = parameters["includeSchedule"]?.toString()?.toBoolean() ?: true,
            includeTasks = parameters["includeTasks"]?.toString()?.toBoolean() ?: true,
            includeEmails = parameters["includeEmails"]?.toString()?.toBoolean() ?: false,
            detailLevel = parameters["detailLevel"]?.toString() ?: "medium"
        )
    }

    private fun mapToSummarizeVideoRequest(parameters: Map<String, Any>): SummarizeVideoRequest {
        return SummarizeVideoRequest(
            youtubeUrl = parameters["youtubeUrl"]?.toString() ?: "",
            summaryType = parameters["summaryType"]?.toString() ?: "detailed",
            includeTimestamps = parameters["includeTimestamps"]?.toString()?.toBoolean() ?: false,
            language = parameters["language"]?.toString() ?: "english"
        )
    }

    private fun getCurrentDate(): String {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return java.time.LocalDate.now().format(formatter)
    }

    override suspend fun executeTask(request: TaskExecutionRequest): Flow<TaskExecutionResult> = flow {
        try {
            emit(TaskExecutionResult.Loading("", "Starting execution..."))
            
            val response = when {
                // Handle summarize_day specifically
                request.taskId == "summarize_day" -> {
                    val summarizeDayRequest = mapToSummarizeDayRequest(request.parameters)
                    n8nApiService.executeSummarizeDayTask(summarizeDayRequest)
                }
                // Handle summarize_video specifically
                request.taskId == "summarize_video" -> {
                    val summarizeVideoRequest = mapToSummarizeVideoRequest(request.parameters)
                    n8nApiService.executeSummarizeVideoTask(summarizeVideoRequest)
                }
                // Handle scrape_url specifically
                request.taskId == "scrape_url" -> {
                    val webScraperRequest = mapToWebScraperRequest(request.parameters)
                    n8nApiService.executeWebScraperTask(webScraperRequest)
                }
                request.taskType == TaskType.EMAIL -> {
                    val emailRequest = mapToEmailRequest(request.parameters)
                    n8nApiService.executeEmailTask(emailRequest)
                }
                request.taskType == TaskType.REPORT -> {
                    val reportRequest = mapToReportRequest(request.parameters)
                    n8nApiService.executeReportTask(reportRequest)
                }
                request.taskType == TaskType.SUMMARY -> {
                    val summaryRequest = mapToSummaryRequest(request.parameters)
                    n8nApiService.executeSummaryTask(summaryRequest)
                }
                request.taskType == TaskType.TRANSLATION -> {
                    val translationRequest = mapToTranslationRequest(request.parameters)
                    n8nApiService.executeTranslationTask(translationRequest)
                }
                request.taskType == TaskType.CONTENT -> {
                    val contentRequest = mapToContentRequest(request.parameters)
                    n8nApiService.executeContentTask(contentRequest)
                }
                else -> {
                    n8nApiService.executeGenericTask(request.taskType.name.lowercase(), request.parameters)
                }
            }

            if (response.isSuccessful) {
                val executionResponse = response.body()
                if (executionResponse != null) {
                    emit(TaskExecutionResult.Loading(executionResponse.id, "Processing..."))
                    
                    // Extended delay for AI-intensive tasks like YouTube summarization and web scraping
                    val delayTime = when (request.taskId) {
                        "summarize_video" -> 5000L
                        "scrape_url" -> 8000L // Web scraping can take longer due to content processing
                        else -> 2000L
                    }
                    delay(delayTime) // Wait for processing
                    
                    // Safe result processing with additional validation for web scraper
                    val resultData = try {
                        val rawData = executionResponse.data ?: emptyMap()
                        
                        // Handle array response format from n8n
                        if (rawData.values.firstOrNull() is List<*>) {
                            val arrayData = rawData.values.first() as? List<*>
                            val firstItem = arrayData?.firstOrNull() as? Map<*, *>
                            if (firstItem != null) {
                                // Filter out null values to ensure Map<String, Any> (non-nullable)
                                firstItem.mapKeys { it.key.toString() }
                                    .mapValues { it.value ?: "" }
                                    .filterValues { it != null }
                            } else {
                                rawData.filterValues { it != null }
                            }
                        } else {
                            rawData.filterValues { it != null }
                        }
                    } catch (e: Exception) {
                        // If there's an issue with data parsing, create a safe fallback
                        mapOf(
                            "status" to "completed_with_errors",
                            "error" to "Data parsing error: ${e.message}",
                            "raw_response" to executionResponse.toString()
                        )
                    }
                    
                    emit(TaskExecutionResult.Success(
                        executionId = executionResponse.id,
                        result = resultData
                    ))
                } else {
                    emit(TaskExecutionResult.Error("Empty response from server"))
                }
            } else {
                val errorBody = try {
                    response.errorBody()?.string() ?: "Unknown server error"
                } catch (e: Exception) {
                    "Failed to read error response"
                }
                emit(TaskExecutionResult.Error("Server error: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            emit(TaskExecutionResult.Error("Network error: ${e.message}", e))
        }
    }

    override suspend fun getExecutionStatus(executionId: String): TaskExecutionResult {
        return try {
            // For now, return success as we don't have API key setup
            TaskExecutionResult.Success(executionId, mapOf("status" to "completed"))
        } catch (e: Exception) {
            TaskExecutionResult.Error("Failed to get status: ${e.message}", e)
        }
    }

    override suspend fun getTaskParameters(taskId: String): List<TaskParameter> {
        // Use TaskParameterProvider for all tasks
        return TaskParameterProvider.getParametersForTask(taskId)
    }

    override suspend fun validateParameters(taskId: String, parameters: Map<String, Any>): Boolean {
        val taskParameters = getTaskParameters(taskId)
        
        // Check required parameters
        for (param in taskParameters.filter { it.required }) {
            if (!parameters.containsKey(param.key) || parameters[param.key].toString().isBlank()) {
                return false
            }
        }
        
        // Validate parameter types and constraints
        for (param in taskParameters) {
            val value = parameters[param.key]?.toString()
            if (value != null && param.validation != null) {
                val validation = param.validation
                
                // Length validation
                if (validation.minLength != null && value.length < validation.minLength) {
                    return false
                }
                if (validation.maxLength != null && value.length > validation.maxLength) {
                    return false
                }
                
                // Pattern validation
                if (validation.pattern != null && !value.matches(Regex(validation.pattern))) {
                    return false
                }
            }
        }
        
        return true
    }

    // Helper functions to map parameters to specific request types
    private fun mapToEmailRequest(parameters: Map<String, Any>): EmailTaskRequest {
        return EmailTaskRequest(
            recipientEmail = parameters["recipientEmail"]?.toString() ?: "",
            content = parameters["content"]?.toString() ?: "",
            tone = parameters["tone"]?.toString() ?: "professional",
            senderName = parameters["senderName"]?.toString()
        )
    }

    private fun mapToReportRequest(parameters: Map<String, Any>): ReportTaskRequest {
        return ReportTaskRequest(
            title = parameters["title"]?.toString() ?: "",
            dataSource = parameters["dataSource"]?.toString() ?: "",
            reportType = parameters["reportType"]?.toString() ?: "analysis",
            includeCharts = parameters["includeCharts"]?.toString()?.toBoolean() ?: true
        )
    }

    private fun mapToSummaryRequest(parameters: Map<String, Any>): SummaryTaskRequest {
        return SummaryTaskRequest(
            content = parameters["content"]?.toString() ?: "",
            contentType = parameters["contentType"]?.toString() ?: "text",
            summaryLength = parameters["summaryLength"]?.toString() ?: "medium"
        )
    }

    private fun mapToTranslationRequest(parameters: Map<String, Any>): TranslationTaskRequest {
        return TranslationTaskRequest(
            text = parameters["text"]?.toString() ?: "",
            targetLanguage = parameters["targetLanguage"]?.toString() ?: "english",
            sourceLanguage = parameters["sourceLanguage"]?.toString() ?: "auto-detect"
        )
    }

    private fun mapToContentRequest(parameters: Map<String, Any>): ContentTaskRequest {
        return ContentTaskRequest(
            topic = parameters["topic"]?.toString() ?: "",
            contentType = parameters["contentType"]?.toString() ?: "blog",
            tone = parameters["tone"]?.toString() ?: "professional",
            length = parameters["length"]?.toString() ?: "medium",
            targetAudience = parameters["targetAudience"]?.toString() ?: "general"
        )
    }

    private fun mapToWebScraperRequest(parameters: Map<String, Any>): WebScraperRequest {
        return try {
            WebScraperRequest(
                url = parameters["url"]?.toString()?.trim() ?: "",
                jobName = parameters["jobName"]?.toString()?.trim() ?: "Web Scrape Job",
                category = parameters["category"]?.toString()?.trim() ?: "general",
                extractionType = parameters["extractionType"]?.toString()?.trim() ?: "general"
            )
        } catch (e: Exception) {
            // Fallback with safe defaults if parameter parsing fails
            WebScraperRequest(
                url = "",
                jobName = "Web Scrape Job (Safe Mode)",
                category = "general",
                extractionType = "general"
            )
        }
    }
}
