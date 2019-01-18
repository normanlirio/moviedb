package com.investagram.exam.moviedb.Model

/**
 * Created by fluxion inc on 18/01/2019.
 */
object Login {
    data class LoginResponse (val success : Boolean, val expires_at : String, val request_token : String)
}