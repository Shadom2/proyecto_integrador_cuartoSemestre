package com.uniwork.notifier.service

import com.uniwork.notifier.dto.request.TaskRequest
import com.uniwork.notifier.dto.response.TaskResponse
import com.uniwork.notifier.entity.Priority
import com.uniwork.notifier.entity.Status
import com.uniwork.notifier.mapper.toEntity
import com.uniwork.notifier.mapper.toResponse
import com.uniwork.notifier.repository.StudentRepository
import com.uniwork.notifier.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val studentRepository: StudentRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<TaskResponse> {
        return taskRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): TaskResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { RuntimeException("Tarea no encontrada con ID: $id") }
        return task.toResponse()
    }

    @Transactional(readOnly = true)
    fun findByStudentId(studentId: Long): List<TaskResponse> {
        if (!studentRepository.existsById(studentId)) {
            throw RuntimeException("Estudiante no encontrado con ID: $studentId")
        }
        return taskRepository.findByStudentId(studentId).map { it.toResponse() }
    }

    @Transactional
    fun create(request: TaskRequest): TaskResponse {
        val student = studentRepository.findById(request.studentId)
            .orElseThrow { RuntimeException("Estudiante no encontrado con ID: ${request.studentId}") }

        Priority.values().find { it.name == request.priority.uppercase() }
            ?: throw RuntimeException("Prioridad no válida: ${request.priority}")

        val task = request.toEntity(student)
        val saved = taskRepository.save(task)
        return saved.toResponse()
    }

    @Transactional
    fun update(id: Long, request: TaskRequest): TaskResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { RuntimeException("Tarea no encontrada con ID: $id") }

        val student = studentRepository.findById(request.studentId)
            .orElseThrow { RuntimeException("Estudiante no encontrado con ID: ${request.studentId}") }

        Priority.values().find { it.name == request.priority.uppercase() }
            ?: throw RuntimeException("Prioridad no válida: ${request.priority}")

        task.title = request.title
        task.description = request.description
        task.dueDate = request.dueDate
        task.priority = Priority.valueOf(request.priority.uppercase())
        task.status = if (request.status != null) Status.valueOf(request.status.uppercase()) else task.status
        task.student = student

        val updated = taskRepository.save(task)
        return updated.toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        if (!taskRepository.existsById(id)) {
            throw RuntimeException("Tarea no encontrada con ID: $id")
        }
        taskRepository.deleteById(id)
    }
}