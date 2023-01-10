package com.rsudanta.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsudanta.newsapp.R
import com.rsudanta.newsapp.databinding.ItemCategoryPreviewBinding
import com.rsudanta.newsapp.models.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryAdapter>() {

    private var onClickListener: ((String) -> Unit)? = null

    private val categories =
        listOf(
            Category("Business", R.drawable.ic_business),
            Category("Entertainment", R.drawable.ic_entertainment),
            Category("General", R.drawable.ic_general),
            Category("Health", R.drawable.ic_health),
            Category("Science", R.drawable.ic_science),
            Category("Sports", R.drawable.ic_sport),
            Category("Technology", R.drawable.ic_tech),
        )

    inner class CategoryAdapter(binding: ItemCategoryPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivIconCategory = binding.ivIconCategory
        val tvTitleCategory = binding.tvTitleCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter {
        return CategoryAdapter(
            ItemCategoryPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryAdapter, position: Int) {
        val category = categories[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(category.icon)
                .into(holder.ivIconCategory)
            holder.tvTitleCategory.text = category.title
        }

        holder.itemView.setOnClickListener {
            onClickListener?.let {
                it(category.title)
            }
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        onClickListener = listener
    }


}