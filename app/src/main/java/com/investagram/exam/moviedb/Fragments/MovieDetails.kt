package com.investagram.exam.moviedb.Fragments

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.investagram.exam.moviedb.API.APIResponse
import com.investagram.exam.moviedb.API.APIService
import com.investagram.exam.moviedb.Global.*
import com.investagram.exam.moviedb.Global.Constants.API_KEY
import com.investagram.exam.moviedb.Global.Constants.GENERIC_TITLE_POPUP
import com.investagram.exam.moviedb.Global.Constants.REQUEST_LOGIN
import com.investagram.exam.moviedb.Model.Rating
import com.investagram.exam.moviedb.Model.WatchlistMovie
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetails : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var movieId: Int = 0


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setCustomActionbar(activity as AppCompatActivity, "moviedetails")
        val bundle: Bundle? = this.arguments
        if (bundle != null) {
            movieId = bundle.getInt("id")
        }

        relative_moviedetails_container.visibility = View.GONE
        val movie = Movie()
        movie.execute()

        bottom_navigation.setOnNavigationItemSelectedListener(this)

        button_moviedetails_addtowatchlist.setOnClickListener {
            if (Variables.isLoggedIn) {
                val watch = Watchlist()
                watch.execute()
            } else {
                askToLoginPopup(activity as AppCompatActivity, GENERIC_TITLE_POPUP, "Please login to add this movie to watchlist")
            }
        }

        text_moviedetails_seereviews.setOnClickListener {
            val fragment = MovieReview()
            val bundle = Bundle()
            bundle.putInt("movieid", movieId)
            fragment.arguments = bundle

            switchFragment(activity as AppCompatActivity, fragment)
        }

        linear_moviedetails_userrating.setOnClickListener {
            if (Variables.isLoggedIn) {
                popUpSubmitRating(activity as AppCompatActivity)
            } else {
                askToLoginPopup(activity as AppCompatActivity, GENERIC_TITLE_POPUP, "Please login to rate movie")
            }
        }

        if (Variables.isLoggedIn) {
            GetUserMovieRating().execute()
            linear_moviedetails_userrating.visibility = View.VISIBLE
        }

        text_moviedetails_removerating.setOnClickListener {
            DeleteRating().execute()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            if(resultCode == REQUEST_LOGIN) {
                linear_moviedetails_userrating.visibility = View.VISIBLE

                GetUserMovieRating().execute()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> switchFragment(context, Home())
            R.id.action_wathchlist -> if (Variables.isLoggedIn) {
                switchFragment(context, WatchlistFragment())
            } else {
                askToLoginPopup(activity as AppCompatActivity, GENERIC_TITLE_POPUP, "Please login to see you watchlist")
            }
            R.id.action_settings -> switchFragment(context, SettingsFragment())
        }
        return true
    }

    private fun disableWatchlistButton(watchlist: Boolean) {
        if (watchlist) {
            button_moviedetails_addtowatchlist.isEnabled = false
            button_moviedetails_addtowatchlist.setBackgroundColor(resources.getColor(R.color.md_grey_700))
        } else {
            button_moviedetails_addtowatchlist.isEnabled = true
            button_moviedetails_addtowatchlist.setBackgroundColor(resources.getColor(R.color.moviedb_green))
        }
    }

    private fun popUpSubmitRating(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.rating_layout, null)
        dialogBuilder.setView(dialogView)
        val rbRating = dialogView.findViewById<RatingBar>(R.id.ratingBar_rate)
        val btnSubmit = dialogView.findViewById<TextView>(R.id.button_rate_submit)


        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        rbRating.rating = rating_moviedetails_userrating.rating
        btnSubmit.setOnClickListener {
            SubmitRating().execute(rbRating.rating.toDouble())
            alertDialog.dismiss()
        }
    }


    inner class DeleteRating : AsyncTask<String, String, String>() {
        val pd: ProgressDialog = ProgressDialog(activity)
        var message: String = ""
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            val client = retrofitClient()?.create(APIService::class.java)
            val deleteRate: APIResponse.RateMovie? = client?.deleteRating(movieId, API_KEY, Variables.session_ID)?.execute()?.body()
            message = deleteRate!!.status_message!!
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            rating_moviedetails_userrating.rating = 0.0F
            notify(activity as AppCompatActivity, "Success!", message)
        }
    }


    inner class Watchlist : AsyncTask<String, String, String>() {

        val pd: ProgressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            val client = retrofitClient()?.create(APIService::class.java)
            val watchlistItems = WatchlistMovie("movie", movieId, true)
            val watchList: APIResponse.AddWatchlist? = client?.addToWatchlist(Variables.account_ID!!, API_KEY, Variables.session_ID!!, watchlistItems)?.execute()?.body()
            return watchList!!.status_message
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            if (result.equals("Success.", true)) {
                button_moviedetails_addtowatchlist.isEnabled = false
                button_moviedetails_addtowatchlist.setBackgroundColor(resources.getColor(R.color.md_grey_700))
                notify(activity as AppCompatActivity, "Success!", "Added to watchlist")
            }
        }

    }

    inner class Movie : AsyncTask<String, String, String>() {
        val pd: ProgressDialog = ProgressDialog(activity)
        var genreList: String = ""
        var movieData: APIResponse.MovieDetails? = null
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            val client = retrofitClient()?.create(APIService::class.java)
            val movieDetails: APIResponse.MovieDetails? = client?.getMovieDetails(movieId.toString(), API_KEY)?.execute()?.body()
            val iterator = movieDetails?.genres?.listIterator()

            if (iterator != null) {
                for (genre in iterator) {
                    genreList += "${genre.name}, "
                }
            }
            movieData = movieDetails

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()

            var imageBaseUrl: String = Constants.IMAGE_BASE_URL + movieData?.poster_path
            Glide.with(activity).load(imageBaseUrl).into(image_moviedetails_poster)
            text_moviedetails_title.text = movieData?.original_title
            text_moviedetails_status.text = movieData?.status
            text_moviedetails_tagline.text = movieData?.tagline
            text_moviedetails_overview.text = movieData?.overview
            text_moviedetails_homepage.text = movieData?.homepage
            text_moviedetails_genre.text = genreList.substring(0, genreList.length - 2)
            text_moviedetails_vote.text = "${movieData?.vote_average}/10"
            Log.v("MOVIE", "" + movieData?.vote_average!!.toFloat())
            rating_moviedetails_rate.rating = movieData?.vote_average!!.toFloat()
            relative_moviedetails_container.visibility = View.VISIBLE

        }
    }

    inner class GetUserMovieRating : AsyncTask<String, String, String>() {

        val pd: ProgressDialog = ProgressDialog(activity)
        var isObject = false
        var rate = Rating()
        var watchlist: Boolean = false
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            val client = retrofitClient()?.create(APIService::class.java)
            try {
                val state: APIResponse.AccountStateObject? = client?.getAccountStateObject(movieId, API_KEY, Variables.session_ID)?.execute()?.body()
                rate = state?.rated!!
                watchlist = state.watchlist
                isObject = true
                Log.v("MOVIE DETAIL", "RATING : ${rate.value}")
            } catch (e: Exception) {
                val state: APIResponse.AccountStateBoolean? = client?.getAccountStateBoolean(movieId, API_KEY, Variables.session_ID)?.execute()?.body()
                isObject = state?.rated!!
                watchlist = state.watchlist
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            if (rate is Rating) {
                rating_moviedetails_userrating.rating = rate.value.toFloat()
            } else {
                rating_moviedetails_userrating.rating = 0.0F
            }

            disableWatchlistButton(watchlist)
        }
    }

    inner class SubmitRating : AsyncTask<Double, String, String>() {
        val pd: ProgressDialog = ProgressDialog(activity)
        var message: String? = ""
        var submittedRating: Double = 0.0
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: Double?): String {
            val client = retrofitClient()?.create(APIService::class.java)
            val rating = Rating(params[0]!!)
            val submitRating: APIResponse.RateMovie? = client?.rateMovie(movieId, API_KEY, Variables.session_ID, rating)?.execute()?.body()
            message = submitRating?.status_message
            submittedRating = params[0]!!
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            rating_moviedetails_userrating.rating = submittedRating.toFloat()
            notify(activity as AppCompatActivity, "Success!", message!!)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
    }
}
