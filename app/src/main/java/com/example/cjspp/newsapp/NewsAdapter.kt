package com.example.cjspp.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cjspp.R
import com.example.cjspp.databinding.CustomNewsArticleBinding

class NewsAdapter(val context:Context) : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    class MyViewHolder(val itemBinding:CustomNewsArticleBinding):RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = CustomNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(layout)
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<NewsModel>(){
        override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem.image == newItem.image && oldItem.title == newItem.createdBy &&
                    oldItem.description == newItem.description && oldItem.createdBy == newItem.createdBy
                    && oldItem.dateCreated == newItem.dateCreated

        }

        override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem == newItem
        }

    }

    val diff = AsyncListDiffer(this, diffCallBack)

    override fun getItemCount(): Int {
        return  diff.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentNews = diff.currentList[position]

        Glide.with(context).load(currentNews.image).into(holder.itemBinding.imageViewNews)
        holder.itemBinding.tvNewsTitle.text = currentNews.title
        holder.itemBinding.tvNewsDescription.text = currentNews.description
//        holder.itemBinding.tvCreatedBy.text = currentNews.createdBy
        holder.itemBinding.tvDateCreated.text = currentNews.dateCreated
    }
}