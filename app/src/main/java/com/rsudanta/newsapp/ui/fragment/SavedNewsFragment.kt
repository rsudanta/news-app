package com.rsudanta.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsudanta.newsapp.adapter.SavedNewsAdapter
import com.rsudanta.newsapp.databinding.FragmentSavedNewsBinding
import com.rsudanta.newsapp.ui.NewsViewModel
import com.rsudanta.newsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedNewsFragment : Fragment() {
    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var savedNewsAdapter: SavedNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadData()

        return binding.root
    }

    private fun loadData() {
        viewModel.savedArticle.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { article ->
                        savedNewsAdapter.differ.submitList(article)
                        if (article.isEmpty()) {
                            binding.rlEmpty.visibility = View.VISIBLE
                        } else {
                            binding.rlEmpty.visibility = View.INVISIBLE
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("savedNewsFragment", "An error occurred: $message")
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
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.rvSavedNews.apply {
            adapter = savedNewsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            savedNewsAdapter.setOnItemClickListener { article ->
                val action =
                    SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}