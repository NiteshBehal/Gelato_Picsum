package com.gelato.picsum.ui.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.ui.adapters.viewholders.PagerItemViewholder

class ImagePagerAdapter(
    val downloadImage: (String?) -> Unit,
    val shareImage: (String?) -> Unit
) :
    PagingDataAdapter<ImageData, PagerItemViewholder>(DATA_COMPARTOR) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagerItemViewholder {
        return PagerItemViewholder.create(parent)
    }

    override fun onBindViewHolder(holder: PagerItemViewholder, position: Int) {
        getItem(position)?.let { data ->
            holder.bind(data)
            holder.binding.btnDownload.setOnClickListener {
                downloadImage.invoke(data.download_url)
            }
            holder.binding.btnShare.setOnClickListener {
                shareImage.invoke(data.download_url)
            }
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