package com.example.beerapp.data.model

import androidx.lifecycle.LiveData
import androidx.paging.PagingData

data class BeerListResults(
    val data: LiveData<PagingData<BeerDTO>>,
    val networkErrors: LiveData<String>
)