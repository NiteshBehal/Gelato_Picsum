package com.gelato.picsum.data.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gelato.picsum.data.db.ImagesDb
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.data.network.ImageListApi
import kotlinx.coroutines.flow.Flow

class PicsumRepo(private val imageListApi: ImageListApi, private val db: ImagesDb) {

    @ExperimentalPagingApi
    fun fetchImageList(): Flow<PagingData<ImageData>> {
        val pagingSourceFactory = {db.imageDao().pagingSource()}
        return Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            remoteMediator = ImagesRemoteMediator(db, imageListApi),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}