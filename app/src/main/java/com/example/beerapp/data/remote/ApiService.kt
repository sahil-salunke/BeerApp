package com.example.beerapp.data.remote

import com.example.beerapp.data.model.BeerDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("beers?")
    suspend fun getAllBears(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): List<BeerDTO>?

}