package com.example.beerapp.presentation.beerlist

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(beerListRepository: BeerListRepository) : ViewModel() {

    val selectedBeer = MutableLiveData<BeerDTO>()
    val isLoading = ObservableBoolean(false)
    val isError = ObservableBoolean(false)
    val isDataAvailable = ObservableBoolean(false)
    val errorMessage = MutableLiveData<String>()
    val reloadClick = SingleLiveEvent<Void>()
    val result: Flow<PagingData<BeerDTO>>
        get() = _beerList

    // Ask for Beer data
    private val _beerList = beerListRepository.getBeerList().cachedIn(viewModelScope)

    fun reloadBeers() {
        reloadClick.call()
    }

}