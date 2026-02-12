package com.uniwork.notifier.mapper

import com.uniwork.notifier.dto.request.StudentRequest
import com.uniwork.notifier.dto.response.StudentResponse
import com.uniwork.notifier.entity.Student

fun StudentRequest.toEntity(password: String): Student {
    return Student(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        password = password
    )
}

fun Student.toResponse(): StudentResponse {
    return StudentResponse(
        id = this.id!!,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        createdAt = this.createdAt
    )
}