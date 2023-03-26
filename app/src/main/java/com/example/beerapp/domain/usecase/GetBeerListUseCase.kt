package com.example.beerapp.domain.usecase

import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBeerListUseCase @Inject constructor(private val repository: BeerListRepository) {

    suspend operator fun invoke(): Flow<PagingData<BeerDTO>> = repository.getBeerList()

}