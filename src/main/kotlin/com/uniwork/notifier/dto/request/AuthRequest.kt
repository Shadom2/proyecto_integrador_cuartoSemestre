package com.uniwork.notifier.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * DTO para registro de nuevos estudiantes
 */
data class RegisterRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    val firstName: String,

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 50, message = "El apellido no puede superar 50 caracteres")
    val lastName: String,

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    @Size(max = 100, message = "El email no puede superar 100 caracteres")
    val email: String,

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    val password: String
)

/**
 * DTO para login de estudiantes existentes
 */
data class LoginRequest(
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    val email: String,

    @NotBlank(message = "La contraseña no puede estar vacía")
    val password: String
)
