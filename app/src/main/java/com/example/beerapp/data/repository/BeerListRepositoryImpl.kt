package com.example.beerapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.paging.BeerRemoteMediator
import com.example.beerapp.room.BeerDAO
import com.example.beerapp.room.RemoteKeyDAO
import kotlinx.coroutines.flow.Flow

class BeerListRepositoryImpl(
    private val apiService: ApiService,
    private val beerDAO: BeerDAO,
    private val remoteKeyDAO: RemoteKeyDAO
) : BeerListRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getBeerList(pageNo: Int, perPage: Int): Flow<PagingData<BeerDTO>?> {
        return Pager(
            config = PagingConfig(
                pageSize = perPage,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = perPage
            ),
            remoteMediator = BeerRemoteMediator(
                apiService = apiService,
                beerDAO = beerDAO,
                remoteKeyDAO = remoteKeyDAO
            ),
            pagingSourceFactory = {
                beerDAO.getAllBeers()
                // BeerPagingSource(apiService)
            }
        ).flow
    }
}