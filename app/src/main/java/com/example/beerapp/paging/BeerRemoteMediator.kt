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
import com.example.beerapp.utils.Constants.PER_PAGE
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class BeerRemoteMediator(
    private val remoteKeyDAO: RemoteKeyDAO,
    private val beerDAO: BeerDAO,
    private val apiService: ApiService
) : RemoteMediator<Int, BeerDTO>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerDTO>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.next?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prev
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.next
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = apiService.getAllBears(loadKey, PER_PAGE)
            val endOfPagination = response.size < PER_PAGE

            if (loadType == LoadType.REFRESH) {
                beerDAO.deleteBeers()
                remoteKeyDAO.deleteAllRemoteKeys()
            }

            val prev = if (loadKey == 1) null else loadKey - 1
            val next = if (endOfPagination) null else loadKey + 1

            if (loadType == LoadType.REFRESH) {
                beerDAO.deleteBeers()
                remoteKeyDAO.deleteAllRemoteKeys()
            }

            beerDAO.insert(response)
            val keys = response.map { quote ->
                RemoteKey(
                    id = quote.id,
                    prev = prev,
                    next = next
                )
            }
            remoteKeyDAO.addAllRemoteKeys(keys)
            MediatorResult.Success(endOfPagination)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, BeerDTO>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDAO.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, BeerDTO>
    ): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { beer ->
                remoteKeyDAO.getRemoteKeys(id = beer.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, BeerDTO>
    ): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { quote ->
                remoteKeyDAO.getRemoteKeys(id = quote.id)
            }
    }

}