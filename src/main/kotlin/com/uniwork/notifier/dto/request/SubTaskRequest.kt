package com.uniwork.notifier.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class SubTaskRequest(

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede superar 100 caracteres")
    val title: String,

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    val description: String? = null,

    val status: String? = null,

    @NotNull(message = "El ID de la tarea no puede estar vacío")
    val taskId: Long
)