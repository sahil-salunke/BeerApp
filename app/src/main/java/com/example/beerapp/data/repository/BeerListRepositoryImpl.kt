package com.example.beerapp.data.repository

import androidx.paging.*
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.paging.BeerRemoteMediator
import com.example.beerapp.room.BeerDAO
import com.example.beerapp.room.RemoteKeyDAO
import com.example.beerapp.utils.Constants.PER_PAGE

@ExperimentalPagingApi
class BeerListRepositoryImpl(
    private val apiService: ApiService,
    private val beerDAO: BeerDAO,
    private val remoteKeyDAO: RemoteKeyDAO
) : BeerListRepository {

    override fun getBeerList() = Pager(
        config = PagingConfig(
            pageSize = PER_PAGE,
            maxSize = 100
        ),
        remoteMediator = BeerRemoteMediator(remoteKeyDAO, beerDAO, apiService),
        pagingSourceFactory = { beerDAO.getAllBeers() }
    ).liveData

}