package com.investagram.exam.moviedb.API

import com.investagram.exam.moviedb.Model.Genre
import com.investagram.exam.moviedb.Model.Rating
import com.investagram.exam.moviedb.Model.Results
import com.investagram.exam.moviedb.Model.ReviewResults
import java.util.*

/**
 * Created by fluxion inc on 18/01/2019.
 */
object APIResponse {

    data class LoginResponse(val success: Boolean, val expires_at: String, val request_token: String)
    data class RequestToken(val success: Boolean, val expires_at: String, val request_token: String)
    data class GetSessionId(val success: Boolean, val session_id: String)
    data class TrendingMovies(val page: Int, val results: ArrayList<Results>?)
    data class SearchMovies(val page: Int, val total_results: Int, val total_pages: Int, val results: ArrayList<Results>?)
    data class MovieDetails(val adult: Boolean, val genres: ArrayList<Genre>?, val homepage: String, val id: String, val original_language: String,
                            val original_title: String, val overview: String, val popularity: Double, val poster_path: String, val tagline: String,
                            val vote_average: String, val status: String
    )

    data class RateMovie(val status_code: Int, val status_message: String?)

    data class AccountDetails(val id: Int, val name: String, val username: String, val iso_639_1: String, val iso_3166_1: String)
    data class AddWatchlist(val status_code: Int, val status_message: String)
    data class MovieReview(val id: Int, val page: Int, val results: ArrayList<ReviewResults>?, val total_pages: Int, val total_results: Int)
    data class AccountStateObject(val id: Int, val favorite: Boolean, val rated: Rating, val watchlist: Boolean)
    data class AccountStateBoolean(val id: Int, val favorite: Boolean, val rated: Boolean, val watchlist: Boolean)

}