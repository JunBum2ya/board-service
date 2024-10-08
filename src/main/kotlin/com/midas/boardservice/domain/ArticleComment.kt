package com.midas.boardservice.domain

import com.midas.boardservice.member.domain.Member
import jakarta.persistence.*

@Entity
class ArticleComment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id")
    val article: Article,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "member_id", nullable = false)
    val member: Member,
    @Column(updatable = false) private var parentCommentId: Long? = null,
    @Column(nullable = false, length = 500) private var content: String
): BaseEntity() {
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = [CascadeType.ALL])
    val childComments = mutableSetOf<ArticleComment>()

    fun getId(): Long? {
        return this.id
    }

    fun getParentCommentId(): Long? {
        return this.parentCommentId
    }

    fun getContent(): String {
        return this.content
    }

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