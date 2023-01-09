package com.rsudanta.newsapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rsudanta.newsapp.databinding.FragmentSearchResultBinding
import com.rsudanta.newsapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()
    private val args: SearchResultFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val keyword = args.keyword

        setupUI(keyword)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}