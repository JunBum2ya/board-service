package com.campus.board.controller

import com.campus.board.config.SecurityConfig
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig::class)
@WebMvcTest(ArticleController::class)
class ArticleControllerTest(@Autowired private val mvc : MockMvc) {

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() {
        // Given
        // When & Then
        mvc.perform( get("/articles"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("articles/index"))
    }

}