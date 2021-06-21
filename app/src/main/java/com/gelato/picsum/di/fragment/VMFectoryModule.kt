package com.gelato.picsum.di.fragment

import androidx.lifecycle.ViewModelProvider
import com.gelato.myapplication.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class VMFectoryModule {
    @FragmentScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory

}