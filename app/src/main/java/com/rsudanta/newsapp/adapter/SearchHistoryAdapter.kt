package com.rsudanta.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rsudanta.newsapp.databinding.ItemSearchHistoryPreviewBinding
import com.rsudanta.newsapp.models.SearchHistory

class SearchHistoryAdapter : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class SearchHistoryViewHolder(binding: ItemSearchHistoryPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvSearchHistory = binding.tvSearchHistory
        val ivDeleteHistory = binding.ivDeleteHistory
    }

    private val differCallback = object : DiffUtil.ItemCallback<SearchHistory>() {
        override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        return SearchHistoryViewHolder(
            ItemSearchHistoryPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val searchHistory = differ.currentList[position]
        holder.itemView.apply {
            holder.tvSearchHistory.text = searchHistory.keyword
            holder.ivDeleteHistory.setOnClickListener {
                onClickListener?.onDeleteClick(searchHistory.keyword)
            }
            holder.itemView.setOnClickListener {
                onClickListener?.onKeywordClick(searchHistory.keyword)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun onClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onDeleteClick(keyword: String)
        fun onKeywordClick(keyword: String)
    }

}