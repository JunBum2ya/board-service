package com.campus.board.domain

import jakarta.persistence.*
import lombok.ToString

@ToString
@Entity
@Table(
    indexes = [
        Index(columnList = "email", unique = true),
        Index(columnList = "createdAt"),
        Index(columnList = "createdBy")
    ]
)
class Member(
    @Id @Column(length = 50) private var id: String,
    @Column(nullable = false) var password: String,
    @Column(length = 100) var email: String,
    @Column(length = 100) var nickname: String,
    var memo : String? = null
) : AuditingFields() {

    fun getId() : String {
        return this.id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}