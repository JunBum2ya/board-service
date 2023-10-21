package com.campus.board.domain

import java.time.LocalDateTime

class Article(val id : Long,var title : String,var hashTag : String,var content : String) {
    var createdAt : LocalDateTime = LocalDateTime.now()
    var createdBy : String = ""
    var modifiedAt : LocalDateTime? = null
    var modifiedBy : String? = ""
}