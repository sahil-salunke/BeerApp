package com.example.beerapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.BeerDatabase
import com.example.beerapp.data.room.RemoteKeyDAO
import com.example.beerapp.domain.paging.BeerRemoteMediator
import com.example.beerapp.testutil.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class PageKeyedRemoteMediatorTest {

    @get:Rule
    private val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var beerDatabase: BeerDatabase

    private lateinit var apiService: ApiService

    private lateinit var mockWebServer: MockWebServer

    @Mock
    private lateinit var beerDAO: BeerDAO

    @Mock
    private lateinit var remoteKeyDAO: RemoteKeyDAO

    @Mock
    private lateinit var context: Context

    private val beerList = listOf(BeerDTO(1), BeerDTO(2), BeerDTO(3))

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        beerDatabase = BeerDatabase.getInstance(context = context)
        beerDAO = beerDatabase.getBeerDAO()
        apiService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {

        Mockito.`when`(apiService.getAllBears(anyInt(), anyInt()))
            .thenReturn(Response.success(beerList))

        val remoteMediator = BeerRemoteMediator(remoteKeyDAO, beerDAO, apiService)

        val pagingState = PagingState(
            listOf(PagingSource.LoadResult.Page(beerList, 1, 2)),
            null,
            PagingConfig(15),
            15
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        // To test endOfPaginationReached, don't set up the mockApi to return post
        // data here.
        val remoteMediator = BeerRemoteMediator(remoteKeyDAO, beerDAO, apiService)

        val pagingState = PagingState(
            listOf(PagingSource.LoadResult.Page(beerList, 1, 2)),
            null,
            PagingConfig(15),
            15
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        // Set up failure message to throw exception from the mock API.
        Mockito.`when`(apiService.getAllBears(anyInt(), anyInt()))
            .thenReturn(null)

        val remoteMediator = BeerRemoteMediator(remoteKeyDAO, beerDAO, apiService)

        val pagingState = PagingState(
            listOf(PagingSource.LoadResult.Page(beerList, 1, 2)),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @After
    fun tearDown() {
        beerDatabase.clearAllTables()
        beerDatabase.close()
    }
}