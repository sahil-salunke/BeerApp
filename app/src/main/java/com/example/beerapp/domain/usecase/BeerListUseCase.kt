package com.example.beerapp.domain.usecase

import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.repository.BeerListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BeerListUseCase @Inject constructor(private val repository: BeerListRepository) {

    operator fun invoke(): Flow<PagingData<BeerDTO>> = repository.getBeerList()

}