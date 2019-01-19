package com.investagram.exam.moviedb.Beans

/**
 * Created by fluxion inc on 18/01/2019.
 */
class User {
    val username :String?
    val password: String?
    val request_token : String?

    constructor(username: String?, password: String?, request_token : String?) {
        this.username = username
        this.password = password
        this.request_token = request_token
    }
    constructor() {
        username = ""
        password = ""
        request_token = ""
    }
}