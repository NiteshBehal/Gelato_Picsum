package com.gelato.picsum.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.ui.adapters.viewholders.PagerItemViewholder

/**
 * Image Detail viewpager adapter
 */
class ImagePagerAdapter: RecyclerView.Adapter<PagerItemViewholder>() {
    private var imageList: List<ImageData>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagerItemViewholder {
        return PagerItemViewholder.create(parent)
    }

    override fun onBindViewHolder(holder: PagerItemViewholder, position: Int) {
        imageList?.get(position)?.let { data ->
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return imageList?.size ?: 0
    }

    fun submitList(images: List<ImageData>) {
        this.imageList = images
        notifyDataSetChanged()
    }
}