package com.campus.board.dto.request

import com.campus.board.dto.ArticleDto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Length

class ArticleRequest(
    @NotBlank(message = "TITLE_IS_MANDATORY") var title: String,
    @NotBlank(message = "CONTENT_IS_MANDATORY") var content: String
) {

    fun toDto() : ArticleDto {
        return ArticleDto(title = title, content = content)
    }

}