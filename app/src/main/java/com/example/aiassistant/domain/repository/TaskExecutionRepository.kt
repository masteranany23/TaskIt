package com.example.aiassistant.domain.repository

import com.example.aiassistant.domain.model.*
import kotlinx.coroutines.flow.Flow

interface TaskExecutionRepository {
    suspend fun executeTask(request: TaskExecutionRequest): Flow<TaskExecutionResult>
    suspend fun getExecutionStatus(executionId: String): TaskExecutionResult
    suspend fun getTaskParameters(taskId: String): List<TaskParameter>
    suspend fun validateParameters(taskId: String, parameters: Map<String, Any>): Boolean
}
