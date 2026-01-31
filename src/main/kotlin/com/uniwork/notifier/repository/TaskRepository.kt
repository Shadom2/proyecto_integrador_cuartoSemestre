package com.uniwork.notifier.repository

import com.uniwork.notifier.entity.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {
    fun findByStudentId(studentId: Long): List<Task>
    fun findByStudentIdAndPriority(studentId: Long, priority: String): List<Task>
}