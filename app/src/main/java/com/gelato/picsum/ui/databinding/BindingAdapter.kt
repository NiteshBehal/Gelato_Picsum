package com.gelato.picsum.ui.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gelato.picsum.R
import com.gelato.picsum.data.models.ImageData

@BindingAdapter("app:loadImage")
fun ImageView.loadImage(image: ImageData) {
    this.post {
        val myOptions = RequestOptions()
            .override(this.width, this.height)
            .fitCenter()
        Glide
            .with(context)
            .load(image.download_url)
            .placeholder(R.drawable.default_img)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(myOptions)
            .into(this)
    }
}

