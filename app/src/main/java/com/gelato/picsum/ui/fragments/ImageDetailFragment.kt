package com.gelato.picsum.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.widget.ViewPager2
import client.yalantis.com.foldingtabbar.FoldingTabBar
import com.gelato.myapplication.ViewModelProviderFactory
import com.gelato.picsum.R
import com.gelato.picsum.databinding.FragmentImageDetailBinding
import com.gelato.picsum.di.ComponentFactory
import com.gelato.picsum.ui.adapters.ImagePagerAdapter
import com.gelato.picsum.ui.viewmodels.ImageDetailViewModel
import com.gelato.picsum.utils.SELECTED_IMAGE_ID_KEY
import com.gelato.picsum.utils.downloadImage
import com.gelato.picsum.utils.shareImage
import javax.inject.Inject

/**
 * Fragment to display Image Detail View Pager
 */
@ExperimentalPagingApi
class ImageDetailFragment : Fragment() {

    private val imageDetailViewModel: ImageDetailViewModel by viewModels { providerFactory }
    private val layoutId: Int = R.layout.fragment_image_detail
    private lateinit var binding: FragmentImageDetailBinding
    private var selectedImageId: String = ""

    private val adapter by lazy {
        ImagePagerAdapter()
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentFactory.createFragmentComponent().inject(this)
        arguments?.let {
            selectedImageId = it.getString(SELECTED_IMAGE_ID_KEY, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setupObservers()
    }

    private fun setUI() {
        binding.vpImages.adapter = adapter
        setExpandableMenu()
        handleNavigationButtonsLogic()
    }

    private fun setExpandableMenu() {
        binding.menuExp.onFoldingItemClickListener = object :
            FoldingTabBar.OnFoldingItemSelectedListener {
            override fun onFoldingItemSelected(item: MenuItem): Boolean {
                val imageUrl = imageDetailViewModel.imageList.value?.get(
                    binding.vpImages.currentItem
                )?.download_url
                when (item.itemId) {
                    R.id.menu_share -> {
                        shareImage(imageUrl, requireContext())
                    }
                    R.id.menu_download -> {
                        downloadImage(imageUrl, requireContext())
                    }
                }
                return false
            }
        }
    }

    private fun handleNavigationButtonsLogic() {
        binding.vpImages.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.imgLeftArrow.visibility = View.INVISIBLE
                } else {
                    binding.imgLeftArrow.visibility = View.VISIBLE
                }
                if (position == adapter.itemCount - 1) {
                    binding.imgRightArrow.visibility = View.INVISIBLE
                } else {
                    binding.imgRightArrow.visibility = View.VISIBLE
                }
                binding.toolbar.title =
                    imageDetailViewModel.imageList.value
                        ?.get(position)?.author?.let {
                            "By: $it"
                        } ?: getString(R.string.app_name)
            }
        })

        binding.imgLeftArrow.setOnClickListener {
            binding.vpImages.currentItem =
                binding.vpImages.currentItem - 1
        }
        binding.imgRightArrow.setOnClickListener {
            binding.vpImages.currentItem =
                binding.vpImages.currentItem + 1
        }
    }

    private fun setupObservers() {
        imageDetailViewModel.imageList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            val index =
                it.indexOf(it.firstOrNull { it.id.equals(selectedImageId) })
            binding.vpImages.post {
                binding.vpImages.setCurrentItem(
                    index,
                    false
                )
            }
        })
    }
}

