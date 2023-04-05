package com.example.beerapp


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.beerapp.common.MainCoroutineRule
import com.example.beerapp.common.TestDiffCallback
import com.example.beerapp.common.TestListCallback
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.usecase.BeerListUseCase
import com.example.beerapp.presentation.beerlist.BeerListViewModel
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class BeerListViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: BeerListUseCase

    private lateinit var viewModel: BeerListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BeerListViewModel(useCase)

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

        Mockito.`when`(useCase()).thenReturn(dummyData)

        viewModel.getBeersData()

        val result = viewModel.resultList.first()

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

        viewModel.getBeersData()

        val result = viewModel.resultList.first()

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

