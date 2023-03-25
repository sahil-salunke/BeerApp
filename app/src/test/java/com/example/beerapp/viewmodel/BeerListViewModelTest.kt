package com.example.beerapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.beerapp.MockitoUtils
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.presentation.beerlist.BeerListViewModel
import com.example.beerapp.testutil.MainCoroutineRule
import com.example.beerapp.testutil.getOrAwaitValue
import com.example.beerapp.utils.Resource
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi

class BeerListViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val testDispatcher = StandardTestDispatcher()

//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: BeerListRepository

    @Mock
    private lateinit var liveData: MutableLiveData<PagingData<BeerDTO>>

    private val observer: Observer<PagingData<BeerDTO>> = MockitoUtils().mock()

    override fun setUp() {
        super.setUp()
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    fun testViewModelToGetEmptyResponse() = kotlinx.coroutines.test.runTest {
//        liveData.postValue(PagingData.from(listOf(BeerDTO(1), BeerDTO(2), BeerDTO(3))))
//        whenever(repository.getBeerList()).thenReturn(liveData)
        Mockito.`when`(repository.getBeerList()).thenReturn(
            MutableLiveData(
                PagingData.from(
                    listOf(
                        BeerDTO(1),
                        BeerDTO(2),
                        BeerDTO(3)
                    )
                )
            )
        )
        val viewModel = BeerListViewModel(repository)
        repository.getBeerList().cachedIn(viewModel)
        testDispatcher.scheduler.advanceUntilIdle()
//        val beer = viewModel.result.getOrAwaitValue()
//        verify(observer).onChanged(viewModel.result.value)
        assertEquals(
            MutableLiveData(PagingData.from(listOf(BeerDTO(1), BeerDTO(2), BeerDTO(3)))),
            viewModel.result.value
        );
    }

    override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
    }

}