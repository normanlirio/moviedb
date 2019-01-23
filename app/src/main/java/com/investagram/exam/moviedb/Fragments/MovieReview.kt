package com.investagram.exam.moviedb.Fragments

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
import com.investagram.exam.moviedb.API.APIResponse
import com.investagram.exam.moviedb.API.APIService
import com.investagram.exam.moviedb.Adapters.MovieReviewAdapter
import com.investagram.exam.moviedb.Global.Constants.API_KEY
import com.investagram.exam.moviedb.Global.retrofitClient
import com.investagram.exam.moviedb.Global.setCustomActionbar
import com.investagram.exam.moviedb.Model.ReviewResults
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.fragment_movie_review.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MovieReview.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MovieReview.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieReview : Fragment() {

    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
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
        val pd: ProgressDialog = ProgressDialog(activity)
        val list: ArrayList<ReviewResults> = ArrayList()
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
                val adapter: MovieReviewAdapter = MovieReviewAdapter(activity, list)
                recycler_moviereview_items.adapter = adapter
            } else {
                text_moviereview_noreview.visibility = View.VISIBLE
            }

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
         * @return A new instance of fragment MovieReview.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): MovieReview {
            val fragment = MovieReview()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
