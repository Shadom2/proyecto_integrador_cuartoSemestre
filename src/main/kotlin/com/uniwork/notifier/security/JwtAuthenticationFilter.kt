package com.uniwork.notifier.security

import com.uniwork.notifier.repository.StudentRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filtro que intercepta cada petición HTTP para validar el token JWT
 */
@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val studentRepository: StudentRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader("Authorization")

            // Si no hay header o no empieza con "Bearer ", continuar sin autenticar
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)
                return
            }

            val token = authHeader.substring(7) // Quitar "Bearer "
            val email = jwtUtil.extractEmail(token)

            // Si el token es válido y no hay autenticación previa
            if (SecurityContextHolder.getContext().authentication == null) {
                val student = studentRepository.findByEmail(email)

                if (student != null && jwtUtil.validateToken(token, email)) {
                    // Crear autenticación y setearla en el contexto de seguridad
                    val authentication = UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        emptyList() // Sin roles por ahora
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (e: Exception) {
            // Si hay cualquier error en el token, simplemente continuar sin autenticar
            logger.error("Error procesando JWT token: ${e.message}")
        }

        filterChain.doFilter(request, response)
    }
}
