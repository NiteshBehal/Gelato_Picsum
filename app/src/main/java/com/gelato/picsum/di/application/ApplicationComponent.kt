package com.gelato.picsum.di.application

import android.content.Context
import com.gelato.picsum.data.db.ImagesDb
import com.gelato.picsum.data.network.ImageListApi
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

/**
 * ApplicationComponent class to inject Application level dependencies
 */
@ApplicationScope
@Component(
    modules = [ApplicationModule::class,
        ApiModule::class,
        DBModule::class]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder

        fun build(): ApplicationComponent
    }

    fun getImageListApi(): ImageListApi

    fun getImagesDb(): ImagesDb

    fun getRetrofit(): Retrofit
}