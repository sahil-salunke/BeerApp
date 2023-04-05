package com.example.beerapp.presentation.beerlist

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.usecase.BeerListUseCase
import com.example.beerapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(private val beerListUseCase: BeerListUseCase) :
    ViewModel() {

    val selectedBeer = MutableLiveData<BeerDTO>()

    val reloadClick = SingleLiveEvent<Void>()

    private var _beerList: Flow<PagingData<BeerDTO>> = flowOf(PagingData.empty())
    val resultList: Flow<PagingData<BeerDTO>>
        get() = _beerList

    fun getBeersData() {
        _beerList = beerListUseCase().cachedIn(viewModelScope)
    }

    fun reloadBeers() {
        reloadClick.call()
    }


}