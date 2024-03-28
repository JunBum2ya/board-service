package com.midas.boardservice.domain.contant

enum class FormStatus(val description: String, val update: Boolean) {
    CREATE(description = "저장", update = false), UPDATE(description = "수정", update = true)
}