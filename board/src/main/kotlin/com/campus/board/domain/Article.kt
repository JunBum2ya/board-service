package com.campus.board.domain

import jakarta.persistence.*
import lombok.ToString
import lombok.ToString.Exclude
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * id : 아이디
 * title : 제목
 * hashTag : 해시태그
 * content 본문
 */
@ToString
@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(
    indexes = [
        Index(columnList = "title"),
        Index(columnList = "hashtag"),
        Index(columnList = "createdAt"),
        Index(columnList = "createdBy")
    ]
)
class Article(
    @Column(nullable = false, length = 30) var title: String,
    var hashTag: String,
    @Column(nullable = false, length = 2000) var content: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long? = null
    @Exclude
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL])
    private val articleComments: MutableSet<ArticleComment> = linkedSetOf();
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

        other as Article

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    fun getId() : Long? {
        return this.id
    }

    fun getCreatedBy() : String {
        return this.createdBy
    }

}