package com.uniwork.notifier.dto.response

import java.time.LocalDateTime

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val dueDate: LocalDateTime,
    val priority: String,
    val status: String,
    val studentId: Long,
    val createdAt: LocalDateTime
)