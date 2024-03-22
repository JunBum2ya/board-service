package com.midas.boardservice.domain

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
    fun update(title: String? = null, content: String? = null) {
        title?.let { this.title = it }
        content?.let { this.content = it }
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