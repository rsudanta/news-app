package com.rsudanta.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsudanta.newsapp.adapter.NewsAdapter
import com.rsudanta.newsapp.databinding.FragmentCategoryBinding
import com.rsudanta.newsapp.databinding.FragmentSavedNewsBinding
import com.rsudanta.newsapp.ui.NewsViewModel
import com.rsudanta.newsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()
    private val args: CategoryFragmentArgs by navArgs()

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadData()

        return binding.root
    }

    private fun loadData() {
        val category = args.category
        viewModel.getCategorizedNews(category)

        viewModel.categorizedNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { news ->
                        newsAdapter.differ.submitList(news.articles)
                        if (news.articles.isEmpty()) {
                            binding.rlEmpty.visibility = View.VISIBLE
                        } else {
                            binding.rlEmpty.visibility = View.INVISIBLE
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("CategoryFragment", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    newsAdapter.differ.submitList(emptyList())
                    showProgressBar()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            newsAdapter.setOnItemClickListener { article ->
                val action =
                    CategoryFragmentDirections.actionCategoryFragmentToArticleFragment(article.url)
                findNavController().navigate(action)
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}