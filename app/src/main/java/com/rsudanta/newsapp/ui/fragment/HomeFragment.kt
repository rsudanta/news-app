package com.rsudanta.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.rsudanta.newsapp.R
import com.rsudanta.newsapp.adapter.BreakingNewsAdapter
import com.rsudanta.newsapp.adapter.CategoryAdapter
import com.rsudanta.newsapp.adapter.NewsAdapter
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
    lateinit var newsAdapter: NewsAdapter

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
        loadData()

        return binding.root
    }

    private fun loadData() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { breakingNews ->
                        breakingNewsAdapter.differ.submitList(breakingNews.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("HomeNewsFragment", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        viewModel.news.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { news ->
                        newsAdapter.differ.submitList(news.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("HomeNewsFragment", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.tvExplore.visibility = View.VISIBLE
        binding.rvCategory.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.INVISIBLE
        binding.tvExplore.visibility = View.INVISIBLE
        binding.rvCategory.visibility = View.INVISIBLE
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
            breakingNewsAdapter.setOnItemClickListener { article ->
                val action = HomeFragmentDirections.actionHomeFragmentToArticleFragment(article)
                findNavController().navigate(action)
            }
        }

        binding.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(activity, 4)
            val itemSpacingDecoration = ItemSpacingDecoration(40)
            addItemDecoration(itemSpacingDecoration)
            categoryAdapter.setOnClickListener { category ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToCategoryFragment(category)
                newsAdapter.differ.submitList(emptyList())
                findNavController().navigate(action)
            }
        }

        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            newsAdapter.setOnItemClickListener { article ->
                val action = HomeFragmentDirections.actionHomeFragmentToArticleFragment(article)
                findNavController().navigate(action)
            }
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

