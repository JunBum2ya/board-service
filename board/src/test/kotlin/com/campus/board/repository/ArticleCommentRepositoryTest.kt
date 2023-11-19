package com.campus.board.repository

import com.campus.board.config.JpaConfig
import com.campus.board.domain.Article
import com.campus.board.domain.ArticleComment
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("testdb")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 테스트")
@Import(value = [JpaConfig::class])
@DataJpaTest
class ArticleCommentRepositoryTest(@Autowired val articleRepository: ArticleRepository,@Autowired val articleCommentRepository: ArticleCommentRepository) {

    var testArticleComment : ArticleComment? = null

    @BeforeEach
    fun saveTestData() {
        val article = Article(title = "test 게시글", hashTag = "#test #jpa #database", content = "첫번째 게시글이 될것 같습니다.")
        articleRepository.save(article)
        testArticleComment = ArticleComment(article = article, content = "첫번째 게시글의 덧글")
        articleCommentRepository.save(ArticleComment(article = article, content = "첫번째 게시글의 첫번째 게시글"))
        articleCommentRepository.save(testArticleComment?:throw Exception("TEST ERROR"))
    }

    @AfterEach
    fun deleteTestData() {
        articleCommentRepository.deleteAll()
        articleRepository.deleteAll()
    }

    @DisplayName("select test")
    @Test
    fun given_whenSelect_thenWorksFine() {
        val articleComments = articleCommentRepository.findAll()
        Assertions.assertThat(articleComments).isNotNull.hasSize(2)
    }

    @DisplayName("select by id test")
    @Test
    fun given_whenSelectById_thenWorksFine() {
        val article = articleCommentRepository.findByIdOrNull(testArticleComment?.getId()?:throw Exception("TEST ERROR"))
        Assertions.assertThat(article).isNotNull
    }

    @DisplayName("insert test")
    @Test
    fun given_whenInsert_thenWorksFine() {
        val article = testArticleComment?.article?:throw Exception("TEST : ERROR")
        val savedArticleComment = articleCommentRepository.save(ArticleComment(article = article, content = "게시글에 덧글 추가"))
        Assertions.assertThat(savedArticleComment).isNotNull
        Assertions.assertThat(savedArticleComment.article).isNotNull
        Assertions.assertThat(savedArticleComment.article).isEqualTo(article)
    }

    @DisplayName("update test")
    @Test
    fun given_whenUpdate_thenWorksFine() {
        val articleComment = articleCommentRepository.findByIdOrNull(testArticleComment?.getId()?:throw Exception("DB ERROR"))?:throw Exception("1번이 없음")
        articleComment.content = "test 34"
        val contrastArticleComment = articleCommentRepository.findByIdOrNull(articleComment.getId() ?:throw Exception("DB ERROR"))?:throw Exception("1번이 없음")
        Assertions.assertThat(articleComment).isEqualTo(contrastArticleComment)
        Assertions.assertThat(articleComment.content).isEqualTo(contrastArticleComment.content)
    }

    @DisplayName("delete test")
    @Test
    fun given_whenDelete_thenWorksFine() {
        val articleComment = articleCommentRepository.findByIdOrNull(testArticleComment?.getId()?:throw Exception("DB ERROR"))?:throw Exception("1번이 없음")
        articleCommentRepository.delete(articleComment)
        Assertions.assertThat(articleCommentRepository.findByIdOrNull(testArticleComment?.getId())).isNull()
        Assertions.assertThat(articleComment).hasFieldOrPropertyWithValue("id",testArticleComment?.getId()?:throw Exception("DB ERROR"))
    }

}