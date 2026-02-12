package com.uniwork.notifier.controller

import com.uniwork.notifier.dto.request.LoginRequest
import com.uniwork.notifier.dto.request.RegisterRequest
import com.uniwork.notifier.dto.response.AuthResponse
import com.uniwork.notifier.entity.Student
import com.uniwork.notifier.mapper.toResponse
import com.uniwork.notifier.repository.StudentRepository
import com.uniwork.notifier.security.JwtUtil
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

/**
 * Controlador de autenticación - gestiona registro y login
 */
@RestController
@RequestMapping("/auth")
class AuthController(
    private val studentRepository: StudentRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    /**
     * POST /auth/register - Registra un nuevo estudiante
     */
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Any> {
        return try {
            // Verificar si el email ya existe
            if (studentRepository.existsByEmail(request.email)) {
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(mapOf("error" to "El email ${request.email} ya está registrado"))
            }

            // Crear estudiante con contraseña hasheada
            val student = Student(
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                password = passwordEncoder.encode(request.password) ?: ""
            )

            val savedStudent = studentRepository.save(student)

            // Generar token JWT
            val token = jwtUtil.generateToken(savedStudent.email)

            // Retornar token + datos del estudiante (sin el password)
            val response = AuthResponse(
                token = token,
                student = savedStudent.toResponse()
            )

            ResponseEntity.status(HttpStatus.CREATED).body(response)

        } catch (e: Exception) {
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error al registrar usuario: ${e.message}"))
        }
    }

    /**
     * POST /auth/login - Autentica un estudiante existente
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            // Buscar estudiante por email
            val student = studentRepository.findByEmail(request.email)
                ?: return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(mapOf("error" to "Credenciales inválidas"))

            // Verificar contraseña
            if (!passwordEncoder.matches(request.password, student.password)) {
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(mapOf("error" to "Credenciales inválidas"))
            }

            // Generar token JWT
            val token = jwtUtil.generateToken(student.email)

            // Retornar token + datos del estudiante
            val response = AuthResponse(
                token = token,
                student = student.toResponse()
            )

            ResponseEntity.ok(response)

        } catch (e: Exception) {
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error al iniciar sesión: ${e.message}"))
        }
    }
}
