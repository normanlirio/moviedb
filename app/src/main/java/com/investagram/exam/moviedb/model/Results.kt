package com.investagram.exam.moviedb.model

class Results(val adult: Boolean = false,val backdrop_path:String ="",val genre_ids : ArrayList<Int>? = null,val id :Int = 0,
              val original_language :String ="", val original_title:String="",val overview:String="",val poster_path:String="",val release_date:String="",
              val title:String="",val video:Boolean = false,val vote_average: Double = 0.0,val vote_count: Int =0,val popularity:Double = 0.0
              )

