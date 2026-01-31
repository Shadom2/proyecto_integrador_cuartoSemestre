package com.uniwork.notifier.service

import com.uniwork.notifier.dto.request.StudentRequest
import com.uniwork.notifier.entity.Student
import com.uniwork.notifier.repository.StudentRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock
    lateinit var studentRepository: StudentRepository

    @InjectMocks
    lateinit var studentService: StudentService

    private lateinit var studentRequest: StudentRequest
    private lateinit var student: Student

    @BeforeEach
    fun setup() {
        studentRequest = StudentRequest(
            firstName = "Pablo",
            lastName = "Brito",
            email = "pablo@test.com"
        )

        student = Student(
            id = 1L,
            firstName = "Pablo",
            lastName = "Brito",
            email = "pablo@test.com"
        )
    }

    // ========== findAll ==========

    @Test
    fun `findAll retorna lista de estudiantes`() {
        `when`(studentRepository.findAll()).thenReturn(listOf(student))

        val result = studentService.findAll()

        assertEquals(1, result.size)
        assertEquals("Pablo", result[0].firstName)
        verify(studentRepository).findAll()
    }

    @Test
    fun `findAll retorna lista vacia cuando no hay estudiantes`() {
        `when`(studentRepository.findAll()).thenReturn(emptyList())

        val result = studentService.findAll()

        assertEquals(0, result.size)
        verify(studentRepository).findAll()
    }

    // ========== findById ==========

    @Test
    fun `findById retorna estudiante cuando existe`() {
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val result = studentService.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Pablo", result.firstName)
        verify(studentRepository).findById(1L)
    }

    @Test
    fun `findById lanza excepcion cuando no existe`() {
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty<Student>())

        val exception = assertThrows(RuntimeException::class.java) {
            studentService.findById(99L)
        }

        assertEquals("Estudiante no encontrado con ID: 99", exception.message)
        verify(studentRepository).findById(99L)
    }

    // ========== create ==========

    @Test
    fun `create guarda y retorna estudiante correctamente`() {
        `when`(studentRepository.existsByEmail("pablo@test.com")).thenReturn(false)
        `when`(studentRepository.save(any<Student>())).thenReturn(student)

        val result = studentService.create(studentRequest)

        assertEquals("Pablo", result.firstName)
        assertEquals("pablo@test.com", result.email)
        verify(studentRepository).existsByEmail("pablo@test.com")
        verify(studentRepository).save(any<Student>())
    }

    @Test
    fun `create lanza excepcion cuando email ya existe`() {
        `when`(studentRepository.existsByEmail("pablo@test.com")).thenReturn(true)

        val exception = assertThrows(RuntimeException::class.java) {
            studentService.create(studentRequest)
        }

        assertEquals("Ya existe un estudiante con ese email", exception.message)
        verify(studentRepository).existsByEmail("pablo@test.com")
        verify(studentRepository, never()).save(any<Student>())
    }

    // ========== update ==========

    @Test
    fun `update modifica y retorna estudiante correctamente`() {
        val updatedRequest = StudentRequest(
            firstName = "Pablo",
            lastName = "Brito Actualizado",
            email = "pablo@test.com"
        )

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(studentRepository.save(any<Student>())).thenReturn(student)

        val result = studentService.update(1L, updatedRequest)

        assertEquals("Pablo", result.firstName)
        verify(studentRepository).findById(1L)
        verify(studentRepository).save(any<Student>())
    }

    @Test
    fun `update lanza excepcion cuando estudiante no existe`() {
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty<Student>())

        val exception = assertThrows(RuntimeException::class.java) {
            studentService.update(99L, studentRequest)
        }

        assertEquals("Estudiante no encontrado con ID: 99", exception.message)
    }

    @Test
    fun `update lanza excepcion cuando nuevo email ya esta en uso`() {
        val otherEmailRequest = StudentRequest(
            firstName = "Pablo",
            lastName = "Brito",
            email = "otro@test.com"
        )

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(studentRepository.existsByEmail("otro@test.com")).thenReturn(true)

        val exception = assertThrows(RuntimeException::class.java) {
            studentService.update(1L, otherEmailRequest)
        }

        assertEquals("Ya existe un estudiante con ese email", exception.message)
    }

    // ========== delete ==========

    @Test
    fun `delete elimina estudiante correctamente`() {
        `when`(studentRepository.existsById(1L)).thenReturn(true)

        studentService.delete(1L)

        verify(studentRepository).existsById(1L)
        verify(studentRepository).deleteById(1L)
    }

    @Test
    fun `delete lanza excepcion cuando estudiante no existe`() {
        `when`(studentRepository.existsById(99L)).thenReturn(false)

        val exception = assertThrows(RuntimeException::class.java) {
            studentService.delete(99L)
        }

        assertEquals("Estudiante no encontrado con ID: 99", exception.message)
        verify(studentRepository, never()).deleteById(any<Long>())
    }
}