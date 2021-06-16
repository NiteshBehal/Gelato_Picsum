package com.gelato.picsum.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gelato.picsum.R
import com.gelato.picsum.data.db.ImagesDb
import com.gelato.picsum.data.network.ImageListApi
import com.gelato.picsum.data.network.ServiceBuilder
import com.gelato.picsum.data.repository.PicsumRepo
import com.gelato.picsum.databinding.FragmentImageListBinding
import com.gelato.picsum.ui.adapters.ImageListAdapter
import com.gelato.picsum.ui.adapters.LoadingFooterAdapter
import com.gelato.picsum.ui.viewmodels.ImageListViewModel
import com.gelato.picsum.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class ImageListFragment : Fragment() {

    private lateinit var imageListViewModel: ImageListViewModel
    private val layoutId: Int = R.layout.fragment_image_list
    protected lateinit var binding: FragmentImageListBinding
    private val adapter by lazy { ImageListAdapter(navigateToDetailPage()) }

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
        setupUI()
        setupObservers()
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

    private fun setupUI() {
        with(binding) {

            adapter.addLoadStateListener { loadState ->
                rvPhotos.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible =
                    loadState.source.refresh is LoadState.Loading
                retryButton.isVisible =
                    loadState.source.refresh is LoadState.Error
            }

            rvPhotos.adapter = adapter.withLoadStateFooter(
                footer = LoadingFooterAdapter { adapter.retry() }
            )
            rvPhotos.layoutManager =
                StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )

            retryButton.setOnClickListener { adapter.retry() }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            imageListViewModel.fetchImageList().collectLatest {
                adapter.submitData(it)
            }

            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvPhotos.scrollToPosition(0) }
        }
    }

    private fun navigateToDetailPage() =
        { position: Int ->
            val bundle = Bundle().apply {
                putInt("SelectedImagePos", position)
            }
            Navigation.findNavController(binding.root)
                .navigate(
                    R.id.action_imageListFragment_to_imageDetailFragment,
                    bundle
                )
        }
}