package com.investagram.exam.moviedb.Activities

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.investagram.exam.moviedb.API.APIService
import com.investagram.exam.moviedb.API.RetrofitClient
import com.investagram.exam.moviedb.Model.User
import com.investagram.exam.moviedb.API.APIResponse
import com.investagram.exam.moviedb.Global.*
import com.investagram.exam.moviedb.Global.Constants.API_KEY
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.activity_login.*
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
            if(!username.equals("", true) && !password.equals("", true)) {
                val login = Login()
                login.execute(username, password)
            } else {
                Toast.makeText(this@LoginActivity, "Username and Password are required", Toast.LENGTH_SHORT)
            }

        })
    }

    inner class Login : AsyncTask<String, String, String>() {
        val progressDialog : ProgressDialog = ProgressDialog(this@LoginActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setMessage("Logging in...")
            progressDialog.setCancelable(true)
            progressDialog.show()
        }
        override fun doInBackground(vararg params: String?): String? {

            val retrofit: Retrofit? = RetrofitClient.getClient("https://api.themoviedb.org/")
            val login = retrofit?.create(APIService::class.java)
            //GEt Token
            val token : APIResponse.RequestToken? = login?.getToken(API_KEY)?.execute()?.body()


            //Create Session with Login
            var user = User(params[0], params[1], token?.request_token)
            val requestToken : APIResponse.LoginResponse? = login?.login(API_KEY, user)?.execute()?.body()
            Log.v("MAIN", "Request Token ${requestToken?.request_token}")

            //Get Session ID
            var map : HashMap<String, String?>? = HashMap()
            map?.put("request_token", requestToken?.request_token)
            val sessionId : APIResponse.GetSessionId? = login?.getSessionId(API_KEY, map)?.execute()?.body()
            Log.v("MAIN", sessionId?.session_id)
            Variables.session_ID  = sessionId?.session_id

            //Get Account details
            val accountId = login?.getAccountDetails(API_KEY, Variables.session_ID!!)?.execute()?.body()
            Variables.account_ID = accountId?.id
            Variables.login_username = accountId?.username


            return sessionId?.session_id
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            if(result != null) {
                Variables.isLoggedIn = true
                setResult(10)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT)
            }

        }

    }
}
