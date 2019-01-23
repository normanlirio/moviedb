package com.investagram.exam.moviedb.fragments

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.investagram.exam.moviedb.api.APIResponse
import com.investagram.exam.moviedb.api.APIService
import com.investagram.exam.moviedb.adapters.MovieReviewAdapter
import com.investagram.exam.moviedb.global.Constants.API_KEY
import com.investagram.exam.moviedb.global.retrofitClient
import com.investagram.exam.moviedb.global.setCustomActionbar
import com.investagram.exam.moviedb.model.ReviewResults
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.fragment_movie_review.*

class MovieReview : Fragment() {

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
        return inflater.inflate(R.layout.fragment_movie_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomActionbar(activity as AppCompatActivity, "moviedetails")
        val bundle = this.arguments
        if (bundle != null) {
            movieId = bundle.getInt("movieid")
            Log.v("MOVIE REVIEW", "ID: $movieId")
            LoadReviews().execute()
        }
        recycler_moviereview_items.layoutManager = LinearLayoutManager(activity)

    }

    inner class LoadReviews : AsyncTask<String, String, String>() {
        private val pd: ProgressDialog = ProgressDialog(activity)
        private val list: ArrayList<ReviewResults> = ArrayList()
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(true)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            val client = retrofitClient()?.create(APIService::class.java)
            val review: APIResponse.MovieReview? = client?.getMovieReview(movieId, API_KEY)?.execute()?.body()

            Log.v("MOVIE REVIEW", "SIZE: ${review?.results?.size}")
            val iterator = review?.results?.listIterator()

            if (iterator != null) {
                for (review in iterator) {
                    list.add(review)
                }
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pd.dismiss()
            relative_moviereview_container.visibility = View.VISIBLE
            if (list.size > 0) {
                text_moviereview_noreview.visibility = View.GONE
                val adapter = MovieReviewAdapter(list)
                recycler_moviereview_items.adapter = adapter
            } else {
                text_moviereview_noreview.visibility = View.VISIBLE
            }

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