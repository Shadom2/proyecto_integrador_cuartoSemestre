package com.uniwork.notifier.repository

import com.uniwork.notifier.entity.SubTask
import org.springframework.data.jpa.repository.JpaRepository

interface SubTaskRepository : JpaRepository<SubTask, Long> {
    fun findByTaskId(taskId: Long): List<SubTask>
}