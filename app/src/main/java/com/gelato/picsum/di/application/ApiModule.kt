package com.gelato.picsum.di.application

import com.gelato.picsum.data.network.ImageListApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiModule {
    @Provides
    @ApplicationScope
    fun provideImageListApi(retrofit: Retrofit): ImageListApi {
        return retrofit.create(ImageListApi::class.java)
    }
}