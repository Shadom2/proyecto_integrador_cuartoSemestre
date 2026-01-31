package com.uniwork.notifier.controller

import com.uniwork.notifier.dto.request.SubTaskRequest
import com.uniwork.notifier.dto.response.SubTaskResponse
import com.uniwork.notifier.service.SubTaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subtasks")
class SubTaskController(private val subTaskService: SubTaskService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<SubTaskResponse>> {
        return ResponseEntity.ok(subTaskService.findAll())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<SubTaskResponse> {
        return ResponseEntity.ok(subTaskService.findById(id))
    }

    @GetMapping("/task/{taskId}")
    fun findByTaskId(@PathVariable taskId: Long): ResponseEntity<List<SubTaskResponse>> {
        return ResponseEntity.ok(subTaskService.findByTaskId(taskId))
    }

    @PostMapping
    fun create(@Valid @RequestBody request: SubTaskRequest): ResponseEntity<SubTaskResponse> {
        val created = subTaskService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: SubTaskRequest): ResponseEntity<SubTaskResponse> {
        return ResponseEntity.ok(subTaskService.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        subTaskService.delete(id)
        return ResponseEntity.noContent().build()
    }
}