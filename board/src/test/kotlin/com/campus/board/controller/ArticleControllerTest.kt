package com.campus.board.controller

import com.campus.board.config.TestSecurityConfig
import com.campus.board.domain.constant.SearchType
import com.campus.board.service.ArticleService
import com.campus.board.service.PaginationService
import com.fastcampus.projectboard.util.FormDataEncoder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@DisplayName("View 컨트롤러 - 게시글")
@Import(TestSecurityConfig::class, FormDataEncoder::class)
@WebMvcTest(ArticleController::class)
class ArticleControllerTest(@Autowired val mvc: MockMvc, @Autowired formDataEncoder: FormDataEncoder) {

    @MockBean
    lateinit var articleService: ArticleService

    @MockBean
    lateinit var paginationService: PaginationService

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() {
        // Given
        given(
            articleService.searchArticles(
                searchType = SearchType.NONE,
                searchValue = null,
                pageable = any(Pageable::class.java)
            )
        ).willReturn(Page.empty())
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(listOf(0, 1, 2, 3, 4))

        // When & Then
        mvc.perform(get("/articles"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("articles/index"))
            .andExpect(model().attributeExists("articles"))
            .andExpect(model().attributeExists("paginationBarNumbers"))
            .andExpect(model().attributeExists("searchTypes"))
        then(articleService).should()
            .searchArticles(
                searchType = SearchType.NONE,
                searchValue = null,
                pageable = any(Pageable::class.java)
            )
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt())
    }

}