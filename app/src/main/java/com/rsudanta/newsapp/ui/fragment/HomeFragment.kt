package com.rsudanta.newsapp.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.rsudanta.newsapp.adapter.BreakingNewsAdapter
import com.rsudanta.newsapp.adapter.CategoryAdapter
import com.rsudanta.newsapp.databinding.FragmentHomeBinding
import com.rsudanta.newsapp.ui.NewsViewModel
import com.rsudanta.newsapp.util.ItemSpacingDecoration
import com.rsudanta.newsapp.util.LinePagerIndicatorDecoration
import com.rsudanta.newsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var breakingNewsAdapter: BreakingNewsAdapter

    @Inject
    lateinit var categoryAdapter: CategoryAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    var onScroll: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        lifecycleScope.launchWhenResumed {
            delay(5000)
            onScroll = false
            autoScrollFeaturesList()
        }
        viewModel.news.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { news ->
                        breakingNewsAdapter.differ.submitList(news.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("HomeNewsFragment", "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        return binding.root
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.rvBreakingNews.apply {
            adapter = breakingNewsAdapter
            linearLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            val pagerIndicatorDecoration = LinePagerIndicatorDecoration()
            addItemDecoration(pagerIndicatorDecoration)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }

        binding.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(activity, 4)
            val itemSpacingDecoration = ItemSpacingDecoration(40)
            addItemDecoration(itemSpacingDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private tailrec suspend fun autoScrollFeaturesList() {

        if (linearLayoutManager.findLastVisibleItemPosition() < breakingNewsAdapter.itemCount - 1 && !onScroll) {
            linearLayoutManager.smoothScrollToPosition(
                binding.rvBreakingNews,
                RecyclerView.State(),
                linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1
            )
        } else if (linearLayoutManager.findLastVisibleItemPosition() == breakingNewsAdapter.itemCount - 1 && !onScroll) {
            linearLayoutManager.smoothScrollToPosition(
                binding.rvBreakingNews,
                RecyclerView.State(),
                0
            )
        }

        binding.rvBreakingNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                onScroll = false
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScroll = true
            }
        })
        delay(5000L)
        autoScrollFeaturesList()
    }
}

