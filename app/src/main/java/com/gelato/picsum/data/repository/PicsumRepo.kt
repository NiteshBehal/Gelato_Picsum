package com.gelato.picsum.data.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gelato.picsum.data.db.ImagesDb
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.data.network.ImageListApi
import com.gelato.picsum.data.paging.ImagesRemoteMediator
import com.gelato.picsum.utils.PAGE_LIMIT
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PicsumRepo @Inject constructor(
    val imageListApi: ImageListApi,
    val db: ImagesDb
) {

    @ExperimentalPagingApi
    fun fetchImageList(): Flow<PagingData<ImageData>> {
        val pagingSourceFactory = { db.imageDao().pagingSource() }
        return Pager(
            config = PagingConfig(pageSize = PAGE_LIMIT, enablePlaceholders = false),
            remoteMediator = ImagesRemoteMediator(db, imageListApi),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun fetchImagesFromDB(): Flow<List<ImageData>> {
        return db.imageDao().getAllImages()
    }

}