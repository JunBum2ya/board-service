package com.midas.boardservice.member.domain

import com.midas.boardservice.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Comment
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
class Member(
    @Id @Column(nullable = false, length = 30) @Comment("사용자 아이디") private var id: String,
    @Column(nullable = false, length = 50, unique = true) @Comment("이메일") private var email: String,
    @Column(nullable = false, length = 30) @Comment("닉네임") private var nickname: String,
    @Column(nullable = false) @Comment("패스워드") private var password: String,
    private var memo: String? = null
): BaseEntity() {

    fun getId(): String {
        return this.id
    }
    fun getEmail(): String {
        return this.email
    }
    fun getNickname(): String {
        return this.nickname
    }
    fun getPassword(): String {
        return this.password
    }
    fun getMemo(): String? {
        return this.memo
    }

    /**
     * 필드 수정
     */
    fun update(email: String? = null, nickname: String? = null, memo: String? = null) {
        email?.let { this.email = it }
        nickname?.let { this.nickname = it }
        memo?.let { this.memo = it }
    }

    /**
     * 패스워드 수정
     */
    fun updatePassword(password: String, passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(password)
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