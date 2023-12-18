package com.campus.board.domain

import jakarta.persistence.*
import org.hibernate.mapping.Index

@Entity
@Table(indexes = [
    Index(columnList = "name", unique = true),
    Index(columnList = "createdAt"),
    Index(columnList = "createdBy")
])
class Hashtag(@Column(nullable = false) var name : String) : AuditingFields() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hashtag_id", nullable = false)
    var id: Long? = null
    @ManyToMany(mappedBy = "hashtags")
    private val articles : Set<Article> = LinkedHashSet<Article>();
}