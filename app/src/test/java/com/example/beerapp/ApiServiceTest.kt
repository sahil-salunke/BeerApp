package com.example.beerapp

import com.example.beerapp.data.remote.ApiService
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.mockito.ArgumentMatchers.anyInt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest : TestCase() {

    private lateinit var mockWebServer: MockWebServer

    private lateinit var apiService: ApiService

    override fun setUp() {
        super.setUp()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    /**
     * Testcase to check success response with no data
     */
    fun testApiForEmptyResponse() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getAllBears(anyInt(), anyInt())
        mockWebServer.takeRequest()

        assertEquals(true, response.body()?.isEmpty())
    }

    /**
     * Testcase to check success response with data
     */
    fun testApiForSuccessResponse() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getAllBears(1, 15)
        mockWebServer.takeRequest()

        assertEquals(false, response.body()?.isEmpty())
        assertEquals(15, response.body()?.size)
    }

    /**
     * Testcase to check 404 error response
     */
    fun testApiForError_404() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getAllBears(anyInt(), anyInt())
        mockWebServer.takeRequest()

        assertEquals(false, response.isSuccessful)
        assertEquals(404, response.code())
    }

    override fun tearDown() {
        super.tearDown()
        mockWebServer.shutdown()
    }

}