package com.investagram.exam.moviedb.API

import com.investagram.exam.moviedb.Beans.User
import com.investagram.exam.moviedb.Model.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by fluxion inc on 18/01/2019.
 */
interface APIService {
    @POST("/authentication/token/validate_with_login")
    fun login(@Query("api_key") apiKey:String, @Body user: User) : Call <Login.LoginResponse>
}