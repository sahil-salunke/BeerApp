package com.example.beerapp.domain.repository

import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO
import kotlinx.coroutines.flow.Flow

interface BeerListRepository {

    fun getBeerList(): Flow<PagingData<BeerDTO>>

}