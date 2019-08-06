package com.example.newsapp.view.util

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.view.FullArticleActivity
import com.squareup.picasso.Picasso

class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var article : Article? = null
    private var thumbnail: ImageView = view.findViewById(R.id.imgArticle)
    private var title: TextView = view.findViewById(R.id.tvTitle)
    private var date: TextView = view.findViewById(R.id.tvDate)
    private var description: TextView = view.findViewById(R.id.tvDescription)

    init {
        view.setOnClickListener {
            article?.articleURL?.let { url ->
                val intent = Intent(view.context, FullArticleActivity::class.java)
                intent.putExtra("ArticleUrl", article?.articleURL)
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(article: Article?) {
        this.article = article

        Picasso.get()
            .load(article?.imageURL)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(thumbnail)

        title.text = article?.title
        date.text = article?.date
        description.text = article?.description
    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_list_item, parent, false)
            return ArticleViewHolder(view)
        }
    }
}