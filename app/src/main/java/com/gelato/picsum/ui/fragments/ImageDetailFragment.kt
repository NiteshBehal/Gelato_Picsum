package com.gelato.picsum.ui.fragments

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.gelato.picsum.R
import com.gelato.picsum.data.db.ImagesDb
import com.gelato.picsum.data.network.ImageListApi
import com.gelato.picsum.data.network.ServiceBuilder
import com.gelato.picsum.data.repository.PicsumRepo
import com.gelato.picsum.databinding.FragmentImageDetailBinding
import com.gelato.picsum.ui.adapters.ImagePagerAdapter
import com.gelato.picsum.ui.viewmodels.ImageListViewModel
import com.gelato.picsum.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File


@ExperimentalPagingApi
class ImageDetailFragment : Fragment() {

    private lateinit var imageListViewModel: ImageListViewModel
    private val layoutId: Int = R.layout.fragment_image_detail
    protected lateinit var binding: FragmentImageDetailBinding
    private var selectedImagePos: Int = 0

    private val adapter by lazy {
        ImagePagerAdapter(
            downloadImage(),
            shareImage()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedImagePos = it.getInt("SelectedImagePos", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setUI()
    }

    private fun setUI() {
        binding.pdfViewPager.adapter = adapter
        lifecycleScope.launch {
            imageListViewModel.fetchImageList().collectLatest {
                adapter.submitData(it)
            }
        }

        binding.pdfViewPager.post {
            binding.pdfViewPager.setCurrentItem(
                selectedImagePos,
                false
            )
        }
    }


    private fun setupViewModel() {
        val api = ServiceBuilder.buildService(ImageListApi::class.java)
        val db = ImagesDb(requireContext())
        val vmFactory = ViewModelFactory(PicsumRepo(api, db))
        imageListViewModel = ViewModelProvider(
            requireActivity(),
            vmFactory
        ).get(ImageListViewModel::class.java)
    }

    private fun downloadImage() =
        { imageUrl: String? ->
            if (imageUrl != null) {
                downloadImageNew(
                    System.currentTimeMillis().toString(),
                    imageUrl
                )
            }
        }

    private fun shareImage() = { imageUrl: String? ->
        if(imageUrl != null) {
            Glide.with(requireContext())
                .asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        shareImageIntent(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
    }

    private fun shareImageIntent(bitmap: Bitmap) {
        val bitmapPath: String = Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            "palette",
            "share palette"
        )
        val bitmapUri = Uri.parse(bitmapPath)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun downloadImageNew(
        filename: String,
        downloadUrlOfImage: String?
    ) {
        try {
            val downloadManager =
                requireActivity()
                    .getSystemService(Context.DOWNLOAD_SERVICE)
                        as DownloadManager
            val downloadUri = Uri.parse(downloadUrlOfImage)
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
                requireContext(),
                "Image download started.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Image download failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

