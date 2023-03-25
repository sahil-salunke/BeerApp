package com.example.beerapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.paging.LimitOffsetDataSource
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.BeerDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class BeerDaoTest : TestCase() {

    @get:Rule
    private val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    private val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    private lateinit var beerDatabase: BeerDatabase

    private lateinit var beerDAO: BeerDAO

    override fun setUp() {
        super.setUp()
        hiltAndroidRule.inject()
        beerDAO = beerDatabase.getBeerDAO()
    }

    @Test
    fun insertDataInDB() = runTest {
        val beer = BeerDTO(
            1, "Buzz", image_url = "https://images.punkapi.com/v2/keg.png",
            abv = 4.5
        )
        beerDAO.insert(listOf(beer))

        val result = (beerDAO.getAllBeers() as LimitOffsetDataSource<BeerDTO>).loadRange(0, 1)
        assertFalse(result.isEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun deleteDataInDB() = runTest {
        val beer = BeerDTO(
            1, "Buzz", image_url = "https://images.punkapi.com/v2/keg.png",
            abv = 4.5
        )
        beerDAO.insert(listOf(beer))
        beerDAO.deleteBeers()

        val result = (beerDAO.getAllBeers() as LimitOffsetDataSource<BeerDTO>).loadRange(0, 1)
        assertTrue(result.isEmpty())
    }

    override fun tearDown() {
        super.tearDown()
        beerDatabase.clearAllTables()
        beerDatabase.close()
    }

}