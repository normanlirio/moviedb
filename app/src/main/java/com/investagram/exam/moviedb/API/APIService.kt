package com.investagram.exam.moviedb.API

import com.investagram.exam.moviedb.Beans.User
import com.investagram.exam.moviedb.Model.APIResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by fluxion inc on 18/01/2019.
 */
interface APIService {
    @POST("/3/authentication/token/validate_with_login")
    fun login(@Query("api_key") apiKey:String, @Body user: User) : Call <APIResponse.LoginResponse>

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
}