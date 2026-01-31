package com.uniwork.notifier.service

import com.uniwork.notifier.dto.request.TaskRequest
import com.uniwork.notifier.entity.Priority
import com.uniwork.notifier.entity.Status
import com.uniwork.notifier.entity.Student
import com.uniwork.notifier.entity.Task
import com.uniwork.notifier.repository.StudentRepository
import com.uniwork.notifier.repository.TaskRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class TaskServiceTest {

    @Mock
    lateinit var taskRepository: TaskRepository

    @Mock
    lateinit var studentRepository: StudentRepository

    @InjectMocks
    lateinit var taskService: TaskService

    private lateinit var student: Student
    private lateinit var task: Task
    private lateinit var taskRequest: TaskRequest

    @BeforeEach
    fun setup() {
        student = Student(
            id = 1L,
            firstName = "Pablo",
            lastName = "Brito",
            email = "pablo@test.com"
        )

        task = Task(
            id = 1L,
            title = "Proyecto Final",
            description = "Desarrollo del backend",
            dueDate = LocalDateTime.now().plusDays(5),
            priority = Priority.HIGH,
            status = Status.PENDING,
            student = student
        )

        taskRequest = TaskRequest(
            title = "Proyecto Final",
            description = "Desarrollo del backend",
            dueDate = LocalDateTime.now().plusDays(5),
            priority = "HIGH",
            status = null,
            studentId = 1L
        )
    }

    // ========== findAll ==========

    @Test
    fun `findAll retorna lista de tareas`() {
        `when`(taskRepository.findAll()).thenReturn(listOf(task))

        val result = taskService.findAll()

        assertEquals(1, result.size)
        assertEquals("Proyecto Final", result[0].title)
        verify(taskRepository).findAll()
    }

    @Test
    fun `findAll retorna lista vacia cuando no hay tareas`() {
        `when`(taskRepository.findAll()).thenReturn(emptyList())

        val result = taskService.findAll()

        assertEquals(0, result.size)
    }

    // ========== findById ==========

    @Test
    fun `findById retorna tarea cuando existe`() {
        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))

        val result = taskService.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Proyecto Final", result.title)
        assertEquals("HIGH", result.priority)
        verify(taskRepository).findById(1L)
    }

    @Test
    fun `findById lanza excepcion cuando no existe`() {
        `when`(taskRepository.findById(99L)).thenReturn(Optional.empty<Task>())

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.findById(99L)
        }

        assertEquals("Tarea no encontrada con ID: 99", exception.message)
    }

    // ========== findByStudentId ==========

    @Test
    fun `findByStudentId retorna tareas del estudiante`() {
        `when`(studentRepository.existsById(1L)).thenReturn(true)
        `when`(taskRepository.findByStudentId(1L)).thenReturn(listOf(task))

        val result = taskService.findByStudentId(1L)

        assertEquals(1, result.size)
        assertEquals(1L, result[0].studentId)
        verify(studentRepository).existsById(1L)
        verify(taskRepository).findByStudentId(1L)
    }

    @Test
    fun `findByStudentId lanza excepcion cuando estudiante no existe`() {
        `when`(studentRepository.existsById(99L)).thenReturn(false)

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.findByStudentId(99L)
        }

        assertEquals("Estudiante no encontrado con ID: 99", exception.message)
    }

    // ========== create ==========

    @Test
    fun `create guarda y retorna tarea correctamente`() {
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(taskRepository.save(any<Task>())).thenReturn(task)

        val result = taskService.create(taskRequest)

        assertEquals("Proyecto Final", result.title)
        assertEquals("HIGH", result.priority)
        assertEquals(1L, result.studentId)
        verify(studentRepository).findById(1L)
        verify(taskRepository).save(any<Task>())
    }

    @Test
    fun `create lanza excepcion cuando estudiante no existe`() {
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty<Student>())

        val requestWithInvalidStudent = taskRequest.copy(studentId = 99L)

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.create(requestWithInvalidStudent)
        }

        assertEquals("Estudiante no encontrado con ID: 99", exception.message)
    }

    @Test
    fun `create lanza excepcion cuando prioridad no es valida`() {
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val requestWithInvalidPriority = taskRequest.copy(priority = "INVALIDA")

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.create(requestWithInvalidPriority)
        }

        assertEquals("Prioridad no válida: INVALIDA", exception.message)
    }

    // ========== update ==========

    @Test
    fun `update modifica y retorna tarea correctamente`() {
        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(taskRepository.save(any<Task>())).thenReturn(task)

        val result = taskService.update(1L, taskRequest)

        assertEquals("Proyecto Final", result.title)
        verify(taskRepository).findById(1L)
        verify(taskRepository).save(any<Task>())
    }

    @Test
    fun `update con status retorna tarea con status actualizado`() {
        val requestWithStatus = taskRequest.copy(status = "IN_PROGRESS")
        val taskUpdated = Task(
            id = 1L,
            title = "Proyecto Final",
            description = "Desarrollo del backend",
            dueDate = LocalDateTime.now().plusDays(5),
            priority = Priority.HIGH,
            status = Status.IN_PROGRESS,
            student = student
        )

        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(taskRepository.save(any<Task>())).thenReturn(taskUpdated)

        val result = taskService.update(1L, requestWithStatus)

        assertEquals("IN_PROGRESS", result.status)
    }

    @Test
    fun `update lanza excepcion cuando tarea no existe`() {
        `when`(taskRepository.findById(99L)).thenReturn(Optional.empty<Task>())

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.update(99L, taskRequest)
        }

        assertEquals("Tarea no encontrada con ID: 99", exception.message)
    }

    @Test
    fun `update lanza excepcion cuando estudiante no existe`() {
        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty<Student>())

        val requestWithInvalidStudent = taskRequest.copy(studentId = 99L)

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.update(1L, requestWithInvalidStudent)
        }

        assertEquals("Estudiante no encontrado con ID: 99", exception.message)
    }

    @Test
    fun `update lanza excepcion cuando prioridad no es valida`() {
        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val requestWithInvalidPriority = taskRequest.copy(priority = "INVALIDA")

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.update(1L, requestWithInvalidPriority)
        }

        assertEquals("Prioridad no válida: INVALIDA", exception.message)
    }

    // ========== delete ==========

    @Test
    fun `delete elimina tarea correctamente`() {
        `when`(taskRepository.existsById(1L)).thenReturn(true)

        taskService.delete(1L)

        verify(taskRepository).existsById(1L)
        verify(taskRepository).deleteById(1L)
    }

    @Test
    fun `delete lanza excepcion cuando tarea no existe`() {
        `when`(taskRepository.existsById(99L)).thenReturn(false)

        val exception = assertThrows(RuntimeException::class.java) {
            taskService.delete(99L)
        }

        assertEquals("Tarea no encontrada con ID: 99", exception.message)
        verify(taskRepository, never()).deleteById(any<Long>())
    }
}