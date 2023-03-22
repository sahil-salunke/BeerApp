package com.example.beerapp.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beerapp.data.model.BeerDTO

@Dao
interface BeerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<BeerDTO>)

    @Query("DELETE FROM Beers")
    suspend fun deleteBeers()

    @Query("SELECT * FROM Beers")
    fun getAllBeers(): PagingSource<Int, BeerDTO>

}