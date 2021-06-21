package com.gelato.picsum.data.network

import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.utils.PAGE_LIMIT
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageListApi {
    @GET("v2/list")
    suspend fun getImageList(
        @Query("page") page: Int,
        @Query("limit") limit: Int = PAGE_LIMIT
    ): List<ImageData>
}