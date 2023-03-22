package com.example.beerapp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.data.model.RemoteKey
import com.example.beerapp.data.remote.ApiService
import com.example.beerapp.room.BeerDAO
import com.example.beerapp.room.RemoteKeyDAO

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val remoteKeyDAO: RemoteKeyDAO,
    private val beerDAO: BeerDAO,
    private val apiService: ApiService
) : RemoteMediator<Int, BeerDTO>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerDTO>
    ): MediatorResult {

        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getClosestPosition(state)
                remoteKey?.next?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.APPEND -> {
                val remoteKey = getLastPosition(state)
                val nextKey = remoteKey?.next ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                nextKey
            }
        }

        val response = apiService.getAllBears(loadKey, state.config.pageSize)

        val endOfPagination = response?.size!! < state.config.pageSize

        if (loadType == LoadType.REFRESH) {
            beerDAO.deleteBeers()
            remoteKeyDAO.clearAll()
        }

        val prev = if (loadKey == 1) null else loadKey - 1
        val next = if (endOfPagination) null else loadKey + 1

        response.map {
            remoteKeyDAO.insert(
                RemoteKey(
                    next = next,
                    prev = prev,
                    id = it.id
                )
            )
        }
        beerDAO.insert(response)
        return MediatorResult.Success(endOfPagination)
    }

    private suspend fun getClosestPosition(state: PagingState<Int, BeerDTO>): RemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let { it ->
                remoteKeyDAO.getAllRemoteKeys(it.id)
            }
        }
    }

    private suspend fun getLastPosition(state: PagingState<Int, BeerDTO>): RemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteKeyDAO.getAllRemoteKeys(it.id)
        }
    }

}