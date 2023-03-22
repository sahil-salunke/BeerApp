package com.example.beerapp.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO

interface BeerListRepository {

    fun getBeerList(): LiveData<PagingData<BeerDTO>>

}