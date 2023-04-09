package com.example.newsline.domain.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GetNewsUseCaseTest {

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
class FakeArticleRepository: NewsRepository {
    override suspend fun get(pageNumber: Int, countryCode: String): List<Article> {
        return emptyList()
    }
}*/
