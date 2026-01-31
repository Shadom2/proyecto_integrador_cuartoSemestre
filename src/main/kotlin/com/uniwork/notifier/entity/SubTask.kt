package com.uniwork.notifier.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sub_tasks")
open class SubTask(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null,

    @Column(name = "title", nullable = false, length = 100)
    open var title: String,

    @Column(name = "description", length = 500)
    open var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    open var status: Status = Status.PENDING,

    @Column(name = "created_at", nullable = false, updatable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    open var task: Task
)