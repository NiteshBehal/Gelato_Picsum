package com.gelato.picsum.ui.adapters

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.ui.adapters.viewholders.ImageViewHolder

/**
 * Image Recyclerview Adapter with LoadMore functionality
 */
class ImageListAdapter(val imageClick: (String) -> Unit) :
    PagingDataAdapter<ImageData, ImageViewHolder>(DATA_COMPARTOR) {
    private val set = ConstraintSet()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        return ImageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let {
            val ratio = String.format("%d:%d", it.width, it.height)
            set.clone(holder.binding.clParent)
            set.setDimensionRatio(holder.binding.ivImage.id, ratio)
            set.applyTo(holder.binding.clParent)
            setItemClick(holder, it)
            holder.bind(it)
        }
    }

    private fun setItemClick(
        holder: ImageViewHolder,
        imageData: ImageData
    ) {
        holder.itemView.setOnClickListener {
            imageClick.invoke(imageData.id)
        }

    }

    companion object {
        private val DATA_COMPARTOR =
            object : DiffUtil.ItemCallback<ImageData>() {
                override fun areItemsTheSame(
                    oldItem: ImageData,
                    newItem: ImageData
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ImageData,
                    newItem: ImageData
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}