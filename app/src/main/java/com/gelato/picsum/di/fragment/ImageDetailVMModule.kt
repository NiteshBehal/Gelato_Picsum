package com.gelato.picsum.di.fragment

import androidx.lifecycle.ViewModel
import com.gelato.picsum.ui.viewmodels.ImageDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ImageDetailVMModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(ImageDetailViewModel::class)
    abstract fun bindsImageDetailViewModel(viewModel: ImageDetailViewModel): ViewModel
}