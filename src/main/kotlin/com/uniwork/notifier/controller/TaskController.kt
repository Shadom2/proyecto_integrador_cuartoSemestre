package com.uniwork.notifier.controller

import com.uniwork.notifier.dto.request.TaskRequest
import com.uniwork.notifier.dto.response.TaskResponse
import com.uniwork.notifier.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<TaskResponse>> {
        return ResponseEntity.ok(taskService.findAll())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        return ResponseEntity.ok(taskService.findById(id))
    }

    @GetMapping("/student/{studentId}")
    fun findByStudentId(@PathVariable studentId: Long): ResponseEntity<List<TaskResponse>> {
        return ResponseEntity.ok(taskService.findByStudentId(studentId))
    }

    @PostMapping
    fun create(@Valid @RequestBody request: TaskRequest): ResponseEntity<TaskResponse> {
        val created = taskService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: TaskRequest): ResponseEntity<TaskResponse> {
        return ResponseEntity.ok(taskService.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        taskService.delete(id)
        return ResponseEntity.noContent().build()
    }
}