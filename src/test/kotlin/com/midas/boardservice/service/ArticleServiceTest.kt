package com.midas.boardservice.service

import com.midas.boardservice.repository.ArticleRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk

class ArticleServiceTest: BehaviorSpec({
    val articleRepository = mockk<ArticleRepository>()
    val articleService = ArticleService(articleRepository)


}) {
}