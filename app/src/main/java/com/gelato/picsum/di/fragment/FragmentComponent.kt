package com.gelato.picsum.di.fragment

import androidx.paging.ExperimentalPagingApi
import com.gelato.picsum.di.application.ApplicationComponent
import com.gelato.picsum.ui.fragments.ImageDetailFragment
import com.gelato.picsum.ui.fragments.ImageListFragment
import dagger.Component

@ExperimentalPagingApi
@FragmentScope
@Component(
    modules = [VMFectoryModule::class,
        ImageListVMModule::class,
        ImageDetailVMModule::class],
    dependencies = [ApplicationComponent::class]
)
interface FragmentComponent {

    fun inject(fragment: ImageListFragment)
    fun inject(fragment: ImageDetailFragment)

}