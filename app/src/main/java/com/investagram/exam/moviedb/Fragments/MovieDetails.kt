package com.investagram.exam.moviedb.Fragments

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.investagram.exam.moviedb.API.APIService
import com.investagram.exam.moviedb.API.RetrofitClient
import com.investagram.exam.moviedb.Global.API_KEY
import com.investagram.exam.moviedb.Global.setCustomActionbar
import com.investagram.exam.moviedb.Global.switchFragment
import com.investagram.exam.moviedb.Model.APIResponse

import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_movie_details.*
import retrofit2.Retrofit

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MovieDetails.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MovieDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetails : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {


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

        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        setCustomActionbar(activity as AppCompatActivity, "moviedetails")
        val bundle: Bundle? = this.arguments
        if (bundle != null) {
            movieId = bundle.getInt("id")
        }
        relative_moviedetails_container.visibility = View.GONE
        val movie: Movie = Movie()
        movie.execute()

        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> switchFragment(context, Home())
            R.id.action_wathchlist -> switchFragment(context, MovieDetails())
        }
        return true
    }
    inner class Movie : AsyncTask<String, String, String>() {
        val pd: ProgressDialog = ProgressDialog(activity)
        var genreList : String = ""
        var movieData: APIResponse.MovieDetails? = null
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setMessage("Loading...")
            pd.setCancelable(false)
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            val retrofit: Retrofit? = RetrofitClient.getClient("https://api.themoviedb.org/")
            val client = retrofit?.create(APIService::class.java)
            val movieDetails: APIResponse.MovieDetails? = client?.getMovieDetails(movieId.toString(),API_KEY)?.execute()?.body()
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

            var imageBaseUrl : String = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + movieData?.poster_path
            Glide.with(activity).load(imageBaseUrl).into(image_moviedetails_poster)
            text_moviedetails_title.text = movieData?.original_title
            text_moviedetails_popularity.text = movieData?.popularity.toString()
            text_moviedetails_tagline.text = movieData?.tagline
            text_moviedetails_overview.text = movieData?.overview
            text_moviedetails_homepage.text = movieData?.homepage
            text_moviedetails_genre.text = genreList.substring(0, genreList.length-2 )
            relative_moviedetails_container.visibility = View.VISIBLE

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
         * @return A new instance of fragment MovieDetails.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): MovieDetails {
            val fragment = MovieDetails()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
