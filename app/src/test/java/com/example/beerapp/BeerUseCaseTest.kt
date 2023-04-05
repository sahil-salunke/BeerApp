package com.example.beerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.beerapp.common.MainCoroutineRule
import com.example.beerapp.common.TestDiffCallback
import com.example.beerapp.common.TestListCallback
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.domain.usecase.BeerListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class BeerUseCaseTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: BeerListRepository

    private lateinit var useCase: BeerListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = BeerListUseCase(repository)
    }

    @Test
    fun testGetBeersData_forSuccess_with_flowOfData() = runTest {

        val dummyData = flowOf(
            PagingData.from(
                listOf(
                    BeerDTO(1), BeerDTO(2), BeerDTO(3)
                )
            )
        )

        Mockito.`when`(repository.getBeerList()).thenReturn(dummyData)

        val result = useCase.invoke().first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback<BeerDTO>(),
            updateCallback = TestListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        val job = launch {
            differ.submitData(result)
        }

        advanceUntilIdle()
        val pagingList = differ.snapshot().items //this is the data you wanted
        assertTrue(pagingList.isEmpty().not())
        assertEquals(3, pagingList.size)
        job.cancel()
    }

    @Test
    fun testGetBeersData_forSuccess_withEmpty_flowOfData() = runTest {

        val dummyData = flowOf(PagingData.from(listOf<BeerDTO>()))

        Mockito.`when`(useCase()).thenReturn(dummyData)

        Mockito.`when`(repository.getBeerList()).thenReturn(dummyData)

        val result = useCase.invoke().first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback<BeerDTO>(),
            updateCallback = TestListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        val job = launch {
            differ.submitData(result)
        }

        advanceUntilIdle()
        val pagingList = differ.snapshot().items //this is the data you wanted
        assertTrue(pagingList.isEmpty())
        assertEquals(0, pagingList.size)
        job.cancel()
    }

}