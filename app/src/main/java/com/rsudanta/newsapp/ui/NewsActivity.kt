package com.rsudanta.newsapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rsudanta.newsapp.R
import com.rsudanta.newsapp.adapter.NewsAdapter
import com.rsudanta.newsapp.databinding.ActivityNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.newsNavHostFragment)
        setupToolbar()
        setupUI()

    }

    private fun setupUI() {
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            when (navDestination.id) {
                R.id.homeFragment -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.savedNewsFragment -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.categoryFragment -> {
                    showToolbar()
                    hideBottomNav()
                }
                R.id.articleFragment -> {
                    showToolbar()
                    hideBottomNav()
                }
                else -> {
                    hideBottomNav()
                    hideToolbar()
                }
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }


    private fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    private fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        navController.addOnDestinationChangedListener { _, navDestination, args ->
            when (navDestination.id) {
                R.id.categoryFragment -> {
                    val category: String = args?.get("category") as String
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    supportActionBar?.title = category
                    binding.ivLogo.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
                R.id.articleFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    binding.ivLogo.visibility = View.VISIBLE
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
                else -> {
                    binding.ivLogo.visibility = View.VISIBLE
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            newsAdapter.differ.submitList(emptyList())
            navController.popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val item = menu?.findItem(R.id.actionSearch)

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            when (navDestination.id) {
                R.id.savedNewsFragment -> item?.isVisible = false
                R.id.articleFragment -> item?.isVisible = false
                else -> item?.isVisible = true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId

        if (id == R.id.actionSearch) {
            navController.navigate(R.id.searchFragment)

        }
        return super.onOptionsItemSelected(item)
    }
}