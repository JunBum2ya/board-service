package com.midas.boardservice.domain

import jakarta.persistence.*

@Entity
class ArticleComment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    val article: Article,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "member_id", nullable = false)
    val member: Member,
    @Column(updatable = false) private var parentCommentId: Long,
    @Column(nullable = false, length = 500) private var content: String
) {
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = [CascadeType.ALL])
    private val childComments = LinkedHashSet<ArticleComment>()

    /**
     * 수정 메소드
     */
    fun update(content: String? = null) {
        content?.let { this.content = it }
    }

    fun addChildComment(child: ArticleComment) {
        child.parentCommentId = id ?: -1
        this.childComments.add(child)
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