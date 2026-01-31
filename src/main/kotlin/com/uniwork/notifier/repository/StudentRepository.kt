package com.uniwork.notifier.repository

import com.uniwork.notifier.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long> {
    fun findByEmail(email: String): Student?
    fun existsByEmail(email: String): Boolean
}