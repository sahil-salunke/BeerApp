package com.example.beerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.beerapp.Helper
import com.example.beerapp.MockitoUtils
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.data.repository.BeerListRepositoryImpl
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.RemoteKeyDAO
import junit.framework.TestCase
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class BeerListRepositoryImplTest : TestCase() {

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var beerDAO: BeerDAO

    @Mock
    private lateinit var remoteKeyDAO: RemoteKeyDAO

    override fun setUp() {
        super.setUp()
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Testcase to check success response with no data
     */
    @OptIn(ExperimentalPagingApi::class)
    fun testApiForEmptyResponse() = kotlinx.coroutines.test.runTest {

        Mockito.`when`(apiService.getAllBears(anyInt(), anyInt()))
            .thenReturn(Response.success(emptyList()))

        val repo = BeerListRepositoryImpl(apiService, beerDAO, remoteKeyDAO)
        val result = repo.getBeerList()

        assertEquals(true, result)
//        Assert.assertEquals(true, result.value)
//        Assert.assertEquals(0, result.data!!.size)
//
//        val mockResponse = MockResponse()
//        mockResponse.setBody("[]")
//        mockWebServer.enqueue(mockResponse)
//
//        val response = apiService.getAllBears(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
//        mockWebServer.takeRequest()
//
//        assertEquals(true, response.body()?.isEmpty())
    }

    /**
     * Testcase to check success response with data
     */
    @OptIn(ExperimentalPagingApi::class)
    fun testApiForSuccessResponse() = kotlinx.coroutines.test.runTest {
//        val content = Helper.readFileResource("/response.json")
//        Mockito.`when`(apiService.getAllBears(anyInt(), anyInt()))
//            .thenReturn(Response.success())
//
//        val repo = BeerListRepositoryImpl(apiService, beerDAO, remoteKeyDAO)
//        val result = repo.getBeerList()


        //        val mockResponse = MockResponse()
//        val content = Helper.readFileResource("/response.json")
//        mockResponse.setResponseCode(200)
//        mockResponse.setBody(content)
//        mockWebServer.enqueue(mockResponse)
//
//        val response = apiService.getAllBears(1, 15)
//        mockWebServer.takeRequest()
//
//        assertEquals(false, response.body()?.isEmpty())
//        assertEquals(15, response.body()?.size)
    }

    /**
     * Testcase to check 404 error response
     */
    fun testApiForError_404() = kotlinx.coroutines.test.runTest {
//        val mockResponse = MockResponse()
//        mockResponse.setResponseCode(404)
//        mockWebServer.enqueue(mockResponse)
//
//        val response = apiService.getAllBears(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
//        mockWebServer.takeRequest()
//
//        assertEquals(false, response.isSuccessful)
//        assertEquals(404, response.code())
    }


    private val testObserver: Observer<PagingData<BeerDTO>> = MockitoUtils().mock()

//    @OptIn(ExperimentalPagingApi::class)
//    @Test
//    fun testRepositoryLiveDataPagingAPI() {
//        val repository = BeerListRepositoryImpl(apiService, beerDAO, remoteKeyDAO)
//        val pagingDataLiveData = repository.getBeerList()
//        assertNotNull(pagingDataLiveData)
//
//        val testObserver = Observer<PagingData<BeerDTO>>()
//        pagingDataLiveData.observeForever(testObserver)
//
//        repository.getBeerList() // Load the initial data
//
//        testObserver.assertValueCount(1)
//        val pagingData = testObserver.values[0]
//        assertEquals(20, pagingData.size)
//        assertEquals(1, pagingData.getPageNumber())
//        assertEquals(325, pagingData.getTotalCount())
//        // Add additional verifications for the PagingData object as needed
//
//        pagingDataLiveData.removeObserver(testObserver)
//    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun testBeerListRepositoryImpl() {
        val apiService = mock(ApiService::class.java)
        val beerDAO = mock(BeerDAO::class.java)
        val remoteKeyDAO = mock(RemoteKeyDAO::class.java)
        val beerListRepositoryImpl = BeerListRepositoryImpl(apiService, beerDAO, remoteKeyDAO)
        assertNotNull(beerListRepositoryImpl.getBeerList())
    }

    override fun tearDown() {
        super.tearDown()
//        mockWebServer.shutdown()
    }

}