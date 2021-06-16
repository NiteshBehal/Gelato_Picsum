package com.gelato.picsum.ui.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.databinding.ItemImageViewpagerBinding

class PagerItemViewholder(val binding: ItemImageViewpagerBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ImageData) {
        binding.image = data
        ViewCompat.setTransitionName(binding.imageIv, data.download_url);
    }

    companion object {
        fun create(parent: ViewGroup): PagerItemViewholder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemImageViewpagerBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return PagerItemViewholder(binding)
        }
    }
}