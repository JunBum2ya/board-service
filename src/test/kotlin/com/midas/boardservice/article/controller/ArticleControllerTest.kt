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
import com.midas.boardservice.article.repository.ArticleRepository
import com.midas.boardservice.article.service.ArticleService
import com.midas.boardservice.common.service.PaginationService
import com.midas.boardservice.config.TestSecurityConfig
import com.midas.boardservice.security.config.SecurityConfig
import com.midas.boardservice.util.FormDataEncoder
import com.midas.boardservice.util.TestAuthenticationPrincipal
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@WebMvcTest(controllers = [ArticleController::class])
@Import(TestSecurityConfig::class) // Security 필터 비활성화
class ArticleControllerTest(private val mockMvc: MockMvc, @MockBean private val articleService: ArticleService) :
    AnnotationSpec() {

    @Test
    fun `게시글을 등록에 성공하면 200 OK가 발생한다`() {
        mockMvc.perform(post("/api/articles")).andDo(MockMvcResultHandlers.print())
    }

}