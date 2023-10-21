package com.campus.board.domain

import java.time.LocalDateTime

class ArticleComment(var id : Long,var article : Article,var content : String) {
    var createdAt : LocalDateTime = LocalDateTime.now()
    var createdBy : String = ""
    var modifiedAt : LocalDateTime? = null
    var modifiedBy : String? = ""
}