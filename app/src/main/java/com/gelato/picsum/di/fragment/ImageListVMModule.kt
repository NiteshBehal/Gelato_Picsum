package com.gelato.picsum.di.fragment

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.gelato.picsum.ui.viewmodels.ImageListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ImageListVMModule {
    @ExperimentalPagingApi
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(ImageListViewModel::class)
    abstract fun bindsImageListViewModel(viewModel: ImageListViewModel): ViewModel
}