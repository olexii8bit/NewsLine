package com.example.newsline.data.repository

import com.example.newsline.PAGE_SIZE
import com.example.newsline.domain.exceptionHandler.HandleError
import com.example.newsline.domain.exceptionHandler.NoInternetConnectionException
import com.example.newsline.domain.exceptionHandler.ServiceUnavailableException
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.models.Source
import com.example.newsline.domain.repository.RemoteArticleRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException

private const val TEST_TOTAL_ARTICLES = 31
private const val TEST_PAGE_SIZE = 10
private const val TEST_TOTAL_PAGES = 4

internal class ArticleRepositoryImplTest {

    private lateinit var fakeApiService: FakeApiService
    private lateinit var errorHandler: HandleError

    @BeforeEach
    fun setUp() {
        errorHandler = HandleError.DataError()
        fakeApiService = FakeApiService()
    }

    @Test
    fun get_returns_list_of_articles() {
        val remoteArticleRepository: RemoteArticleRepository =
            ArticleRepositoryImpl(
                errorHandler,
                fakeApiService)
        val expected: List<Article> = List(TEST_PAGE_SIZE) {
            Article(Source())
        }
        for (pageNumber in 1..TEST_TOTAL_PAGES) {
            val actual = runBlocking{ remoteArticleRepository.get(pageNumber) }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun get_returns_empty_list() = runBlocking {
        val remoteArticleRepository: RemoteArticleRepository =
            ArticleRepositoryImpl(
                errorHandler,
                fakeApiService/*,
                fakeLocationService*/)
        val expected: List<Article> = emptyList()

        for (pageNumber in TEST_TOTAL_PAGES+1..10) {
            val actual = remoteArticleRepository.get(pageNumber)
            assertEquals(expected, actual)
        }
        for (pageNumber in -10..0) {
            val actual = remoteArticleRepository.get(pageNumber)
            assertEquals(expected, actual)
        }

        fakeApiService.changeHasResults(false)
        val actual = remoteArticleRepository.get(1)
        assertEquals(expected, actual)
    }

    @Test
    fun get_throws_exception(): Unit = runBlocking {
        assertThrows<NoInternetConnectionException>{
            fakeApiService.changeConnection(false)
            val remoteArticleRepository: RemoteArticleRepository =
                ArticleRepositoryImpl(
                    errorHandler,
                    fakeApiService/*,
                    fakeLocationService*/)
            remoteArticleRepository.get(1)
        }
        fakeApiService.changeConnection(true)

        assertThrows<ServiceUnavailableException> {
            fakeApiService.changeResponseStatus(false)
            val remoteArticleRepository: RemoteArticleRepository =
                ArticleRepositoryImpl(
                    errorHandler,
                    fakeApiService/*,
                    fakeLocationService*/)
            remoteArticleRepository.get(1)
        }
    }
}

class FakeApiService: com.example.newsline.data.newsApi.ApiService {

    private var isInternetConnection: Boolean = true
    private var statusOk: Boolean = true
    private var hasResults: Boolean = true

    fun changeConnection(isConnected: Boolean) { isInternetConnection = isConnected }

    fun changeResponseStatus(isOk: Boolean) { statusOk = isOk }

    fun changeHasResults(has: Boolean) {hasResults = has}

    private val baseResponse: com.example.newsline.domain.models.ResponseDTO =
        com.example.newsline.domain.models.ResponseDTO(
            status = "ok",
            totalResults = TEST_TOTAL_ARTICLES,
            articles = List(PAGE_SIZE) { Article(Source()) }
        )
    private val emptyResponse: com.example.newsline.domain.models.ResponseDTO =
        com.example.newsline.domain.models.ResponseDTO(
            status = "ok",
            totalResults = TEST_TOTAL_ARTICLES,
            articles = emptyList()
        )

    override suspend fun getHeadlines(
        country: String,
        category: String,
        sources: String,
        q: String,
        pageSize: Int,
        page: Int,
    ) : com.example.newsline.domain.models.ResponseDTO {
        println("get 1 $page")
        if (!isInternetConnection) throw UnknownHostException()
        if (!statusOk) throw Exception()
        if ((TEST_PAGE_SIZE * page) >= (baseResponse.totalResults + TEST_PAGE_SIZE) ||
            page <= 0) return emptyResponse
        if (!hasResults) return emptyResponse
        return baseResponse
    }
}