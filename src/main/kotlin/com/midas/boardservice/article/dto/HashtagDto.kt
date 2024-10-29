package com.midas.boardservice.article.dto

import com.midas.boardservice.article.domain.Hashtag

data class HashtagDto(val id: Long? = null, val hashtagName: String) {
    companion object {
        fun from(hashtag: Hashtag): HashtagDto {
            return HashtagDto(id = hashtag.getId(), hashtagName = hashtag.hashtagName)
        }
    }

    fun toEntity(): Hashtag {
        return Hashtag(hashtagName = hashtagName)
    }
}
