package com.wenthor.model

import com.wenthor.enumeration.Role
import com.wenthor.enumeration.Status
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    val id: UUID?,
    @Embedded
    var baseEntity: BaseEntity,
    @Column(name = "full_name", nullable = false)
    val fullName: String,
    @Column(name = "normalized_name", nullable = false)
    var normalizedName: String,
    @Enumerated(EnumType.STRING)
    var status: Status?,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
    @ElementCollection
    @CollectionTable(name = "user_industries", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "industry_id")
    val industries: Set<UUID>,
    var accountExpired: Boolean,
    var accountLock: Boolean,
    var credentialSlock: Boolean,
    var enabled: Boolean
){
    constructor() : this(Builder())

    private constructor(builder: Builder) : this(
        builder.id,
        baseEntity = BaseEntity(
            LocalDateTime.now(),
            LocalDateTime.now(),
            builder.createdBy,
            builder.updatedBy
        ),
        builder.fullName,
        builder.normalizedName,
        builder.status,
        builder.email,
        builder.password,
        builder.role,
        builder.industries,
        builder.accountExpired,
        builder.accountLock,
        builder.credentialSlock,
        builder.enabled
    )

    class Builder {
        var id: UUID? = null
            private set
        var createdBy: UUID? = null
            private set
        var updatedBy: UUID? = null
            private set
        var fullName: String = ""
            private set
        var normalizedName: String = ""
            private set
        var status: Status = Status.PASSIVE
            private set
        var email: String = ""
            private set
        var password: String = ""
            private set
        var role: Role = Role.USER
            private set
        var industries: Set<UUID> = HashSet<UUID>()
            private set
        var accountExpired: Boolean = false
            private set
        var accountLock: Boolean = false
            private set
        var credentialSlock: Boolean = false
            private set
        var enabled: Boolean = true
            private set


        fun id(id: UUID?) = apply { this.id = id }
        fun createdBy(createdBy: UUID) = apply { this.createdBy = createdBy }
        fun updatedBy(updatedBy: UUID) = apply { this.updatedBy = updatedBy }
        fun fullName(fullName: String) = apply { this.fullName = fullName }
        fun normalizedName(normalizedName: String) = apply { this.normalizedName = normalizedName }
        fun status(status: Status) = apply { this.status = status }
        fun email(email: String) = apply { this.email = email }
        fun password(password: String) = apply { this.password = password }
        fun role(role: Role) = apply { this.role = role }
        fun industries(industries: Set<UUID>) = apply { this.industries = industries }
        fun accountExpired(accountExpired: Boolean) = apply { this.accountExpired = accountExpired }
        fun accountLock(accountLock: Boolean) = apply { this.accountLock = accountLock }
        fun credentialSlock(credentialSlock: Boolean) = apply { this.credentialSlock = credentialSlock }
        fun enabled(enabled: Boolean) = apply { this.enabled = enabled }
        fun build() = User(this)

    }
}

