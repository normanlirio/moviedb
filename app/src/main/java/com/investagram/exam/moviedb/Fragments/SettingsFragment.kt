package com.investagram.exam.moviedb.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.investagram.exam.moviedb.Activities.LoginActivity
import com.investagram.exam.moviedb.Global.Constants.GENERIC_TITLE_POPUP
import com.investagram.exam.moviedb.Global.Constants.REQUEST_LOGIN
import com.investagram.exam.moviedb.Global.Variables
import com.investagram.exam.moviedb.Global.askToLoginPopup
import com.investagram.exam.moviedb.Global.setCustomActionbar
import com.investagram.exam.moviedb.Global.switchFragment
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCustomActionbar(activity as AppCompatActivity, "Settings")
        bottom_navigation.setOnNavigationItemSelectedListener(this)

        text_settings_login.setOnClickListener({
            if (Variables.isLoggedIn) {
                Variables.isLoggedIn = false
                text_settings_login.text = "LOGIN"
                text_settings_username.visibility = View.GONE
            } else {
                val intent = Intent(activity, LoginActivity::class.java)
                activity!!.startActivityForResult(intent, REQUEST_LOGIN)
            }
        })
        if (Variables.isLoggedIn) {
            text_settings_login.text = "LOGOUT"
            text_settings_username.text = "Hello, ${Variables.login_username}"
            text_settings_username.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            if(resultCode == REQUEST_LOGIN) {
                text_settings_login.text = "LOGOUT"
                text_settings_username.text = "Hello, $Variables.login_username"
                text_settings_username.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(activity, "User cancelled", Toast.LENGTH_LONG)
        }
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
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
