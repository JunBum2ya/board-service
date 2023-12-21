package com.campus.board.controller

import com.campus.board.config.TestSecurityConfig
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
class DataRestTest(@Autowired val mvc : MockMvc) {
    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticleCommentsJsonResponse() {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
    }

}