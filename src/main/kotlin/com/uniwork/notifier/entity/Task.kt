package com.uniwork.notifier.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
open class Task(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null,

    @Column(name = "title", nullable = false, length = 100)
    open var title: String,

    @Column(name = "description", length = 500)
    open var description: String? = null,

    @Column(name = "due_date", nullable = false)
    open var dueDate: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    open var priority: Priority,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    open var status: Status = Status.PENDING,

    @Column(name = "created_at", nullable = false, updatable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    open var student: Student
) {
    @OneToMany(mappedBy = "task", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    open val subTasks: MutableList<SubTask> = mutableListOf()
}

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}

enum class Status {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}