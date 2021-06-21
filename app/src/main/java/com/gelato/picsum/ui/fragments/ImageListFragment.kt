package com.gelato.picsum.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gelato.myapplication.ViewModelProviderFactory
import com.gelato.picsum.R
import com.gelato.picsum.databinding.FragmentImageListBinding
import com.gelato.picsum.di.ComponentFactory
import com.gelato.picsum.ui.adapters.ImageListAdapter
import com.gelato.picsum.ui.adapters.LoadingFooterAdapter
import com.gelato.picsum.ui.viewmodels.ImageListViewModel
import com.gelato.picsum.utils.SELECTED_IMAGE_ID_KEY
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragment to display Image Grid
 */
@ExperimentalPagingApi
class ImageListFragment : Fragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private val imageListViewModel: ImageListViewModel by viewModels { providerFactory }
    private val layoutId: Int = R.layout.fragment_image_list
    private lateinit var binding: FragmentImageListBinding
    private val imageListAdapter by lazy { ImageListAdapter(navigateToDetailPage()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentFactory.createFragmentComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        with(binding) {
            imageListAdapter.addLoadStateListener { loadState ->
                handleViewState(loadState)
            }
            rvImagesList.apply {
                adapter = imageListAdapter.withLoadStateFooter(
                    footer = LoadingFooterAdapter { imageListAdapter.retry() }
                )
                layoutManager =
                    StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL
                    )
            }
            btnRetry.setOnClickListener { imageListAdapter.retry() }
        }
    }

    private fun handleViewState(loadState: CombinedLoadStates) {
        binding.apply {
            if (imageListAdapter.itemCount > 0) {
                rvImagesList.isVisible = true
                pbLoading.isVisible = false
                btnRetry.isVisible = false
                tvNoResult.isVisible = false
            } else {
                rvImagesList.isVisible = false
                pbLoading.isVisible =
                    loadState.refresh is LoadState.Loading
                btnRetry.isVisible =
                    loadState.refresh is LoadState.Error
                tvNoResult.isVisible =
                    loadState.refresh is LoadState.Error
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            imageListViewModel.fetchImageList().collectLatest {
                imageListAdapter.submitData(it)
            }

            imageListAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvImagesList.scrollToPosition(0) }
        }
    }

    private fun navigateToDetailPage() =
        { selectedImageId: String ->
            val bundle = Bundle().apply {
                putString(SELECTED_IMAGE_ID_KEY, selectedImageId)
            }
            Navigation.findNavController(binding.root)
                .navigate(
                    R.id.action_imageListFragment_to_imageDetailFragment,
                    bundle
                )

        }
}