package com.linkdev.newsfeeds.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.linkdev.newsfeeds.R
import com.linkdev.newsfeeds.data.model.Article
import com.linkdev.newsfeeds.databinding.NewsItemBinding
import com.linkdev.newsfeeds.utils.AppUtils
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val context: Context,
    private val items: MutableList<Article>,
    private val itemClickListener: OnArticleClickListener,
) : RecyclerView.Adapter<NewsAdapter.ItemHolder>() {

    fun clearList() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Article>) {
        if (items.isEmpty()) {
            items.addAll(newItems)
            notifyDataSetChanged()
        } else {
            val lastPosition = items.size - 1

            items.addAll(newItems)

            notifyItemRangeInserted(lastPosition + 1, items.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding: NewsItemBinding =
            NewsItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ItemHolder, position: Int) {
        val item = items[position]

        Glide.with(context)
            .load(item.urlToImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(viewHolder.itemBinding.newsItemImage)

        viewHolder.itemBinding.newsItemTitle.text = item.title

        val authorText = context.getString(R.string.by_author, item.author)
        viewHolder.itemBinding.newsItemAuthor.text = authorText

        try {
            var mFormatter = SimpleDateFormat(AppUtils.API_DATE_FORMAT, Locale.ENGLISH)
            mFormatter.isLenient = false

            var dateText = item.publishedAt
            val date = mFormatter.parse(dateText)

            mFormatter = SimpleDateFormat(AppUtils.APP_DATE_FORMAT, Locale.ENGLISH)
            mFormatter.isLenient = false

            dateText = mFormatter.format(date!!)

            viewHolder.itemBinding.newsItemDate.text = dateText
        } catch (e: Exception) {
            viewHolder.itemBinding.newsItemDate.text = item.publishedAt
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemHolder(val itemBinding: NewsItemBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {

        init {
            itemBinding.root.setOnClickListener {
                itemClickListener.onArticleClick(items[bindingAdapterPosition])
            }
        }

    }

    interface OnArticleClickListener {
        fun onArticleClick(article: Article)
    }
}