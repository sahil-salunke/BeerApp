package com.example.beerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import com.example.beerapp.common.MainCoroutineRule
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.data.repository.BeerListRepositoryImpl
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.RemoteKeyDAO
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalPagingApi
class BeerListRepositoryImplTest {

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var beerDAO: BeerDAO

    @Mock
    private lateinit var remoteKeyDAO: RemoteKeyDAO

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repositoryImpl: BeerListRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repositoryImpl = BeerListRepositoryImpl(apiService, beerDAO, remoteKeyDAO)
    }

    @Test
    fun testBeerListRepositoryResponseData() = runTest {
        val result = repositoryImpl.getBeerList()
        assertNotNull(result)
    }

}