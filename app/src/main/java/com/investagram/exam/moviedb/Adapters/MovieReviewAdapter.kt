package com.investagram.exam.moviedb.Adapters

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.investagram.exam.moviedb.Model.Results
import com.investagram.exam.moviedb.Model.ReviewResults
import com.investagram.exam.moviedb.R

/**
 * Created by Lirio on 1/20/2019.
 */
class MovieReviewAdapter(private val activity: FragmentActivity?, private val mList: ArrayList<ReviewResults>?) : RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_review_layout, parent, false)
        return MovieReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mList!!.size
    }

    override fun onBindViewHolder(holder: MovieReviewViewHolder, position: Int) {
        holder.tvContent.text = mList!!.get(position).content
        holder.tvAuthor.text = mList!!.get(position).author
    }

    class MovieReviewViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            val tvContent = itemView!!.findViewById<TextView>(R.id.text_review_item_content)
            val tvAuthor = itemView!!.findViewById<TextView>(R.id.ext_review_item_author)
    }

}