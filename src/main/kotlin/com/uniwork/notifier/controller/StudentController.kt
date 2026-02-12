package com.uniwork.notifier.controller

import com.uniwork.notifier.dto.request.StudentRequest
import com.uniwork.notifier.dto.response.StudentResponse
import com.uniwork.notifier.service.StudentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/students")
class StudentController(private val studentService: StudentService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<StudentResponse>> {
        return ResponseEntity.ok(studentService.findAll())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<StudentResponse> {
        return ResponseEntity.ok(studentService.findById(id))
    }

    // POST endpoint removido - Los estudiantes solo se crean a través de /auth/register
    // para asegurar que siempre tengan password hasheado
    /*
    @PostMapping
    fun create(@Valid @RequestBody request: StudentRequest): ResponseEntity<StudentResponse> {
        val created = studentService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }
    */

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: StudentRequest): ResponseEntity<StudentResponse> {
        return ResponseEntity.ok(studentService.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        studentService.delete(id)
        return ResponseEntity.noContent().build()
    }
}