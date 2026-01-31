package com.uniwork.notifier.service

import com.uniwork.notifier.dto.request.SubTaskRequest
import com.uniwork.notifier.entity.Priority
import com.uniwork.notifier.entity.Status
import com.uniwork.notifier.entity.Student
import com.uniwork.notifier.entity.SubTask
import com.uniwork.notifier.entity.Task
import com.uniwork.notifier.repository.SubTaskRepository
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
class SubTaskServiceTest {

    @Mock
    lateinit var subTaskRepository: SubTaskRepository

    @Mock
    lateinit var taskRepository: TaskRepository

    @InjectMocks
    lateinit var subTaskService: SubTaskService

    private lateinit var student: Student
    private lateinit var task: Task
    private lateinit var subTask: SubTask
    private lateinit var subTaskRequest: SubTaskRequest

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

        subTask = SubTask(
            id = 1L,
            title = "Crear entidades",
            description = "Crear las 3 entidades JPA",
            status = Status.PENDING,
            task = task
        )

        subTaskRequest = SubTaskRequest(
            title = "Crear entidades",
            description = "Crear las 3 entidades JPA",
            status = null,
            taskId = 1L
        )
    }

    // ========== findAll ==========

    @Test
    fun `findAll retorna lista de subtareas`() {
        `when`(subTaskRepository.findAll()).thenReturn(listOf(subTask))

        val result = subTaskService.findAll()

        assertEquals(1, result.size)
        assertEquals("Crear entidades", result[0].title)
        verify(subTaskRepository).findAll()
    }

    @Test
    fun `findAll retorna lista vacia cuando no hay subtareas`() {
        `when`(subTaskRepository.findAll()).thenReturn(emptyList())

        val result = subTaskService.findAll()

        assertEquals(0, result.size)
    }

    // ========== findById ==========

    @Test
    fun `findById retorna subtarea cuando existe`() {
        `when`(subTaskRepository.findById(1L)).thenReturn(Optional.of(subTask))

        val result = subTaskService.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Crear entidades", result.title)
        assertEquals(1L, result.taskId)
        verify(subTaskRepository).findById(1L)
    }

    @Test
    fun `findById lanza excepcion cuando no existe`() {
        `when`(subTaskRepository.findById(99L)).thenReturn(Optional.empty<SubTask>())

        val exception = assertThrows(RuntimeException::class.java) {
            subTaskService.findById(99L)
        }

        assertEquals("Sub-tarea no encontrada con ID: 99", exception.message)
    }

    // ========== findByTaskId ==========

    @Test
    fun `findByTaskId retorna subtareas de la tarea`() {
        `when`(taskRepository.existsById(1L)).thenReturn(true)
        `when`(subTaskRepository.findByTaskId(1L)).thenReturn(listOf(subTask))

        val result = subTaskService.findByTaskId(1L)

        assertEquals(1, result.size)
        assertEquals(1L, result[0].taskId)
        verify(taskRepository).existsById(1L)
        verify(subTaskRepository).findByTaskId(1L)
    }

    @Test
    fun `findByTaskId lanza excepcion cuando tarea no existe`() {
        `when`(taskRepository.existsById(99L)).thenReturn(false)

        val exception = assertThrows(RuntimeException::class.java) {
            subTaskService.findByTaskId(99L)
        }

        assertEquals("Tarea no encontrada con ID: 99", exception.message)
    }

    // ========== create ==========

    @Test
    fun `create guarda y retorna subtarea correctamente`() {
        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        `when`(subTaskRepository.save(any<SubTask>())).thenReturn(subTask)

        val result = subTaskService.create(subTaskRequest)

        assertEquals("Crear entidades", result.title)
        assertEquals(1L, result.taskId)
        assertEquals("PENDING", result.status)
        verify(taskRepository).findById(1L)
        verify(subTaskRepository).save(any<SubTask>())
    }

    @Test
    fun `create lanza excepcion cuando tarea no existe`() {
        `when`(taskRepository.findById(99L)).thenReturn(Optional.empty<Task>())

        val requestWithInvalidTask = subTaskRequest.copy(taskId = 99L)

        val exception = assertThrows(RuntimeException::class.java) {
            subTaskService.create(requestWithInvalidTask)
        }

        assertEquals("Tarea no encontrada con ID: 99", exception.message)
    }

    // ========== update ==========

    @Test
    fun `update modifica y retorna subtarea correctamente`() {
        `when`(subTaskRepository.findById(1L)).thenReturn(Optional.of(subTask))
        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        `when`(subTaskRepository.save(any<SubTask>())).thenReturn(subTask)

        val result = subTaskService.update(1L, subTaskRequest)

        assertEquals("Crear entidades", result.title)
        verify(subTaskRepository).findById(1L)
        verify(subTaskRepository).save(any<SubTask>())
    }

    @Test
    fun `update con status retorna subtarea con status actualizado`() {
        val requestWithStatus = subTaskRequest.copy(status = "COMPLETED")
        val subTaskUpdated = SubTask(
            id = 1L,
            title = "Crear entidades",
            description = "Crear las 3 entidades JPA",
            status = Status.COMPLETED,
            task = task
        )

        `when`(subTaskRepository.findById(1L)).thenReturn(Optional.of(subTask))
        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task))
        `when`(subTaskRepository.save(any<SubTask>())).thenReturn(subTaskUpdated)

        val result = subTaskService.update(1L, requestWithStatus)

        assertEquals("COMPLETED", result.status)
    }

    @Test
    fun `update lanza excepcion cuando subtarea no existe`() {
        `when`(subTaskRepository.findById(99L)).thenReturn(Optional.empty<SubTask>())

        val exception = assertThrows(RuntimeException::class.java) {
            subTaskService.update(99L, subTaskRequest)
        }

        assertEquals("Sub-tarea no encontrada con ID: 99", exception.message)
    }

    @Test
    fun `update lanza excepcion cuando tarea no existe`() {
        `when`(subTaskRepository.findById(1L)).thenReturn(Optional.of(subTask))
        `when`(taskRepository.findById(99L)).thenReturn(Optional.empty<Task>())

        val requestWithInvalidTask = subTaskRequest.copy(taskId = 99L)

        val exception = assertThrows(RuntimeException::class.java) {
            subTaskService.update(1L, requestWithInvalidTask)
        }

        assertEquals("Tarea no encontrada con ID: 99", exception.message)
    }

    // ========== delete ==========

    @Test
    fun `delete elimina subtarea correctamente`() {
        `when`(subTaskRepository.existsById(1L)).thenReturn(true)

        subTaskService.delete(1L)

        verify(subTaskRepository).existsById(1L)
        verify(subTaskRepository).deleteById(1L)
    }

    @Test
    fun `delete lanza excepcion cuando subtarea no existe`() {
        `when`(subTaskRepository.existsById(99L)).thenReturn(false)

        val exception = assertThrows(RuntimeException::class.java) {
            subTaskService.delete(99L)
        }

        assertEquals("Sub-tarea no encontrada con ID: 99", exception.message)
        verify(subTaskRepository, never()).deleteById(any<Long>())
    }
}