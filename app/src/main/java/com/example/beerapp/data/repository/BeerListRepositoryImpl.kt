package com.example.beerapp.data.repository

import androidx.paging.*
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.domain.paging.BeerRemoteMediator
import com.example.beerapp.data.room.BeerDAO
import com.example.beerapp.data.room.RemoteKeyDAO
import com.example.beerapp.utils.Constants.PER_PAGE
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class BeerListRepositoryImpl(
    private val apiService: ApiService,
    private val beerDAO: BeerDAO,
    private val remoteKeyDAO: RemoteKeyDAO
) : BeerListRepository {
    override fun getBeerList(): Flow<PagingData<BeerDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = PER_PAGE,
                initialLoadSize = PER_PAGE
            ),
            remoteMediator = BeerRemoteMediator(remoteKeyDAO, beerDAO, apiService),
            pagingSourceFactory = { beerDAO.getAllBeers() }
        ).flow
    }

}
