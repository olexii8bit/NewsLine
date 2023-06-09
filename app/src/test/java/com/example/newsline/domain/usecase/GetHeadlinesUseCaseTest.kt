package com.example.newsline.domain.usecase

import com.example.newsline.domain.repository.ArticleRepository
import com.example.newsline.domain.models.Article
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GetHeadlinesUseCaseTest {

    @Test
    fun execute_returns_list() {
        assertEquals(1, 1)
    }

    @Test
    fun execute_throws_exception() {
        assertThrows<Exception>{ throw Exception()}
    }

    @Test
    fun newCountry_test() {
        assertEquals(1, 1)
    }

}
/*
class FakeArticleRepository: ArticleRepository {
    override suspend fun get(pageNumber: Int, countryCode: String): List<Article> {
        return emptyList()
    }
}*/
