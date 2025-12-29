package com.example.aiassistant.domain.usecase

import com.example.aiassistant.domain.model.*
import com.example.aiassistant.domain.repository.TaskExecutionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExecuteTaskUseCase @Inject constructor(
    private val repository: TaskExecutionRepository
) {
    suspend operator fun invoke(request: TaskExecutionRequest): Flow<TaskExecutionResult> {
        return repository.executeTask(request)
    }
}

class GetTaskParametersUseCase @Inject constructor(
    private val repository: TaskExecutionRepository
) {
    suspend operator fun invoke(taskId: String): List<TaskParameter> {
        return repository.getTaskParameters(taskId)
    }
}

class ValidateTaskParametersUseCase @Inject constructor(
    private val repository: TaskExecutionRepository
) {
    suspend operator fun invoke(taskId: String, parameters: Map<String, Any>): Boolean {
        return repository.validateParameters(taskId, parameters)
    }
}
