package com.gelato.picsum.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.gelato.picsum.R
import java.io.File

fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities?.let {
            when {
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
                else -> return false
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun shareImage(imageUrl: String?, context: Context) {
    if (!isNetworkAvailable(context)) {
        context.showToast(context.getString(R.string.network_error))
        return
    }
    imageUrl?.let {
        Glide.with(context)
            .asBitmap()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    shareImageIntent(resource, context)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    } ?: run {
        context.showToast(context.getString(R.string.download_error))
    }
}

private fun shareImageIntent(bitmap: Bitmap, context: Context) {
    val bitmapPath: String = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap,
        context.getString(R.string.app_name),
        context.getString(R.string.share_description)
    )
    val bitmapUri = Uri.parse(bitmapPath)
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/png"
    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.share)
        )
    )
}

fun downloadImage(imageUrl: String?, context: Context) {
    if (!isNetworkAvailable(context)) {
        context.showToast(context.getString(R.string.network_error))
        return
    }
    imageUrl?.let {
        try {
            val filename = System.currentTimeMillis().toString()
            val downloadManager =
                context
                    .getSystemService(Context.DOWNLOAD_SERVICE)
                        as DownloadManager
            val downloadUri = Uri.parse(it)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        or
                        DownloadManager.Request.NETWORK_MOBILE
            )
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + filename + ".jpg"
                )

            downloadManager.enqueue(request)
            Toast.makeText(
                context,
                context.getString(R.string.download_started),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.download_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}