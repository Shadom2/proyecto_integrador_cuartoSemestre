package com.uniwork.notifier.mapper

import com.uniwork.notifier.dto.request.SubTaskRequest
import com.uniwork.notifier.dto.response.SubTaskResponse
import com.uniwork.notifier.entity.Status
import com.uniwork.notifier.entity.SubTask
import com.uniwork.notifier.entity.Task

fun SubTaskRequest.toEntity(task: Task): SubTask {
    return SubTask(
        title = this.title,
        description = this.description,
        status = if (this.status != null) Status.valueOf(this.status.uppercase()) else Status.PENDING,
        task = task
    )
}

fun SubTask.toResponse(): SubTaskResponse {
    return SubTaskResponse(
        id = this.id!!,
        title = this.title,
        description = this.description,
        status = this.status.name,
        taskId = this.task.id!!,
        createdAt = this.createdAt
    )
}