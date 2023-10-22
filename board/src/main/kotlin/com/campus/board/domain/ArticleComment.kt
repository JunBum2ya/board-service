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
@EntityListeners(AuditingEntityListener::class)
@Table(
    indexes = [
        Index(columnList = "createdAt"),
        Index(columnList = "createdBy")
    ]
)
class ArticleComment(
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "article_id") var article: Article,
    @Column(nullable = false, length = 1000) var content: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long? = null

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