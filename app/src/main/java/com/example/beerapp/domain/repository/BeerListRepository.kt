package com.example.beerapp.domain.repository

import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO
import kotlinx.coroutines.flow.Flow

interface BeerListRepository {

    suspend fun getBeerList(pageNo: Int, perPage: Int): Flow<PagingData<BeerDTO>?>

}