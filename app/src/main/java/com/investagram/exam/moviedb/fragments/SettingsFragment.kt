package com.investagram.exam.moviedb.fragments

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
import com.investagram.exam.moviedb.activities.LoginActivity
import com.investagram.exam.moviedb.global.*
import com.investagram.exam.moviedb.global.Constants.GENERIC_TITLE_POPUP
import com.investagram.exam.moviedb.global.Constants.REQUEST_LOGIN
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCustomActionbar(activity as AppCompatActivity, "Settings")
        bottom_navigation.setOnNavigationItemSelectedListener(this)

        text_settings_login.setOnClickListener {
            if (Variables.isLoggedIn) {
                Variables.isLoggedIn = false
                text_settings_login.text = Constants.LOGIN
                text_settings_username.visibility = View.GONE
            } else {
                val intent = Intent(activity, LoginActivity::class.java)
                activity!!.startActivityForResult(intent, REQUEST_LOGIN)
            }
        }
        if (Variables.isLoggedIn) {
            text_settings_login.text = Constants.LOGOUT
            text_settings_username.text = "Hello, ${Variables.login_username}"
            text_settings_username.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            if(resultCode == REQUEST_LOGIN) {
                text_settings_login.text = Constants.LOGOUT
                text_settings_username.text = "Hello, ${Variables.login_username}"
                text_settings_username.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(activity, "User cancelled", Toast.LENGTH_LONG).show()
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
