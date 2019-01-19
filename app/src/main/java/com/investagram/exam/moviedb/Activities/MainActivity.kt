package com.investagram.exam.moviedb.Activities


import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.investagram.exam.moviedb.Fragments.Home
import com.investagram.exam.moviedb.Fragments.MovieDetails

import com.investagram.exam.moviedb.R

class MainActivity : AppCompatActivity(), Home.OnFragmentInteractionListener, MovieDetails.OnFragmentInteractionListener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            val fragment : Fragment = Home()
            ft.replace(R.id.fragment_container, fragment)
            ft.commit()
        }

    }


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
    override fun onFragmentInteraction(uri: Uri) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
