package com.example.newsline.data.repository

import com.example.newsline.PAGE_SIZE
import com.example.newsline.LocationService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException

// todo make many variants of api service that imitate every behavior case
internal class RemoteArticleRepositoryImplTest {
    /*private val testGoodResponseDto: ResponseDTO = ResponseDTO(
        status = "ok",
        totalResults = 20,
        articles = List(PAGE_SIZE) {Article(Source())}
        )*/



    private lateinit var fakeApiService: FakeApiService
    private lateinit var errorHandler: com.example.newsline.domain.exceptionHandler.HandleError
    /*private lateinit var fakeLocationService: FakeLocationService*/

    @BeforeEach
    fun setUp() {
        errorHandler = com.example.newsline.domain.exceptionHandler.HandleError.DataError()
        fakeApiService = FakeApiService()
        /*fakeLocationService = FakeLocationService()*/
    }

    @Test
    fun returns_list_of_articles() {
        val remoteArticleRepository: com.example.newsline.domain.repository.RemoteArticleRepository =
            com.example.newsline.data.repository.RemoteArticleRepositoryImpl(
                errorHandler,
                fakeApiService/*,
                fakeLocationService*/)
        val expected: List<com.example.newsline.domain.models.Article> = List(PAGE_SIZE) {
            com.example.newsline.domain.models.Article(com.example.newsline.domain.models.Source())
        }
        for (pageNumber in 1..3) {
            val actual = runBlocking{ remoteArticleRepository.getArticles(pageNumber) }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun returns_empty_list_when_pageNumber_exits_able_page_count() = runBlocking {
        val remoteArticleRepository: com.example.newsline.domain.repository.RemoteArticleRepository =
            com.example.newsline.data.repository.RemoteArticleRepositoryImpl(
                errorHandler,
                fakeApiService/*,
                fakeLocationService*/)
        val expected: List<com.example.newsline.domain.models.Article> = emptyList()
        for (pageNumber in 4..6) {
            val actual = remoteArticleRepository.getArticles(pageNumber)
            assertEquals(expected, actual)
        }
        for (pageNumber in -6..0) {
            val actual = remoteArticleRepository.getArticles(pageNumber)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun returns_empty_list_when_no_results() = runBlocking {
        fakeApiService.changeHasResults(false)
        val remoteArticleRepository: com.example.newsline.domain.repository.RemoteArticleRepository =
            com.example.newsline.data.repository.RemoteArticleRepositoryImpl(
                errorHandler,
                fakeApiService/*,
                fakeLocationService*/)
        val expected: List<com.example.newsline.domain.models.Article> = emptyList()
        val actual = remoteArticleRepository.getArticles(1)
        assertEquals(expected, actual)
    }

    @Test
    fun throws_exception_when_UnknownHostException(): Unit = runBlocking {
        assertThrows<com.example.newsline.domain.exceptionHandler.NoInternetConnectionException>{
            fakeApiService.changeConnection(false)
            val remoteArticleRepository: com.example.newsline.domain.repository.RemoteArticleRepository =
                com.example.newsline.data.repository.RemoteArticleRepositoryImpl(
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
            val remoteArticleRepository: com.example.newsline.domain.repository.RemoteArticleRepository =
                com.example.newsline.data.repository.RemoteArticleRepositoryImpl(
                    errorHandler,
                    fakeApiService/*,
                    fakeLocationService*/)
            remoteArticleRepository.getArticles(1)
        }
    }
}

/*class FakeLocationService: LocationService {
    override fun getCurrentLocationCountry() = "us"
    override fun getCurrentCountryCode() = com.example.newsline.data.newsApi.enums.Country.US
}*/

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
            totalResults = 30,
            articles = List(PAGE_SIZE) { com.example.newsline.domain.models.Article(com.example.newsline.domain.models.Source()) }
        )
    private val emptyResponse: com.example.newsline.domain.models.ResponseDTO =
        com.example.newsline.domain.models.ResponseDTO(
            status = "ok",
            totalResults = 30,
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
        if ((PAGE_SIZE * page) > baseResponse.totalResults || page <= 0) return emptyResponse
        if (!hasResults) return emptyResponse
        return baseResponse
    }
}