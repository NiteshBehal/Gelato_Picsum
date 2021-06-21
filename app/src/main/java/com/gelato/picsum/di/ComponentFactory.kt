package com.gelato.picsum.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.gelato.picsum.GelatoPicsumApplication
import com.gelato.picsum.di.application.ApplicationComponent
import com.gelato.picsum.di.application.DaggerApplicationComponent
import com.gelato.picsum.di.fragment.DaggerFragmentComponent
import com.gelato.picsum.di.fragment.FragmentComponent

object ComponentFactory {

    fun createApplicationComponent(context: Context): ApplicationComponent {
        return DaggerApplicationComponent.builder().application(context).build()
    }

    @ExperimentalPagingApi
    fun createFragmentComponent(): FragmentComponent {
        return DaggerFragmentComponent.builder()
            .applicationComponent(GelatoPicsumApplication.applicationComponent)
            .build()
    }
}