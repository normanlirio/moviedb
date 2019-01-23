package com.investagram.exam.moviedb.fragments

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.investagram.exam.moviedb.api.APIResponse
import com.investagram.exam.moviedb.api.APIService
import com.investagram.exam.moviedb.adapters.TrendingMoviesAdapter
import com.investagram.exam.moviedb.global.*
import com.investagram.exam.moviedb.global.Constants.API_KEY
import com.investagram.exam.moviedb.global.Constants.GENERIC_TITLE_POPUP
import com.investagram.exam.moviedb.model.Results
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_home.*

class Home : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {


    private var mParam1: String? = null
    private var mParam2: String? = null
    private var newList: ArrayList<Results>? = ArrayList()
    private var searchedList: ArrayList<Results>? = ArrayList()
    private var mListener: OnFragmentInteractionListener? = null
    private var isSearching: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        setCustomActionbar(activity as AppCompatActivity, "")
        if (isNetworkAvailable(activity as AppCompatActivity)) {
            val trendingMovies = TrendingMovies()
            trendingMovies.execute()
        } else {
            notify(activity as AppCompatActivity, GENERIC_TITLE_POPUP, "This app requires an internet connection")
        }

        recycler_home_items.layoutManager = GridLayoutManager(activity, 2)
        button_home_search.setOnClickListener {
            if (isNetworkAvailable(activity as AppCompatActivity)) {
                if (isSearching) {
                    searchedList?.clear()
                    isSearching = false
                    //Reset Search bar
                    button_home_search.setImageDrawable(resources.getDrawable(R.drawable.ic_search))
                    edit_home_search.setText("")
                    val trendingMovies: TrendingMoviesAdapter = TrendingMoviesAdapter(activity, newList)
                    trendingMovies.notifyDataSetChanged()
                    recycler_home_items.adapter = trendingMovies
                } else {
                    if (!edit_home_search.text.toString().equals("", true)) {
                        isSearching = true
                        button_home_search.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black))
                        val searchMovie = SearchMovies()
                        searchMovie.execute(edit_home_search.text.toString())
                    } else {
                        Toast.makeText(activity, "Please enter a keyword", Toast.LENGTH_SHORT)
                    }

                }
            } else {
                notify(activity as AppCompatActivity, "OOPS!", "This app requires an internet connection")
            }

        }

        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> switchFragment(context, Home())
            R.id.action_wathchlist ->
                if (Variables.isLoggedIn) {
                    switchFragment(context, WatchlistFragment())
                } else {
                    askToLoginPopup(activity as AppCompatActivity, GENERIC_TITLE_POPUP, "Please login to see you watchlist")
                }
            R.id.action_settings -> switchFragment(context, SettingsFragment())

        }
        return true
    }

    inner class SearchMovies : AsyncTask<String, String, String>() {
        private val pd: ProgressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String): String {
            Log.v("HOME", params[0])
            val client = retrofitClient()?.create(APIService::class.java)
            val searchMovies: APIResponse.SearchMovies? = client?.searchMovie(API_KEY, params[0])?.execute()?.body()
            val iterator = searchMovies?.results?.listIterator()
            if (iterator != null) {
                for (movie in iterator) {
                    searchedList?.add(movie)
                }
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            val searchedMovies = TrendingMoviesAdapter(activity, searchedList)
            searchedMovies.notifyDataSetChanged()
            Log.v("HOME", "" + searchedMovies.itemCount + searchedList?.size)
            recycler_home_items.adapter = searchedMovies
            recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        }

    }

    inner class TrendingMovies : AsyncTask<String, String, String>() {
        private val pd: ProgressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(true)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {

            val client = retrofitClient()?.create(APIService::class.java)
            val trendingList: APIResponse.TrendingMovies? = client?.getTrendingMovies(API_KEY)?.execute()?.body()
            val iterator = trendingList?.results?.listIterator()
            if (newList?.size!! > 0) {
                newList?.clear()
            }

            if (iterator != null) {
                for (movie in iterator) {
                    newList?.add(movie)
                }
            }

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            val trendingMovies = TrendingMoviesAdapter(activity, newList)
            recycler_home_items.adapter = trendingMovies
            recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })


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
