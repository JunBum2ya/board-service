package com.midas.boardservice.dto

import com.midas.boardservice.domain.Hashtag

data class HashtagDto(val id: Long? = null, val hashtagName: String) {
    companion object {
        fun from(hashtag: Hashtag): HashtagDto {
            return HashtagDto(id = hashtag.id, hashtagName = hashtag.hashtagName)
        }
    }
}