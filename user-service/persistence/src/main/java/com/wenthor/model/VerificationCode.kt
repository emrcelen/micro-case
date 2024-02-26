package com.wenthor.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "verification_codes")
data class VerificationCode(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    val id: UUID?,
    @Embedded
    var baseEntity: BaseEntity,
    @Column(nullable = false, unique = true)
    val code: String,
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,
    val expiryDate: LocalDateTime
) {

    constructor() : this(Builder())

    private constructor(builder: Builder): this(
        builder.id,
        BaseEntity(
            LocalDateTime.now(),
            LocalDateTime.now(),
            builder.createdBy,
            builder.updatedBy
        ),
        builder.code,
        builder.user,
        builder.expiryDate
    )

    class Builder {
        var id: UUID? = null
            private set
        var createdBy: UUID? = null
            private set
        var updatedBy: UUID? = null
            private set
        var code: String = ""
            private set
        var user: User = User()
            private set
        var expiryDate: LocalDateTime = LocalDateTime.now().plusMinutes(5)
            private set

        fun id(id: UUID?) = apply { this.id = id }
        fun createdBy(createdBy: UUID) = apply { this.createdBy = createdBy }
        fun updatedBy(updatedBy: UUID) = apply { this.updatedBy = updatedBy }
        fun code(code: String) = apply { this.code = code }
        fun user(user:User) = apply { this.user = user }
        fun expiryDate(expiryDate: LocalDateTime) = apply { this.expiryDate = expiryDate }
        fun build() = VerificationCode(this)
    }
}
