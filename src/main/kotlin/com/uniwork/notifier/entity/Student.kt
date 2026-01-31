package com.uniwork.notifier.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "students")
open class Student(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null,

    @Column(name = "first_name", nullable = false, length = 50)
    open var firstName: String,

    @Column(name = "last_name", nullable = false, length = 50)
    open var lastName: String,

    @Column(name = "email", nullable = false, unique = true, length = 100)
    open var email: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now()
) {
    @OneToMany(mappedBy = "student", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    open val tasks: MutableList<Task> = mutableListOf()
}