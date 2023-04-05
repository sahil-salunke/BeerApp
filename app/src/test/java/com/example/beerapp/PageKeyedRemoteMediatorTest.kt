package com.example.beerapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import com.example.beerapp.common.MainCoroutineRule
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.RemoteKeyDAO
import com.example.beerapp.domain.paging.BeerRemoteMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.assertTrue
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalPagingApi
class PageKeyedRemoteMediatorTest {

    @get:Rule
    private val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var beerDAO: BeerDAO

    @Mock
    private lateinit var remoteKeyDAO: RemoteKeyDAO

    private val beerList = listOf(BeerDTO(1), BeerDTO(2), BeerDTO(3))

    private lateinit var remoteMediator: BeerRemoteMediator

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        remoteMediator = BeerRemoteMediator(remoteKeyDAO, beerDAO, apiService)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {

        Mockito.`when`(apiService.getAllBears(anyInt(), anyInt()))
            .thenReturn(Response.success(beerList))

        val pagingState = PagingState(
            listOf(PagingSource.LoadResult.Page(beerList, anyInt(), anyInt())),
            null,
            PagingConfig(15),
            15
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        // To test endOfPaginationReached, don't set up the mockApi to return post
        // data here.
        val pagingState = PagingState(
            listOf(PagingSource.LoadResult.Page(beerList, 1, 2)),
            null,
            PagingConfig(15),
            15
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
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
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

}