package com.example.beerapp.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.beerapp.R
import com.example.beerapp.data.model.BeerDTO

@BindingAdapter("urlToImage")
fun urlToImage(view: ImageView, s: String?) {
    val options = RequestOptions.placeholderOf(R.drawable.ic_pitcher).error(R.drawable.ic_pitcher)
    Glide.with(view).setDefaultRequestOptions(options).load(s ?: "").into(view)
}

@BindingAdapter("app:details")
fun setValues(textView: TextView, beer: BeerDTO) {
    val maltIngredients = beer.ingredients?.malt?.joinToString {
        it.name.toString()
    }

    val hops = beer.ingredients?.hops?.joinToString {
        it.name.toString()
    }

    val foodPairings = beer.food_pairing?.joinToString {
        it
    }

    textView.text =
        textView.context.getString(R.string.txt_detail_info, maltIngredients, hops, foodPairings)
}

@BindingAdapter("visibility")
fun visibility(view: View, visibility: Boolean?) {
    visibility?.let {
        view.visibility = if (it) View.VISIBLE else View.GONE
    }
}
