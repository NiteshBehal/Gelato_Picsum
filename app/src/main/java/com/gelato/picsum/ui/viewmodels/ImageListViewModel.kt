package com.gelato.picsum.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.data.repository.PicsumRepo
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class ImageListViewModel(
    repo: PicsumRepo
) : ViewModel() {

    private val imageResult: Flow<PagingData<ImageData>> =
        repo.fetchImageList().cachedIn(viewModelScope)

    fun fetchImageList() = imageResult

}