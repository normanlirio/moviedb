package com.investagram.exam.moviedb.Fragments

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
import com.investagram.exam.moviedb.API.APIResponse
import com.investagram.exam.moviedb.API.APIService
import com.investagram.exam.moviedb.Adapters.TrendingMoviesAdapter
import com.investagram.exam.moviedb.Global.Constants.API_KEY
import com.investagram.exam.moviedb.Global.Variables
import com.investagram.exam.moviedb.Global.retrofitClient
import com.investagram.exam.moviedb.Global.setCustomActionbar
import com.investagram.exam.moviedb.Global.switchFragment
import com.investagram.exam.moviedb.Model.Results
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_watchlist.*

class WatchlistFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var mParam1: String? = null
    private var mParam2: String? = null
    val list: ArrayList<Results> = ArrayList()
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
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomActionbar(activity as AppCompatActivity, "Watchlist")
        recycler_watchlist_items.layoutManager = GridLayoutManager(activity, 2)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        val loadWatchlist = LoadWatchlist()
        loadWatchlist.execute()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> switchFragment(context, Home())
            R.id.action_wathchlist -> switchFragment(context, WatchlistFragment())
            R.id.action_settings -> switchFragment(context, SettingsFragment())
        }
        return true
    }


    inner class LoadWatchlist : AsyncTask<String, String, String>() {
        val pd: ProgressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {

            val client = retrofitClient()?.create(APIService::class.java)
            val items: APIResponse.TrendingMovies? = client?.getWatchlist(Variables.account_ID!!, API_KEY, Variables.session_ID!!)?.execute()?.body()
            val iterator = items?.results?.listIterator()

            if (iterator != null) {
                for (watchlist in iterator) {
                    list.add(watchlist)
                }
            }
            return ""
        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            val moviesAdded = TrendingMoviesAdapter(activity, list)
            moviesAdded.notifyDataSetChanged()
            Log.v("WATCHLIST", "" + moviesAdded.itemCount + list?.size)
            recycler_watchlist_items.adapter = moviesAdded
            recycler_watchlist_items.viewTreeObserver.removeOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recycler_watchlist_items.viewTreeObserver.removeOnGlobalLayoutListener(this)
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
        list.clear()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
    }
}
