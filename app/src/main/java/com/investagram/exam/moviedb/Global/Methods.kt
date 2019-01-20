package com.investagram.exam.moviedb.Global

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.opengl.Visibility
import android.support.design.R.id.visible
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.investagram.exam.moviedb.API.RetrofitClient
import com.investagram.exam.moviedb.Activities.LoginActivity
import com.investagram.exam.moviedb.R
import kotlinx.android.synthetic.main.activity_main.view.*
import retrofit2.Retrofit

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
    val tvLogin = customActionbarItems.findViewById<TextView>(R.id.text_actionbar_cancel)
    ivBack.visibility = View.GONE
    if(page.equals("moviedetails", true)) {
        ivBack.visibility = View.VISIBLE
    } else {
        ivBack.visibility = View.GONE
    }
    ivBack.setOnClickListener(View.OnClickListener {
        (activity as AppCompatActivity).supportFragmentManager.popBackStack()
    })

    tvLogin.visibility = View.GONE
}

fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var activeNetworkInfo: NetworkInfo? = null
    activeNetworkInfo = cm.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
}

fun askToLoginPopup(activity: Activity, title: String, message: String) {
    val dialogBuilder = AlertDialog.Builder(activity)
    val inflater = activity.layoutInflater
    val dialogView = inflater.inflate(R.layout.popup_layout, null)
    dialogBuilder.setView(dialogView)
    val tvHeader = dialogView.findViewById<TextView>(R.id.text_popup_title)
    val tvBody = dialogView.findViewById<TextView>(R.id.text_popup_body)
    val btnContinue = dialogView.findViewById<Button>(R.id.button_popup_continue)
    val btnCancel = dialogView.findViewById<Button>(R.id.button_popup_cancel)

    tvHeader.text = title
    tvBody.text = message

    val alertDialog = dialogBuilder.create()
    alertDialog.show()

    btnContinue.setOnClickListener(View.OnClickListener {
        val intent: Intent = Intent(activity, LoginActivity::class.java)
        activity.startActivityForResult(intent, 10)
        alertDialog.dismiss()
    })
    btnCancel.setOnClickListener(View.OnClickListener {
        alertDialog.dismiss()
    })


}

fun notify(activity: Activity, title: String, message: String) {
    val dialogBuilder = AlertDialog.Builder(activity)
    val inflater = activity.layoutInflater
    val dialogView = inflater.inflate(R.layout.popup_layout, null)
    dialogBuilder.setView(dialogView)
    val tvHeader = dialogView.findViewById<TextView>(R.id.text_popup_title)
    val tvBody = dialogView.findViewById<TextView>(R.id.text_popup_body)
    val btnContinue = dialogView.findViewById<Button>(R.id.button_popup_continue)
    val btnCancel = dialogView.findViewById<Button>(R.id.button_popup_cancel)

    tvHeader.text = title
    tvBody.text = message

    val alertDialog = dialogBuilder.create()
    alertDialog.show()
    btnCancel.visibility = View.GONE
    btnContinue.text = "CLOSE"
    btnContinue.setOnClickListener(View.OnClickListener {

        alertDialog.dismiss()
    })
    btnCancel.setOnClickListener(View.OnClickListener {
        alertDialog.dismiss()
    })

}

fun retrofitClient() : Retrofit? {
    return RetrofitClient.getClient("https://api.themoviedb.org/")
}