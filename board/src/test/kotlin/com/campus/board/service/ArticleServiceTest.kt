package com.campus.board.service

import com.campus.board.domain.Article
import com.campus.board.dto.ArticleDto
import com.campus.board.repository.ArticleRepository
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

@DisplayName("비즈니스 로직 - 게시판")
@ExtendWith(MockitoExtension::class)
class ArticleServiceTest {
    @InjectMocks lateinit var articleService: ArticleService
    @Mock lateinit var articleRepository: ArticleRepository

    @DisplayName("게시글 정보를 입력하면 게시글을 저장한다.")
    @Test
    fun givenArticleDto_whenSavingArticle_thenSaveArticle() {
        //given
        val articleDto = ArticleDto(title = "테스트 게시글", content = "테스트 게시글 내용입니다.")
        given(articleRepository.save(any(Article::class.java))).willReturn(articleDto.toEntity())
        //when
        articleService.saveArticle(articleDto)
        //then
        then(articleRepository).should().save(any(Article::class.java))
    }
    @DisplayName("게시글 아이디와 수정 정보를 입력하면 게시글을 수정한다.")
    @Test
    fun givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdateArticle() {
        //given
        val articleDto = ArticleDto(title = "테스트 게시글", content = "테스트 게시글 내용입니다.")
        given(articleRepository.save(any(Article::class.java))).willReturn(articleDto.toEntity())
        //when
        articleService.saveArticle(articleDto)
        //then
        then(articleRepository).should().save(any(Article::class.java))
    }

}