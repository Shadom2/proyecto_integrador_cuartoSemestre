package com.uniwork.notifier.dto.response

import java.time.LocalDateTime

data class StudentResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: LocalDateTime
)