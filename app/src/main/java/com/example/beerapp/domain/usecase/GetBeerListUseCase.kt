package com.example.beerapp.domain.usecase

import androidx.paging.PagingData
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBeerListUseCase @Inject constructor(private val repository: BeerListRepository) {

    operator fun invoke(pageNo: Int, perPage: Int): Flow<Resource<PagingData<BeerDTO>?>> = flow {
        try {
            emit(Resource.Loading())
            val data = repository.getBeerList(pageNo, perPage)
            emit(Resource.Success(data = data.first()))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An Unknown error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Check Network Connectivity"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Something went wrong"))
        }
    }

}