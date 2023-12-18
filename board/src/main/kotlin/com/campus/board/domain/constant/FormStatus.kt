package com.campus.board.domain.constant

enum class FormStatus(val description : String,val update : Boolean) {
    CREATE(description = "생성", update = false),UPDATE(description = "수정", update = true)
}