package com.uniwork.notifier.mapper

import com.uniwork.notifier.dto.request.TaskRequest
import com.uniwork.notifier.dto.response.TaskResponse
import com.uniwork.notifier.entity.Priority
import com.uniwork.notifier.entity.Status
import com.uniwork.notifier.entity.Student
import com.uniwork.notifier.entity.Task

fun TaskRequest.toEntity(student: Student): Task {
    return Task(
        title = this.title,
        description = this.description,
        dueDate = this.dueDate,
        priority = Priority.valueOf(this.priority.uppercase()),
        status = if (this.status != null) Status.valueOf(this.status.uppercase()) else Status.PENDING,
        student = student
    )
}

fun Task.toResponse(): TaskResponse {
    return TaskResponse(
        id = this.id!!,
        title = this.title,
        description = this.description,
        dueDate = this.dueDate,
        priority = this.priority.name,
        status = this.status.name,
        studentId = this.student.id!!,
        createdAt = this.createdAt
    )
}