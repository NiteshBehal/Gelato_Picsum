package com.gelato.picsum.ui.adapters

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.ui.adapters.viewholders.ImageViewHolder

class ImageListAdapter(val imageClick: (Int) -> Unit) :
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
            set.clone(holder.binding.parentContsraint)
            set.setDimensionRatio(holder.binding.ivPhoto.id, ratio)
            set.applyTo(holder.binding.parentContsraint)
            setItemClick(holder, position)
            holder.bind(it)
        }
    }

    private fun setItemClick(
        holder: ImageViewHolder,
        position: Int
    ) {
        holder.itemView.setOnClickListener {
            imageClick.invoke(position)
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