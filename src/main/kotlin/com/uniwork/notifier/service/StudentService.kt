package com.uniwork.notifier.service

import com.uniwork.notifier.dto.request.StudentRequest
import com.uniwork.notifier.dto.response.StudentResponse
import com.uniwork.notifier.mapper.toEntity
import com.uniwork.notifier.mapper.toResponse
import com.uniwork.notifier.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(private val studentRepository: StudentRepository) {

    @Transactional(readOnly = true)
    fun findAll(): List<StudentResponse> {
        return studentRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): StudentResponse {
        val student = studentRepository.findById(id)
            .orElseThrow { RuntimeException("Estudiante no encontrado con ID: $id") }
        return student.toResponse()
    }

    @Transactional
    fun create(request: StudentRequest): StudentResponse {
        if (studentRepository.existsByEmail(request.email)) {
            throw RuntimeException("Ya existe un estudiante con ese email")
        }
        val student = request.toEntity()
        val saved = studentRepository.save(student)
        return saved.toResponse()
    }

    @Transactional
    fun update(id: Long, request: StudentRequest): StudentResponse {
        val student = studentRepository.findById(id)
            .orElseThrow { RuntimeException("Estudiante no encontrado con ID: $id") }

        if (student.email != request.email && studentRepository.existsByEmail(request.email)) {
            throw RuntimeException("Ya existe un estudiante con ese email")
        }

        student.firstName = request.firstName
        student.lastName = request.lastName
        student.email = request.email

        val updated = studentRepository.save(student)
        return updated.toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        if (!studentRepository.existsById(id)) {
            throw RuntimeException("Estudiante no encontrado con ID: $id")
        }
        studentRepository.deleteById(id)
    }
}