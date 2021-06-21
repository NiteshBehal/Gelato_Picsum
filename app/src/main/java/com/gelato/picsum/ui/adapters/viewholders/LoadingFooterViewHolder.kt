package com.gelato.picsum.ui.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.gelato.picsum.databinding.ItemImageListFooterBinding

class LoadingFooterViewHolder(
    val binding: ItemImageListFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.btnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.tvErrorMsg.isVisible = loadState is LoadState.Error
        binding.pbLoading.isVisible = loadState is LoadState.Loading
        binding.btnRetry.isVisible = loadState !is LoadState.Loading

    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadingFooterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemImageListFooterBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return LoadingFooterViewHolder(binding, retry)
        }
    }
}