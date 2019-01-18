package com.investagram.exam.moviedb.Beans

/**
 * Created by fluxion inc on 18/01/2019.
 */
public class User {
    val username :String
    val password: String
    val requestToken : String = ""

    constructor(username:String, password:String) {
        this.username = username
        this.password = password
    }
    constructor() {
        username = ""
        password = ""
    }
}