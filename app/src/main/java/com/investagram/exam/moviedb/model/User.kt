package com.investagram.exam.moviedb.model

class User {
    val username :String?
    val password: String?
    val request_token : String?

    constructor(username: String?, password: String?, request_token : String?) {
        this.username = username
        this.password = password
        this.request_token = request_token
    }
}