package com.rsudanta.newsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.rsudanta.newsapp.R
import com.rsudanta.newsapp.databinding.FragmentArticleBinding
import com.rsudanta.newsapp.models.Article
import com.rsudanta.newsapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()
    private val args: ArticleFragmentArgs by navArgs()
    private lateinit var savedArticle: List<Article>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        savedArticle = emptyList()

        viewModel.savedArticle.observe(viewLifecycleOwner) { response ->
            savedArticle = response.data ?: emptyList()
            var saved = false
            for (article in savedArticle) {
                if (article.url == args.article.url) {
                    saved = true
                }
            }

            if (saved) {
                binding.ivSaveNews.setImageResource(R.drawable.ic_bookmark)
            } else {
                binding.ivSaveNews.setImageResource(R.drawable.ic_bookmark_off)
            }

            binding.ivSaveNews.setOnClickListener {
                if (saved) {
                    viewModel.deleteSavedArticle(article.url)
                    binding.ivSaveNews.setImageResource(R.drawable.ic_bookmark_off)
                } else {
                    viewModel.saveArticle(article)
                    binding.ivSaveNews.setImageResource(R.drawable.ic_bookmark)
                }
            }
        }

        binding.ivShareNews.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, article.url)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}