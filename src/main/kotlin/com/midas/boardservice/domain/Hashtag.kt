package com.midas.boardservice.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class Hashtag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    val hashtagName: String
) : BaseEntity() {
    @ManyToMany(mappedBy = "hashtags") private val articles = LinkedHashSet<Article>()
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