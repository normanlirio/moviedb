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
import com.investagram.exam.moviedb.API.RetrofitClient
import com.investagram.exam.moviedb.Adapters.TrendingMoviesAdapter
import com.investagram.exam.moviedb.Global.*
import com.investagram.exam.moviedb.Model.Results

import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_watchlist.*
import retrofit2.Retrofit

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WatchlistFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WatchlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WatchlistFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    val list :ArrayList<Results> = ArrayList()
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
        // Inflate the layout for this fragment
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
        when(item.itemId) {
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
            val items : APIResponse.TrendingMovies? = client?.getWatchlist(ACCOUNT_ID!!, API_KEY, SESSION_ID!!)?.execute()?.body()
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
            Log.v("WATCHLIST","" + moviesAdded.itemCount + list?.size)
            recycler_watchlist_items.adapter = moviesAdded
            recycler_watchlist_items.viewTreeObserver.removeOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recycler_watchlist_items.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WatchlistFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): WatchlistFragment {
            val fragment = WatchlistFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
