package com.investagram.exam.moviedb.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.investagram.exam.moviedb.API.APIService
import com.investagram.exam.moviedb.API.RetrofitClient
import com.investagram.exam.moviedb.Beans.User
import com.investagram.exam.moviedb.Global.API_KEY
import com.investagram.exam.moviedb.Model.Login
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        text_login_username.setText("normz1")
        text_login_password.setText("123456")
        button_login_signin.setOnClickListener(View.OnClickListener {
            val username: String = text_login_username.text.toString()
            val password: String = text_login_password.text.toString()
            var user = User(username, password)
            val retrofit: Retrofit? = RetrofitClient.getClient("https://api.themoviedb.org/")
            retrofit!!
            val login = retrofit.create(APIService::class.java)
            login.login(API_KEY, user).enqueue(object : Callback<Login.LoginResponse> {
                override fun onFailure(call: Call<Login.LoginResponse>?, t: Throwable?) {

                    Log.v("MAIN", "FAILED LOGIN")
                }

                override fun onResponse(call: Call<Login.LoginResponse>?, response: Response<Login.LoginResponse>?) {
                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    Log.v("MAIN", "SUCCESS LOGIN")
                }

            })

        })
    }
}
