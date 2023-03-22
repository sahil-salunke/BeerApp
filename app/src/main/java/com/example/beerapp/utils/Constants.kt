package com.example.beerapp.utils

import android.view.View

object Constants {

    // Base url for network calls
    const val BASE_URL = "https://api.punkapi.com/v2/"

    const val PER_PAGE = 15

    // Extension function to make view visible
    fun View.showView() {
        this.visibility = View.VISIBLE
    }

    // Extension function to make view gone
    fun View.hideView() {
        this.visibility = View.GONE
    }

}