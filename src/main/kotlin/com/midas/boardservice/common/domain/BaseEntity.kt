package com.midas.boardservice.common.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class BaseEntity {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Comment("등록일시")
    private var createdAt: LocalDateTime? = null
    @CreatedBy
    @Column(nullable = false, updatable = false, length = 30)
    @Comment("등록자")
    private var createdBy: String? = null
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    @Comment("최종수정일시")
    private var updatedAt: LocalDateTime? = null
    @LastModifiedBy
    @Column(nullable = false, updatable = false, length = 30)
    @Comment("최종수정자")
    private var updatedBy: String? = null

    fun getCreatedAt(): LocalDateTime? {
        return this.createdAt
    }

    fun getCreatedBy(): String? {
        return this.createdBy
    }

    fun getUpdatedAt(): LocalDateTime? {
        return this.updatedAt
    }

    fun getUpdatedBy(): String? {
        return this.updatedBy
    }

}