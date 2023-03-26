package com.example.beerapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.data.repository.BeerListRepositoryImpl
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.RemoteKeyDAO
import com.example.beerapp.testutil.MainCoroutineRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class BeerListRepositoryImplTest {

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var beerDAO: BeerDAO

    @Mock
    private lateinit var remoteKeyDAO: RemoteKeyDAO

    @Mock
    private lateinit var repositoryImplTest: BeerListRepositoryImplTest

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }


    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun testBeerListRepositoryForSuccess() = runTest {

        val mockRepo = Mockito.mock(BeerListRepositoryImpl::class.java)

        val dummyData = flowOf(
            PagingData.from(
                listOf(
                    BeerDTO(1),
                    BeerDTO(2),
                    BeerDTO(3)
                )
            )
        )

        Mockito.`when`(mockRepo.getBeerList()).thenReturn(dummyData)

        mockRepo.getBeerList().test {
            val list = listOf(awaitItem().map { it })
            assertEquals(1, list.size)
            awaitComplete()
        }

    }


    @After
    fun tearDown() {
    }
}