package com.example.beerapp.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.paging.LimitOffsetDataSource
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.BeerDatabase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BeerDaoTest {

    @get:Rule
    private val instantExecutorRule = InstantTaskExecutorRule()

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var beerDatabase: BeerDatabase

    private lateinit var beerDAO: BeerDAO

    @Mock
    private lateinit var context: Context

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        beerDatabase = BeerDatabase.getInstance(context)
        beerDAO = beerDatabase.getBeerDAO()
    }

    @Test
    fun testInsertDataInDB() = runBlocking {
        val beer = BeerDTO(
            1, "Buzz", image_url = "https://images.punkapi.com/v2/keg.png",
            abv = 4.5
        )
        beerDAO.insert(listOf(beer))


        val result = (beerDAO.getAllBeers() as LimitOffsetDataSource<BeerDTO>).loadRange(0, 1)
        assertFalse(result.isEmpty())
        assertEquals(1, result.toList().size)
    }

    @Test
    fun testDeleteDataFromDB() = runTest {
        val beer = BeerDTO(
            1, "Buzz", image_url = "https://images.punkapi.com/v2/keg.png",
            abv = 4.5
        )
        beerDAO.insert(listOf(beer))
        beerDAO.deleteBeers()

        val result = (beerDAO.getAllBeers() as LimitOffsetDataSource<BeerDTO>).loadRange(0, 1)
        assertTrue(result.isEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        beerDatabase.clearAllTables()
        beerDatabase.close()
    }

}