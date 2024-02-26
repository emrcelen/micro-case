package com.wenthor.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
data class BaseEntity(
    @Column(nullable = false, updatable = false)
    val created: LocalDateTime?,
    var updated: LocalDateTime?,
    @Column(nullable = false, updatable = false)
    val createdBy: UUID?,
    var updatedBy: UUID?
) {
    constructor() : this(
        null,
        null,
        null,
        null
    )
}
