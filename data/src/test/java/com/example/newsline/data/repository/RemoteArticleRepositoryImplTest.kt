package com.example.newsline.data.repository

import com.example.newsline.PAGE_SIZE
import com.example.newsline.domain.exceptionHandler.HandleError
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

internal class RemoteArticleRepositoryImplTest {

    private lateinit var fakeApiService: FakeApiService
    private lateinit var errorHandler: HandleError

    @BeforeEach
    fun setUp() {
        errorHandler = HandleError.DataError()
        fakeApiService = FakeApiService()
        /*fakeLocationService = FakeLocationService()*/
    }

    @Test
    fun e() {
        assertEquals(1, 1)
    }

    @Test
    fun returns_list_of_articles() {
        val remoteArticleRepository: RemoteArticleRepository =
            RemoteArticleRepositoryImpl(
                errorHandler,
                fakeApiService)
        val expected: List<Article> = List(TEST_PAGE_SIZE) {
            Article(Source())
        }
        for (pageNumber in 1..TEST_TOTAL_PAGES) {
            val actual = runBlocking{ remoteArticleRepository.getArticles(pageNumber) }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun returns_empty_list_when_pageNumber_exits_able_page_count() = runBlocking {
        val remoteArticleRepository: RemoteArticleRepository =
            RemoteArticleRepositoryImpl(
                errorHandler,
                fakeApiService/*,
                fakeLocationService*/)
        val expected: List<Article> = emptyList()
        for (pageNumber in TEST_TOTAL_PAGES+1..10) {
            val actual = remoteArticleRepository.getArticles(pageNumber)
            assertEquals(expected, actual)
        }
        for (pageNumber in -10..0) {
            val actual = remoteArticleRepository.getArticles(pageNumber)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun returns_empty_list_when_no_results() = runBlocking {
        fakeApiService.changeHasResults(false)
        val remoteArticleRepository: RemoteArticleRepository =
            RemoteArticleRepositoryImpl(
                errorHandler,
                fakeApiService/*,
                fakeLocationService*/)
        val expected: List<Article> = emptyList()
        val actual = remoteArticleRepository.getArticles(1)
        assertEquals(expected, actual)
    }

    @Test
    fun throws_exception_when_UnknownHostException(): Unit = runBlocking {
        assertThrows<com.example.newsline.domain.exceptionHandler.NoInternetConnectionException>{
            fakeApiService.changeConnection(false)
            val remoteArticleRepository: RemoteArticleRepository =
                RemoteArticleRepositoryImpl(
                    errorHandler,
                    fakeApiService/*,
                    fakeLocationService*/)
            remoteArticleRepository.getArticles(1)
        }
    }

    @Test
    fun throws_exception_when_ApiService_status_is_error(): Unit = runBlocking {
        assertThrows<com.example.newsline.domain.exceptionHandler.ServiceUnavailableException> {
            fakeApiService.changeResponseStatus(false)
            val remoteArticleRepository: RemoteArticleRepository =
                RemoteArticleRepositoryImpl(
                    errorHandler,
                    fakeApiService/*,
                    fakeLocationService*/)
            remoteArticleRepository.getArticles(1)
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