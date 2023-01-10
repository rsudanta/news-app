package com.rsudanta.newsapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsudanta.newsapp.adapter.SearchHistoryAdapter
import com.rsudanta.newsapp.databinding.FragmentSearchBinding
import com.rsudanta.newsapp.models.SearchHistory
import com.rsudanta.newsapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()
    private val args: SearchResultFragmentArgs by navArgs()

    @Inject
    lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        showKeyboard()
        editTextSetup()
        setupRecyclerView()
        loadData()

        binding.ivClear.setOnClickListener {
            binding.etSearch.text.clear()
        }

        binding.etSearch.setOnEditorActionListener { _, id, _ ->
            return@setOnEditorActionListener when (id) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val keyword = binding.etSearch.text.toString()
                    val searchHistory = SearchHistory(keyword = keyword)
                    if (keyword.isNotEmpty()) {
                        viewModel.saveSearchHistory(searchHistory)
                        val action =
                            SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                                keyword
                            )
                        findNavController().navigate(action)
                    }
                    true
                }
                else -> false
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvSearchHistory.apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            searchHistoryAdapter.onClickListener(object : SearchHistoryAdapter.OnClickListener {
                override fun onDeleteClick(keyword: String) {
                    viewModel.deleteSearchHistory(keyword)
                }

                override fun onKeywordClick(keyword: String) {
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(keyword)
                    findNavController().navigate(action)
                    viewModel.saveSearchHistory(SearchHistory(keyword = keyword))
                }
            })
        }
    }

    private fun editTextSetup() {
        val keyword = args.keyword
        if(keyword.isNotEmpty()){
            binding.ivClear.visibility = View.VISIBLE
        }
        binding.etSearch.setText(keyword)
        binding.etSearch.setSelection(keyword.length)
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().isEmpty()) {
                binding.ivClear.visibility = View.GONE
            } else {
                binding.ivClear.visibility = View.VISIBLE
            }
        }
    }

    private fun showKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        binding.etSearch.postDelayed({
            binding.etSearch.requestFocus()
            imm?.showSoftInput(binding.etSearch, 0)
        }, 100)
    }

    private fun loadData() {
        viewModel.getSearchHistory()
        viewModel.searchHistory.observe(viewLifecycleOwner) { histories ->
            searchHistoryAdapter.differ.submitList(histories)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}