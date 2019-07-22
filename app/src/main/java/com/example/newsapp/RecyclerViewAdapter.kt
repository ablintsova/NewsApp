package com.example.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.imgArticle)
    var title: TextView = view.findViewById(R.id.tvTitle)
    var date: TextView = view.findViewById(R.id.tvDate)
    var description: TextView = view.findViewById(R.id.tvDescription)
}

class RecyclerViewAdapter(private var articleList: List<Article>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (articleList.isNotEmpty()) articleList.size else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (articleList.isEmpty()) return
        val articleItem = articleList[position]

        Picasso.get()
            .load(articleItem.imageURL)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbnail)

        holder.title.text = articleItem.title
        holder.date.text = articleItem.date
        holder.description.text = articleItem.description
    }

    fun updateArticleList(newData: List<Article>) {
        articleList = newData
        notifyDataSetChanged()
    }

    fun getArticle(position: Int): Article? {
        return if(articleList.isNotEmpty()) articleList[position] else null
    }
}