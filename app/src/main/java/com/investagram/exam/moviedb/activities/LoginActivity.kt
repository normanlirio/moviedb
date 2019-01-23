package com.investagram.exam.moviedb.activities

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.investagram.exam.moviedb.api.APIResponse
import com.investagram.exam.moviedb.api.APIService
import com.investagram.exam.moviedb.api.RetrofitClient
import com.investagram.exam.moviedb.global.Constants
import com.investagram.exam.moviedb.global.Constants.API_KEY
import com.investagram.exam.moviedb.global.Variables
import com.investagram.exam.moviedb.model.User
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        button_login_signin.setOnClickListener {

            val username: String = text_login_username.text.toString()
            val password: String = text_login_password.text.toString()
            if (!username.equals("", true) && !password.equals("", true)) {
                val login = Login()
                login.execute(username, password)
            } else {
                Log.v("LOGIN", "User cancelled the operation.")
            }

        }
    }

    inner class Login : AsyncTask<String, String, String>() {
        private val progressDialog: ProgressDialog = ProgressDialog(this@LoginActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setMessage("Logging in...")
            progressDialog.setCancelable(true)
            progressDialog.show()
        }

        override fun doInBackground(vararg params: String?): String? {
            val retrofit: Retrofit? = RetrofitClient.getClient(Constants.BASE_URL)
            val login = retrofit?.create(APIService::class.java)
            try {
                //Get Token
                val token: APIResponse.RequestToken? = login?.getToken(API_KEY)?.execute()?.body()


                //Create Session with Login
                val user = User(params[0], params[1], token?.request_token)
                val requestToken: APIResponse.LoginResponse? = login?.login(API_KEY, user)?.execute()?.body()
                Log.v("MAIN", "Request Token ${requestToken?.request_token}")

                //Get Session ID
                val map: HashMap<String, String?>? = HashMap()
                map?.put("request_token", requestToken?.request_token)
                val sessionId: APIResponse.GetSessionId? = login?.getSessionId(API_KEY, map)?.execute()?.body()
                Log.v("MAIN", sessionId?.session_id)
                Variables.session_ID = sessionId?.session_id

                //Get Account details
                val accountId = login?.getAccountDetails(API_KEY, Variables.session_ID!!)?.execute()?.body()
                Variables.account_ID = accountId?.id
                Variables.login_username = accountId?.username

                return sessionId?.session_id
            }catch (e: Exception) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            if (result != null) {
                Variables.isLoggedIn = true
                setResult(10)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
