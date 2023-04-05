package com.example.beerapp

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.BeerDatabase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class BeerDaoTest {

    private lateinit var beerDatabase: BeerDatabase

    private lateinit var beerDAO: BeerDAO

    private val dummy = listOf(
        BeerDTO(
            1, "Buzz1", image_url = "https://images.punkapi.com/v2/keg.png",
            abv = 4.1
        ), BeerDTO(
            2, "Buzz2", image_url = "https://images.punkapi.com/v2/keg.png",
            abv = 4.2
        )
    )

    @Before
    fun setUp() {
        beerDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), BeerDatabase::class.java
        ).allowMainThreadQueries().build()
        beerDAO = beerDatabase.getBeerDAO()
    }

    @Test
    fun testInsertDataInDB() = runBlocking {

        beerDAO.insert(dummy)

        val pagingSource = beerDAO.getAllBeers()

        val page = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2, placeholdersEnabled = false
            )
        )

        val result = (page as? PagingSource.LoadResult.Page)?.data

        assertTrue(result?.isEmpty()!!.not())
        assertEquals(result, dummy)
    }

    @Test
    fun testDeleteDataFromDB() = runBlocking {

        beerDAO.insert(dummy)

        beerDAO.deleteBeers()

        val pagingSource = beerDAO.getAllBeers()

        val page = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2, placeholdersEnabled = false
            )
        )

        val result = (page as? PagingSource.LoadResult.Page)?.data

        assertTrue(result!!.isEmpty())
    }


    @After
    fun tearDown() {
        beerDatabase.clearAllTables()
        beerDatabase.close()
    }

}