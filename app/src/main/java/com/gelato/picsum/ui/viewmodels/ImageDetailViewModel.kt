package com.gelato.picsum.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gelato.picsum.data.repository.PicsumRepo
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ImageDetailViewModel @Inject constructor(repo: PicsumRepo) : ViewModel() {
    val imageList = liveData {
        repo.fetchImagesFromDB().collectLatest {
            emit(it)
        }
    }
}