package com.campus.board.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import lombok.ToString
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@ToString
@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class AuditingFields {
    @CreatedDate
    @Column(nullable = false)
    private var createdAt: LocalDateTime = LocalDateTime.now() //생성일
    @CreatedBy
    @Column(nullable = false, length = 100)
    private var createdBy: String = "" //생성자
    @LastModifiedDate
    @Column(nullable = false)
    private var modifiedAt: LocalDateTime = LocalDateTime.now()//수정일
    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private var modifiedBy: String = ""//수정자
}