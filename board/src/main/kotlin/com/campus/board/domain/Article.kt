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
@Entity
@Table(
    indexes = [
        Index(columnList = "title"),
        Index(columnList = "createdAt"),
        Index(columnList = "createdBy")
    ]
)
class Article(
    @Column(nullable = false) var title: String,
    @Column(nullable = false, length = 2000) var content: String
) : AuditingFields() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long? = null
    @Exclude
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL])
    private val articleComments: MutableSet<ArticleComment> = linkedSetOf();

    @ManyToMany(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    @JoinTable(
        name = "article_hashtags",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "hashtag_id")]
    )
    var hashtags: MutableSet<Hashtag> = mutableSetOf()

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

}