package com.example.beerapp.di

import android.content.Context
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.data.room.BeerDatabase
import com.example.beerapp.hilt.BeerModule
import com.example.beerapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [BeerModule::class])
@Module
class TestDatabaseModule {

    @Provides
    fun getBeerDatabaseForTest(@ApplicationContext context: Context): BeerDatabase {
        return BeerDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideBeerDataServiceForTest(): ApiService {
        return Retrofit.Builder().baseUrl("/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

}