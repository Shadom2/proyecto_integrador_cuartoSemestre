package com.uniwork.notifier.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

/**
 * Utilidad para generar y validar tokens JWT
 */
@Component
class JwtUtil {

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @Value("\${jwt.expiration}")
    private var expiration: Long = 86400000 // 24 horas por defecto

    private fun getSigningKey(): SecretKey {
        return Keys.hmacShaKeyFor(secret.toByteArray())
    }

    /**
     * Genera un token JWT para el email del usuario
     */
    fun generateToken(email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact()
    }

    /**
     * Extrae el email del token JWT
     */
    fun extractEmail(token: String): String {
        return extractAllClaims(token).subject
    }

    /**
     * Valida si el token es válido y no ha expirado
     */
    fun validateToken(token: String, email: String): Boolean {
        return try {
            val extractedEmail = extractEmail(token)
            extractedEmail == email && !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }
}
