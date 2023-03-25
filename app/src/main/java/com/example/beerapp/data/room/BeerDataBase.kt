package com.example.beerapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.model.RemoteKey

@Database(entities = [BeerDTO::class, RemoteKey::class], version = 1, exportSchema = false)

@TypeConverters(RequestConverters::class)
abstract class BeerDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): BeerDatabase {
            return Room.databaseBuilder(context, BeerDatabase::class.java, "beer_db").build()
        }
    }

    abstract fun getBeerDAO(): BeerDAO

    abstract fun getRemoteDAO(): RemoteKeyDAO

}