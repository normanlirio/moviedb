package com.investagram.exam.moviedb.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import com.investagram.exam.moviedb.API.APIService
import com.investagram.exam.moviedb.API.RetrofitClient
import com.investagram.exam.moviedb.Adapters.TrendingMoviesAdapter
import com.investagram.exam.moviedb.Beans.Results
import com.investagram.exam.moviedb.Global.API_KEY
import com.investagram.exam.moviedb.Model.APIResponse

import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Retrofit

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Home.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var newList : ArrayList<Results>? = ArrayList()
    private var searchedList: ArrayList<Results>? = ArrayList()
    private var mListener: OnFragmentInteractionListener? = null
    private var isSearching : Boolean = false

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trendingMovies = TrendingMovies()
        trendingMovies.execute()
        recycler_home_items.layoutManager = GridLayoutManager(activity,2)
        button_home_search.setOnClickListener(View.OnClickListener {
            if(isSearching) {
                searchedList?.clear()
                isSearching = false
                //Reset Search bar
                button_home_search.setImageDrawable(resources.getDrawable(R.drawable.ic_search))
                edit_home_search.setText("")
                val trendingMovies : TrendingMoviesAdapter = TrendingMoviesAdapter(activity, newList)
                trendingMovies.notifyDataSetChanged()
                recycler_home_items.adapter = trendingMovies
            } else {
                if(!edit_home_search.text.toString().equals("", true)) {
                    isSearching = true
                    button_home_search.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black))
                    val searchMovie = SearchMovies()
                    searchMovie.execute(edit_home_search.text.toString())
                } else {
                    Toast.makeText(activity, "Please enter a keyword", Toast.LENGTH_SHORT)
                }

            }

        })
    }

    inner class SearchMovies : AsyncTask<String,String,String>() {
        val pd: ProgressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }
        override fun doInBackground(vararg params: String): String {
            Log.v("HOME", params[0])
            val retrofit: Retrofit? = RetrofitClient.getClient("https://api.themoviedb.org/")
            val client = retrofit?.create(APIService::class.java)
            val searchMovies : APIResponse.SearchMovies? = client?.searchMovie(API_KEY, params[0])?.execute()?.body()
            val iterator = searchMovies?.results?.listIterator()
            if (iterator != null) {
                for(movie in iterator) {
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
            Log.v("HOME","" + searchedMovies.itemCount + searchedList?.size)
            recycler_home_items.adapter = searchedMovies
            recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        }

    }
    inner class TrendingMovies : AsyncTask<String, String, String>() {
        val pd: ProgressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            val retrofit: Retrofit? = RetrofitClient.getClient("https://api.themoviedb.org/")
            val client = retrofit?.create(APIService::class.java)
            val trendingList: APIResponse.TrendingMovies? = client?.getTrendingMovies(API_KEY)?.execute()?.body()
            val iterator = trendingList?.results?.listIterator()
            if (iterator != null) {
                for(movie in iterator) {
                    newList?.add(movie)
                }
            }

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            val trendingMovies : TrendingMoviesAdapter = TrendingMoviesAdapter(activity, newList)
            recycler_home_items.adapter = trendingMovies
            recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recycler_home_items.viewTreeObserver.removeOnGlobalLayoutListener(this)
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
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Home {
            val fragment = Home()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
