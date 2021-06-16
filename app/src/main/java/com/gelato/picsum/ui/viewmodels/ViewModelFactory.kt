package com.gelato.picsum.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.gelato.picsum.data.repository.PicsumRepo

@ExperimentalPagingApi
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repo: PicsumRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
            return ImageListViewModel(repo,) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}