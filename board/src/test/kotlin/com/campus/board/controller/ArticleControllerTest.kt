package com.campus.board.controller

import com.campus.board.config.SecurityConfig
import com.campus.board.dto.ArticleSearchParam
import com.campus.board.repository.ArticleRepository
import com.campus.board.service.ArticleService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig::class)
@WebMvcTest(ArticleController::class)
class ArticleControllerTest {

    @InjectMocks lateinit var articleService: ArticleService
    @Mock lateinit var articleRepository: ArticleRepository

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    @Throws(Exception::class)
    fun givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() {
        // Given
//        val articleSearchParam = ArticleSearchParam()
//        given(articleService.searchArticles(articleSearchParam)).willReturn(Page.empty())
//        // When & Then
//        mvc.perform( get("/articles"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//            .andExpect(view().name("articles/index"))
//        then(articleService).should().searchArticles(articleSearchParam)
    }

}