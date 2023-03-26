package com.example.beerapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class BeerListViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: BeerListRepository

    @Mock
    private lateinit var liveData: MutableLiveData<PagingData<BeerDTO>>

    private val observer: Observer<PagingData<BeerDTO>> = MockitoUtils().mock()

    override fun setUp() {
        super.setUp()
        MockitoAnnotations.openMocks(this)
    }

//    fun testViewModelToGetEmptyResponse() = kotlinx.coroutines.test.runTest {
//        liveData.postValue(PagingData.from(listOf(BeerDTO(1), BeerDTO(2), BeerDTO(3))))
//        Mockito.`when`(repository.getBeerList()).thenReturn(
//            MutableLiveData(
//                PagingData.from(
//                    listOf(
//                        BeerDTO(1),
//                        BeerDTO(2),
//                        BeerDTO(3)
//                    )
//                )
//            )
//        )
//        val viewModel = BeerListViewModel(repository)
//        repository.getBeerList().cachedIn(viewModel)
//        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
//        val beer = viewModel.result.getOrAwaitValue()
//        verify(observer).onChanged(beer)
//        assertEquals(
//            MutableLiveData(PagingData.from(listOf(BeerDTO(1), BeerDTO(2), BeerDTO(3)))),
//            viewModel.result.value
//        )
//    }


    @Test
    fun test_GetProducts() = kotlinx.coroutines.test.runTest {
//        liveData.postValue(PagingData.from(listOf(BeerDTO(1), BeerDTO(2), BeerDTO(3))))
//        Mockito.`when`(repository.getBeerList()).thenReturn(liveData)
//
//        val sut = BeerListViewModel(repository)
//        sut.result.observeForever(observer)
//        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
//        val result = sut.result.getOrAwaitValue()
//        Assert.assertEquals(0, result)
    }

}