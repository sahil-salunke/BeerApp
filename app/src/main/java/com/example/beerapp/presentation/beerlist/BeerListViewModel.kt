package com.example.beerapp.presentation.beerlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.usecase.GetBeerListUseCase
import com.example.beerapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(private val beerListUseCase: GetBeerListUseCase) :
    ViewModel() {

    private val _beers = MutableStateFlow(BeerListState())
    val beerList: StateFlow<BeerListState> = _beers
    val selectedBeer = MutableLiveData<BeerDTO>()

    fun getBeerList(pageNo: Int, perPage: Int) {
        beerListUseCase(pageNo, perPage).onEach {
            when (it) {
                is Resource.Loading -> {
                    _beers.value = BeerListState(isLoading = true)
                }
                is Resource.Success -> {
                    _beers.value = BeerListState(data = it.data)
                }
                is Resource.Error -> {
                    _beers.value = BeerListState(error = it.message ?: "")
                }
            }
        }.launchIn(viewModelScope)
    }

}