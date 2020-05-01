package com.leehendryp.stoneandroidchallenge.feed.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leehendryp.stoneandroidchallenge.R
import com.leehendryp.stoneandroidchallenge.core.utils.toDp
import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import kotlinx.android.synthetic.main.item_feed.view.text_joke_category
import kotlinx.android.synthetic.main.item_feed.view.text_joke_value

class FeedAdapter(
    private val context: Context,
    private val jokes: MutableSet<Joke>,
    private val onClickShare: (Joke) -> Unit
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    companion object {
        private const val MAX_LENGTH = 80
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = jokes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentJoke = jokes.toList()[position]
        holder.bind(currentJoke)
        holder.itemView.setOnClickListener { onClickShare(currentJoke) }
    }

    fun update(jokes: Set<Joke>) {
        this.jokes.addAll(jokes)
        notifyDataSetChanged()
    }

    fun clearList() {
        jokes.clear()
        notifyItemRangeRemoved(0, jokes.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content = view.text_joke_value
        private val category = view.text_joke_category

        fun bind(joke: Joke) {
            with(joke) {
                content.text = this.value
                content.textSize = getProperTextSizeFrom(this)
                category.text = getLabelFrom(this)
            }
        }

        private fun getProperTextSizeFrom(joke: Joke): Float {
            return if (joke.value.length <= MAX_LENGTH) 16f.toDp(context)
            else 24f.toDp(context)
        }

        private fun getLabelFrom(joke: Joke): String {
            return if (joke.categories.isEmpty())
                context.getString(R.string.item_category_label_uncategorized)
            else joke.categories[0]
        }
    }
}