package com.midas.boardservice.article.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.article.dto.ArticleDto
import com.midas.boardservice.article.dto.HashtagDto
import com.midas.boardservice.article.dto.request.ArticleRequest
import com.midas.boardservice.article.service.ArticleService
import com.midas.boardservice.security.TestSecurityConfig
import com.midas.boardservice.member.dto.MemberDto
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ArticleController::class])
@Import(TestSecurityConfig::class)
class ArticleControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @MockkBean private val articleService: ArticleService
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        describe("게시글 저장 : [POST] /api/articles") {
            val memberDto = MemberDto(id = "test", email = "test@test.com", password = "1234", nickname = "test")
            val articleDto = ArticleDto(
                articleId = 1L,
                memberDto = memberDto,
                title = "title",
                content = "content",
                hashtags = setOf(HashtagDto(hashtagName = "test"))
            )
            val request = ArticleRequest(title = "title", content = "content", hashtags = listOf("test"))
            it("200 OK") {
                every { articleService.saveArticle(any(ArticleDto::class)) }.returns(articleDto)
                mockMvc.perform(
                    post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("authorization", "Bearer abc")
                )
                    .andExpect { status().isOk }
                    .andExpect { jsonPath("$.id").value(1L) }
                    .andExpect { jsonPath("$.title").value("title") }
                verify { articleService.saveArticle(any(ArticleDto::class)) }
            }

            it("401 Unauthorized") {
                mockMvc.perform(
                    post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto))
                ).andExpect { status().isUnauthorized }
            }
        }
    }
}