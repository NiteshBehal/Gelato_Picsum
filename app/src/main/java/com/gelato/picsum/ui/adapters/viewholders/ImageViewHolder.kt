package com.gelato.picsum.ui.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.databinding.ItemImageListBinding

/**
 * Image Recyclerview Adapter ViewHolder
 */
class ImageViewHolder(val binding: ItemImageListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ImageData) {
        binding.image = data
    }

    companion object {
        fun create(parent: ViewGroup): ImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemImageListBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return ImageViewHolder(binding)
        }
    }
}