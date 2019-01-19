package com.investagram.exam.moviedb.Adapters

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.investagram.exam.moviedb.Beans.Results

import com.investagram.exam.moviedb.R

import kotlin.collections.ArrayList

/**
 * Created by Lirio on 1/19/2019.
 */

class TrendingMoviesAdapter(private val activity: FragmentActivity?, private val mList: ArrayList<Results>?) : RecyclerView.Adapter<TrendingMoviesAdapter.TrendingMoviesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingMoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_trending_movies, parent, false)
        return TrendingMoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrendingMoviesViewHolder, position: Int) {
        var imageBaseUrl : String = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/" + mList!!.get(position).poster_path
        Glide.with(activity).load(imageBaseUrl).into(holder.ivMovie)

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    inner class TrendingMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMovie = itemView.findViewById<ImageView>(R.id.image_items_trending_movie)
    }
}
