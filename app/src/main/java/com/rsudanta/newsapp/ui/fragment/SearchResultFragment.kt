package com.rsudanta.newsapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsudanta.newsapp.adapter.NewsAdapter
import com.rsudanta.newsapp.databinding.FragmentSearchResultBinding
import com.rsudanta.newsapp.ui.NewsViewModel
import com.rsudanta.newsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()
    private val args: SearchResultFragmentArgs by navArgs()

    @Inject
    lateinit var newsAdapter: NewsAdapter
    lateinit var keyword: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        keyword = args.keyword

        setupUI(keyword)
        setupRecyclerView()
        loadData()

        binding.etSearch.setOnClickListener {
            val action =
                SearchResultFragmentDirections.actionSearchResultFragmentToSearchFragment(keyword)
            findNavController().navigate(action)
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    private fun setupUI(keyword: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvSearch.text =
                Html.fromHtml("Results for <b>$keyword</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            binding.tvSearch.text = Html.fromHtml("Results for <b>$keyword</b>")
        }
        binding.etSearch.setText(keyword)
    }

    private fun loadData() {
        viewModel.getSearchResult(keyword)

        viewModel.searchResult.observe(viewLifecycleOwner) { response ->
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
                        Log.e("SearchResultFragment", "An error occurred: $message")
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
        binding.rvSearchResult.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            newsAdapter.setOnItemClickListener { article ->
                val action =
                    SearchResultFragmentDirections.actionSearchResultFragmentToArticleFragment(
                        article.url
                    )
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