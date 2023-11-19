package com.campus.board.repository

import com.campus.board.config.JpaConfig
import com.campus.board.domain.Article
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("testdb")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 테스트")
@Import(value = [JpaConfig::class])
@DataJpaTest
class ArticleRepositoryTest(@Autowired private val articleRepository : ArticleRepository,@Autowired private val articleCommentRepository: ArticleCommentRepository) {

    var testArticle : Article? = null

    @BeforeEach
    fun saveDefaultArticle() {
        testArticle = Article(title = "test 게시글", hashTag = "#test #jpa #database", content = "첫번째 게시글이 될것 같습니다.")
        articleRepository.save(testArticle?:throw Exception("DB ERROR"))
    }

    @AfterEach
    fun deleteDefaultArticle() {
        articleRepository.deleteAll()
    }

    @DisplayName("select test")
    @Test
    fun given_whenSelect_thenWorksFine() {
        val articles = articleRepository.findAll()
        assertThat(articles).isNotNull.hasSize(1)
    }

    @DisplayName("select by id test")
    @Test
    fun given_whenSelectById_thenWorksFine() {
        val article = articleRepository.findByIdOrNull(testArticle?.getId()?:throw Exception("DB ERROR"))
        assertThat(article).isNotNull
    }

    @DisplayName("insert test")
    @Test
    fun given_whenInsert_thenWorksFine() {
        val article = Article(title = "test 게시글", hashTag = "#test #jpa #database", content = "첫번째 게시글이 될것 같습니다.")
        val savedArticle = articleRepository.save(article)
        assertThat(savedArticle).isNotNull
        assertThat(article.getId()).isNotNull()
        assertThat(savedArticle.getId()).isNotNull()
        assertThat(savedArticle).isEqualTo(article)
    }

    @DisplayName("update test")
    @Test
    fun given_whenUpdate_thenWorksFine() {
        val article = articleRepository.findByIdOrNull(testArticle?.getId()?:throw Exception("DB ERROR"))?:throw Exception("1번이 없음")
        article.content = "test 34"
        val contrastArticle = articleRepository.findByIdOrNull(testArticle?.getId()?:throw Exception("DB ERROR"))?:throw Exception("1번이 없음")
        assertThat(article).isEqualTo(contrastArticle)
        assertThat(article.content).isEqualTo(contrastArticle.content)
    }

    @DisplayName("delete test")
    @Test
    fun given_whenDelete_thenWorksFine() {
        val article = articleRepository.findByIdOrNull(testArticle?.getId()?:throw Exception("DB ERROR"))?:throw Exception("1번이 없음")
        articleRepository.delete(article)
        assertThat(articleRepository.count()).isEqualTo(0)
        assertThat(article).hasFieldOrPropertyWithValue("id",testArticle?.getId()?:throw Exception("DB ERROR"))
    }
}