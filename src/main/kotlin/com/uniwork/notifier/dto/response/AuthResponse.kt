package com.uniwork.notifier.dto.response

/**
 * DTO de respuesta para autenticación (login/register)
 * Contiene el token JWT y los datos del estudiante autenticado
 */
data class AuthResponse(
    val token: String,
    val student: StudentResponse
)
