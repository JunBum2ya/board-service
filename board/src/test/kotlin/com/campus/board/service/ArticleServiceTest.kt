package com.campus.board.service

import com.campus.board.domain.Article
import com.campus.board.domain.constant.SearchType
import com.campus.board.dto.ArticleDto
import com.campus.board.dto.ArticleSearchParam
import com.campus.board.repository.ArticleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@DisplayName("비즈니스 로직 - 게시판")
@ExtendWith(MockitoExtension::class)
class ArticleServiceTest {
    @InjectMocks lateinit var articleService: ArticleService
    @Mock lateinit var articleRepository: ArticleRepository

    @DisplayName("빈 게시글 검색 파라미터를 입력하면 전체 검색을 한 후 빈 페이지를 반환한다.")
    @Test
    fun givenEmptyArticleDto_whenSearchArticles_thenReturnsEmptyArticles() {
        //given
        given(articleRepository.findAll(any(Pageable::class.java))).willReturn(Page.empty())
        //when
        val emptyPage = articleService.searchArticles(searchType = SearchType.NONE, pageable = Pageable.ofSize(10))
        //then
        assertThat(emptyPage).isNotNull
        assertThat(emptyPage.totalPages).isEqualTo(1)
        assertThat(emptyPage.pageable.isUnpaged).isTrue
        then(articleRepository).should().findAll(any(Pageable::class.java))
    }

    @DisplayName("게시글 정보를 입력하면 게시글을 저장한다.")
    @Test
    fun givenArticleDto_whenSavingArticle_thenSaveArticle() {
        //given
        val articleDto = ArticleDto(title = "테스트 게시글", content = "테스트 게시글 내용입니다.")
        given(articleRepository.save(any(Article::class.java))).willReturn(articleDto.toEntity())
        //when
        articleService.createArticle(articleDto)
        //then
        then(articleRepository).should().save(any(Article::class.java))
    }
    @DisplayName("게시글 아이디와 수정 정보를 입력하면 게시글을 수정한다.")
    @Test
    fun givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdateArticle() {
        //given
        val articleDto = ArticleDto(id = 1L,title = "테스트 게시글", content = "테스트 게시글 내용입니다.")
        given(articleRepository.getReferenceById(articleDto.id?:throw Exception("id 없음"))).willReturn(articleDto.toEntity());
        //when
        articleService.updateArticle(articleDto.id?:throw Exception("id 없음"),articleDto)
        //then
        then(articleRepository).should().getReferenceById(articleDto.id?:throw Exception("id 없음"))
    }

    @DisplayName("게시글 아이디와 수정 정보를 입력하면 게시글을 수정한다.")
    @Test
    fun givenArticleId_whenDeletingArticle_thenNothing() {
        //given
        val articleId = any(Long::class.java)
        given(articleRepository.deleteById(articleId)).will {  }
        //when
        articleService.deleteArticle(articleId)
        //then
        then(articleRepository).should().deleteById(articleId)
    }

}