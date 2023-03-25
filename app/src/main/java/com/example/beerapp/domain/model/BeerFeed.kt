package com.example.beerapp.domain.model

import com.example.beerapp.data.model.Ingredients

data class BeerFeed(
    val id: Long?,
    val name: String?,
    val imageUrl: String?,
    val abv: Double?,
    val first_brewed: String?,
    val description: String?,
    val ingredients: Ingredients?,
    val food_pairing: List<String>?
)