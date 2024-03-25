package com.midas.boardservice.dto

import com.midas.boardservice.domain.Hashtag

data class HashtagDto(val id: Long? = null) {
    companion object {
        fun from(hashtag: Hashtag): HashtagDto {
            return HashtagDto(id = hashtag.id)
        }
    }
}
