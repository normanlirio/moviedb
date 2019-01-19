package com.investagram.exam.moviedb.Model

import com.investagram.exam.moviedb.Beans.Genre
import com.investagram.exam.moviedb.Beans.Results
import java.util.ArrayList

/**
 * Created by fluxion inc on 18/01/2019.
 */
object APIResponse {
    data class LoginResponse (val success : Boolean, val expires_at : String, val request_token : String)
    data class RequestToken(val success : Boolean, val expires_at : String, val request_token : String)
    data class GetSessionId(val success :Boolean, val session_id:String)
    data class TrendingMovies(val page:Int, val results : ArrayList<Results>?)
    data class SearchMovies(val page: Int, val total_results: Int, val total_pages:Int, val results: ArrayList<Results>?)
    data class MovieDetails(val adult:Boolean, val genres:ArrayList<Genre>?, val homepage:String, val id:String, val original_language:String,
                            val original_title:String, val overview:String, val popularity:Double, val poster_path:String, val tagline:String,
                            val vote_average:String
                            )
}