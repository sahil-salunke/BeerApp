package com.example.beerapp.hilt

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.data.repository.BeerListRepositoryImpl
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.room.BeerDAO
import com.example.beerapp.room.BeerDatabase
import com.example.beerapp.room.RemoteKeyDAO
import com.example.beerapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object BeerModule {

    @Provides
    @Singleton
    fun provideBeerDataService(): ApiService {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun provideBeerListRepository(
        apiService: ApiService,
        beerDAO: BeerDAO,
        remoteKeyDAO: RemoteKeyDAO
    ): BeerListRepository {
        return BeerListRepositoryImpl(apiService, beerDAO, remoteKeyDAO)
    }

    @Provides
    fun getBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
        return BeerDatabase.getInstance(context)
    }

    @Provides
    fun provideBeerDAO(beerDatabase: BeerDatabase): BeerDAO = beerDatabase.getBeerDAO()

    @Provides
    fun provideRemoteKeyDAO(beerDatabase: BeerDatabase): RemoteKeyDAO = beerDatabase.getRemoteDAO()

}