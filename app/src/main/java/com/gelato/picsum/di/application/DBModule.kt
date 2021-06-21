package com.gelato.picsum.di.application

import android.content.Context
import com.gelato.picsum.data.db.ImagesDb
import dagger.Module
import dagger.Provides

@Module
class DBModule {
    @Provides
    @ApplicationScope
    fun provideImagesDb(context: Context): ImagesDb {
        return ImagesDb(context)
    }
}