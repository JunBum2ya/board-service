package com.campus.board.domain

import jakarta.persistence.*
import lombok.ToString
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * 댓글 도메인
 * id : 아이디
 * article : 게시글
 * content : 본문
 */
@ToString
@Entity
@Table(
    indexes = [
        Index(columnList = "createdAt"),
        Index(columnList = "createdBy")
    ]
)
class ArticleComment(
    @ManyToOne(fetch = FetchType.LAZY, optional = false) val article: Article,
    @Column(nullable = false, length = 1000) var content: String
) : AuditingFields() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long? = null

    fun getId() : Long? {
        return this.id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleComment

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}