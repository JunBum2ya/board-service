package com.midas.boardservice.article.domain

import com.midas.boardservice.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class Hashtag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private var id: Long? = null,
    @Column(nullable = false, length = 30, updatable = false, unique = true) val hashtagName: String
) : BaseEntity() {
    @ManyToMany(mappedBy = "hashtags") val articles = mutableListOf<Article>()
    fun getId(): Long? {
        return this.id
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hashtag

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}