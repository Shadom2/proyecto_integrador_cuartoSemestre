package com.uniwork.notifier.service

import com.uniwork.notifier.dto.request.SubTaskRequest
import com.uniwork.notifier.dto.response.SubTaskResponse
import com.uniwork.notifier.entity.Status
import com.uniwork.notifier.mapper.toEntity
import com.uniwork.notifier.mapper.toResponse
import com.uniwork.notifier.repository.SubTaskRepository
import com.uniwork.notifier.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubTaskService(
    private val subTaskRepository: SubTaskRepository,
    private val taskRepository: TaskRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<SubTaskResponse> {
        return subTaskRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): SubTaskResponse {
        val subTask = subTaskRepository.findById(id)
            .orElseThrow { RuntimeException("Sub-tarea no encontrada con ID: $id") }
        return subTask.toResponse()
    }

    @Transactional(readOnly = true)
    fun findByTaskId(taskId: Long): List<SubTaskResponse> {
        if (!taskRepository.existsById(taskId)) {
            throw RuntimeException("Tarea no encontrada con ID: $taskId")
        }
        return subTaskRepository.findByTaskId(taskId).map { it.toResponse() }
    }

    @Transactional
    fun create(request: SubTaskRequest): SubTaskResponse {
        val task = taskRepository.findById(request.taskId)
            .orElseThrow { RuntimeException("Tarea no encontrada con ID: ${request.taskId}") }

        val subTask = request.toEntity(task)
        val saved = subTaskRepository.save(subTask)
        return saved.toResponse()
    }

    @Transactional
    fun update(id: Long, request: SubTaskRequest): SubTaskResponse {
        val subTask = subTaskRepository.findById(id)
            .orElseThrow { RuntimeException("Sub-tarea no encontrada con ID: $id") }

        val task = taskRepository.findById(request.taskId)
            .orElseThrow { RuntimeException("Tarea no encontrada con ID: ${request.taskId}") }

        subTask.title = request.title
        subTask.description = request.description
        subTask.status = if (request.status != null) Status.valueOf(request.status.uppercase()) else subTask.status
        subTask.task = task

        val updated = subTaskRepository.save(subTask)
        return updated.toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        if (!subTaskRepository.existsById(id)) {
            throw RuntimeException("Sub-tarea no encontrada con ID: $id")
        }
        subTaskRepository.deleteById(id)
    }
}