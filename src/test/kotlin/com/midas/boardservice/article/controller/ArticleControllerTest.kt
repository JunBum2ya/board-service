package com.midas.boardservice.article.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.article.domain.Article
import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.domain.contant.FormStatus
import com.midas.boardservice.article.domain.constant.SearchType
import com.midas.boardservice.article.dto.ArticleDto
import com.midas.boardservice.article.dto.ArticleWithCommentsDto
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.article.dto.request.ArticleRequest
import com.midas.boardservice.article.service.ArticleService
import com.midas.boardservice.common.service.PaginationService
import com.midas.boardservice.util.FormDataEncoder
import com.midas.boardservice.util.TestAuthenticationPrincipal
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@WebMvcTest
class ArticleControllerTest : DescribeSpec({
    val articleService = mockk<ArticleService>()
    val articleController = ArticleController(articleService)

    val mvc = MockMvcBuilders.standaloneSetup(articleController)
        .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver(),TestAuthenticationPrincipal())
        .build()

}) {
    companion object {
        fun buildArticleDto(): ArticleDto {
            return ArticleDto.from(buildArticle())
        }

        fun buildMemberDto(): MemberDto {
            return MemberDto.from(buildMember())
        }

        private fun buildArticle(): Article {
            return Article(
                id = 1L,
                member = buildMember(),
                title = "title",
                content = "content"
            )
        }

        private fun buildMember(): Member {
            return Member(id = "test", email = "test@test.com", nickname = "test", password = "1234")
        }
    }
}