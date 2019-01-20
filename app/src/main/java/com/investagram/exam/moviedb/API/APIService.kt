package com.investagram.exam.moviedb.API

import com.investagram.exam.moviedb.Model.Rating
import com.investagram.exam.moviedb.Model.User

import com.investagram.exam.moviedb.Model.WatchlistMovie
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by fluxion inc on 18/01/2019.
 */
interface APIService {
    @POST("/3/authentication/token/validate_with_login")
    fun login(@Query("api_key") apiKey:String, @Body user: User) : Call <APIResponse.LoginResponse>

    @GET("/3/account")
    fun getAccountDetails(@Query("api_key") apiKey: String, @Query("session_id") session_id:String ) : Call <APIResponse.AccountDetails>

    @GET("/3/trending/all/day")
    fun getTrendingMovies(@Query("api_key") apiKey:String) : Call <APIResponse.TrendingMovies>

    @GET("/3/authentication/token/new")
    fun getToken(@Query("api_key") apiKey:String) : Call <APIResponse.RequestToken>

    @POST("/3/authentication/session/new")
    fun getSessionId(@Query("api_key") apiKey: String, @Body map : HashMap<String,String?>?) : Call<APIResponse.GetSessionId>

    @GET("/3/search/movie")
    fun searchMovie(@Query("api_key") apiKey:String, @Query("query") query: String) : Call <APIResponse.SearchMovies>

    @GET("/3/movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movie_id:String,@Query("api_key") apiKey: String  ) : Call <APIResponse.MovieDetails>

    @POST("/3/movie/{movie_id}/rating")
    fun rateMovie(@Path("movie_id")movie_id:Int, @Query("api_key") apiKey:String, @Query("session_id") session_id:String?, @Body rating: Rating) : Call <APIResponse.RateMovie>

    @POST("/3/account/{account_id}/watchlist")
    fun addToWatchlist(@Path("account_id") account_id:Int, @Query("api_key") apiKey: String, @Query("session_id") session_id:String,
                      @Body watchlist: WatchlistMovie) : Call<APIResponse.AddWatchlist>

    @GET("/3/account/{account_id}/watchlist/movies")
    fun getWatchlist(@Path("account_id")account_id:Int, @Query("api_key")apiKey: String, @Query("session_id") session_id:String) : Call <APIResponse.TrendingMovies>

    @GET("/3/movie/{movie_id}/reviews")
    fun getMovieReview(@Path("movie_id") movie_id:Int, @Query("api_key") apiKey: String) : Call<APIResponse.MovieReview>

    @GET("/3/movie/{movie_id}/account_states")
    fun getAccountStateBoolean(@Path("movie_id") movie_id: Int , @Query("api_key") apiKey: String, @Query("session_id") session_id:String?) : Call <APIResponse.AccountStateBoolean>

    @GET("/3/movie/{movie_id}/account_states")
    fun getAccountStateObject(@Path("movie_id") movie_id: Int , @Query("api_key") apiKey: String, @Query("session_id") session_id:String?) : Call <APIResponse.AccountStateObject>


    @DELETE("/3/movie/{movie_id}/rating")
    fun deleteRating(@Path("movie_id") movie_id:Int, @Query("api_key") apiKey: String, @Query("session_id") session_id:String?) : Call<APIResponse.RateMovie>

}