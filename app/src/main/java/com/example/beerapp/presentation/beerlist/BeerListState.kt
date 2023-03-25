package com.example.beerapp.presentation.beerlist

import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO
import kotlinx.coroutines.flow.Flow

data class BeerListState(
    val isLoading: Boolean = false,
    val data: PagingData<BeerDTO>,
    val selected: BeerDTO? = null,
    val error: String = ""
)