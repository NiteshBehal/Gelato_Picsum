package com.gelato.picsum.data.network

import com.gelato.picsum.data.models.ImageData
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageListApi {
    @GET("v2/list")
    suspend fun getImageList(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 30
    ): List<ImageData>
}