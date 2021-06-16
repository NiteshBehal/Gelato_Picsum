package com.gelato.picsum.ui.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.gelato.picsum.ui.adapters.viewholders.LoadingFooterViewHolder

class LoadingFooterAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingFooterViewHolder>() {
    override fun onBindViewHolder(
        holder: LoadingFooterViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingFooterViewHolder {
        return LoadingFooterViewHolder.create(parent, retry)
    }
}