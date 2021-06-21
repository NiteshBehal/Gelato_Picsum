package com.gelato.picsum.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.gelato.picsum.data.db.ImagesDb
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.data.models.RemoteKey
import com.gelato.picsum.data.network.ImageListApi

/**
 * RemoteMediator class to support Paging library with Load more functionality
 */
@ExperimentalPagingApi
class ImagesRemoteMediator(
    private val database: ImagesDb,
    private val networkService: ImageListApi
) : RemoteMediator<Int, ImageData>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageData>
    ): MediatorResult {
        val page = when (val pageKey = getPageKey(state, loadType)) {
            is MediatorResult.Success -> {
                return pageKey
            }
            else -> {
                pageKey as Int
            }
        }

        return try {
            val response = networkService.getImageList(page)
            val isListEmpty = response.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteDao().clearAll()
                    database.imageDao().clearAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isListEmpty) null else page + 1
                val keys = response.map {
                    RemoteKey(it.id, prevKey, nextKey)
                }
                database.imageDao().insertAll(response)
                database.remoteDao().insertOrReplace(keys)
            }
            MediatorResult.Success(endOfPaginationReached = isListEmpty)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPageKey(
        state: PagingState<Int, ImageData>,
        loadType: LoadType
    ): Any {
        when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getCurrentPosition(state)
                val current = remoteKeys?.nextKey?.minus(1)
                return current ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastPosition(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey
                    ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstPosition(state)
                return remoteKeys?.prevKey ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }
        }
    }

    private suspend fun getLastPosition(state: PagingState<Int, ImageData>): RemoteKey? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { imageData ->
            database.remoteDao().remoteKeyByQuery(imageData.id)
        }
    }

    private suspend fun getFirstPosition(state: PagingState<Int, ImageData>): RemoteKey? {
        return state.pages.firstOrNull() {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { imageData ->
            database.remoteDao().remoteKeyByQuery(imageData.id)
        }
    }

    private suspend fun getCurrentPosition(state: PagingState<Int, ImageData>): RemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let { id ->
                database.remoteDao().remoteKeyByQuery(id)
            }
        }
    }
}
