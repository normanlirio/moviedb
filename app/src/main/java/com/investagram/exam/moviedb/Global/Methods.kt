package com.investagram.exam.moviedb.Global

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.support.design.R.id.visible
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * Created by Lirio on 1/20/2019.
 */
fun switchFragment (activity: Context?, fragment: Fragment)  {
    val ft = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
    ft.replace(R.id.fragment_container, fragment).addToBackStack(null)
    ft.commit()
}

fun setCustomActionbar(activity: Activity, page :String) {
    val actionBar = (activity as AppCompatActivity).supportActionBar
    actionBar!!.setCustomView(R.layout.custom_actionbar)
    val customActionbarItems = actionBar.customView
    val ivBack = customActionbarItems.findViewById<ImageView>(R.id.img_actionbar_back)
    ivBack.visibility = View.GONE
    if(page.equals("moviedetails", true)) {
        ivBack.visibility = View.VISIBLE
    }
    ivBack.setOnClickListener(View.OnClickListener {
        (activity as AppCompatActivity).supportFragmentManager.popBackStack()
    })
}