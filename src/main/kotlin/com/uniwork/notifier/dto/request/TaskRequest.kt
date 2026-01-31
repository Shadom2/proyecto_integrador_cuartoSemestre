package com.uniwork.notifier.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class TaskRequest(

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede superar 100 caracteres")
    val title: String,

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    val description: String? = null,

    @NotNull(message = "La fecha de vencimiento no puede estar vacía")
    val dueDate: LocalDateTime,

    @NotNull(message = "La prioridad no puede estar vacía")
    val priority: String,

    val status: String? = null,

    @NotNull(message = "El ID del estudiante no puede estar vacío")
    val studentId: Long
)