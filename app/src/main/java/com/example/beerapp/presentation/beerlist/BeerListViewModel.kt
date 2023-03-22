package com.example.beerapp.presentation.beerlist

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.domain.repository.BeerListRepository
import com.example.beerapp.domain.usecase.GetBeerListUseCase
import com.example.beerapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(
    private val beerListUseCase: GetBeerListUseCase,
    beerListRepository: BeerListRepository
) :
    ViewModel() {

    //    private val _beers = MutableStateFlow(BeerListState())
//    val beerList: StateFlow<BeerListState> = _beers
    val selectedBeer = MutableLiveData<BeerDTO>()
    val isLoading = ObservableBoolean(false)
    val isError = ObservableBoolean(false)
    val isNoData = ObservableBoolean(false)
    val errorMessage = MutableLiveData<String>()
    lateinit var result: LiveData<PagingData<BeerDTO>>

    fun getBeerList() {
        beerListUseCase().onEach {
            when (it) {
                is Resource.Loading -> {
                    isLoading.set(true)
                    isError.set(false)
                }
                is Resource.Success -> {
                    isLoading.set(false)
                    isError.set(false)
                    result = it.data!!.cachedIn(viewModelScope)
                }
                is Resource.Error -> {
                    isLoading.set(false)
                    isError.set(true)
                    errorMessage.value = it.message ?: ""
                }
            }
        }.launchIn(viewModelScope)
    }

}