package com.uniwork.notifier.dto.response

import java.time.LocalDateTime

data class SubTaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: String,
    val taskId: Long,
    val createdAt: LocalDateTime
)