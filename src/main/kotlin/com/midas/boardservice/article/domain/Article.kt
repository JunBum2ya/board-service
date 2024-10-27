package com.midas.boardservice.article.domain

import com.midas.boardservice.common.domain.BaseEntity
import com.midas.boardservice.member.domain.Member
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Comment("대체키")
    private var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,
    @Column(nullable = false, length = 100) private var title: String,
    @Column(nullable = false, length = 10000) private var content: String
) : BaseEntity() {
    @JoinTable(
        name = "article_hashtag",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "hashtag_id")]
    )
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val hashtags = mutableListOf<Hashtag>()

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL])
    val articleComments = mutableSetOf<ArticleComment>()

    fun getId(): Long? {
        return this.id
    }

    fun getTitle(): String {
        return this.title
    }

    fun getContent(): String {
        return this.content
    }

    fun update(title: String? = null, content: String? = null) {
        title?.let { this.title = it }
        content?.let { this.content = it }
    }

    fun addHashtag(hashtag: Hashtag) {
        this.hashtags.add(hashtag)
    }

    fun addHashtags(hashtags: Collection<Hashtag>) {
        this.hashtags.addAll(hashtags)
    }

    fun clearHashtags() {
        this.hashtags.clear()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Article

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}