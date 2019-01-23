package com.investagram.exam.moviedb.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.investagram.exam.moviedb.model.Results
import com.investagram.exam.moviedb.fragments.MovieDetails

import com.investagram.exam.moviedb.R

import kotlin.collections.ArrayList

class TrendingMoviesAdapter(private val activity: FragmentActivity?, private val mList: ArrayList<Results>?) : RecyclerView.Adapter<TrendingMoviesAdapter.TrendingMoviesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingMoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_trending_movies, parent, false)
        return TrendingMoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrendingMoviesViewHolder, position: Int) {
        var posterPath = mList!![position].poster_path

        var imageBaseUrl = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/$posterPath"
        Glide.with(activity).load(imageBaseUrl).into(holder.ivMovie)

        holder.itemView.setOnClickListener({
           val fragmentManager : FragmentManager = activity!!.supportFragmentManager
            val ft : FragmentTransaction = fragmentManager.beginTransaction()
            val frag : Fragment = MovieDetails()
            val bundle = Bundle()
            bundle.putInt("id", mList[position].id)
            frag.arguments = bundle
            ft.replace(R.id.fragment_container, frag).addToBackStack(null)
            ft.commit()
        })

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    inner class TrendingMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMovie: ImageView = itemView.findViewById<ImageView>(R.id.image_items_trending_movie)
    }
}
