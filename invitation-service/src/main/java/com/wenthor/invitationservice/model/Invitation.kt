package com.wenthor.invitationservice.model

import com.wenthor.invitationservice.enumeration.Status
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "invitation")
data class Invitation(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    val id: UUID?,
    val message: String,
    val code: String,
    @Enumerated(EnumType.STRING)
    val status: Status,
    var user_id: UUID?,
    @Column(nullable = false, updatable = false)
    val created: LocalDateTime,
    var updated: LocalDateTime,
    @Column(nullable = false, updatable = false)
    val createdBy: UUID?,
    var updatedBy: UUID?
) {
    constructor(): this(Builder())

    private constructor(builder:Builder): this(
        builder.id,
        builder.message,
        builder.code,
        builder.status,
        builder.user_id,
        builder.created,
        builder.updated,
        builder.createdBy,
        builder.updatedBy
    )

    class Builder {
        var id: UUID? = null
            private set
        var message: String = ""
            private set
        var code: String = ""
            private set
        var status:Status = Status.PENDING
            private set
        var user_id: UUID? = null
            private set
        var created:LocalDateTime = LocalDateTime.now()
            private set
        var updated:LocalDateTime = LocalDateTime.now()
            private set
        var createdBy: UUID? = null
            private set
        var updatedBy: UUID? = null
            private set

        fun id(id:UUID?) = apply { this.id = id }
        fun message(message: String) = apply { this.message = message }
        fun code(code: String) = apply { this.code = code }
        fun status(status: Status) = apply { this.status = status }
        fun user(user_id: UUID) = apply { this.user_id = user_id }
        fun createdDate(created: LocalDateTime) = apply{this.created = created}
        fun updatedDate(updated: LocalDateTime) = apply { this.updated = updated }
        fun createdBy(createdBy: UUID) = apply { this.createdBy = createdBy }
        fun updatedBy(updatedBy: UUID) = apply { this.updatedBy = updatedBy }

    }
}
