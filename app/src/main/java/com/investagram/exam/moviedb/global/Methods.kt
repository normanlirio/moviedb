package com.investagram.exam.moviedb.global

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.investagram.exam.moviedb.api.RetrofitClient
import com.investagram.exam.moviedb.activities.LoginActivity
import com.investagram.exam.moviedb.global.Constants.BASE_URL
import com.investagram.exam.moviedb.R
import retrofit2.Retrofit

fun switchFragment(activity: Context?, fragment: Fragment) {
    val ft = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
    ft.replace(R.id.fragment_container, fragment).addToBackStack(null)
    ft.commit()
}

fun setCustomActionbar(activity: Activity, page: String) {
    val actionBar = (activity as AppCompatActivity).supportActionBar
    actionBar!!.setCustomView(R.layout.custom_actionbar)
    val customActionbarItems = actionBar.customView
    val ivBack = customActionbarItems.findViewById<ImageView>(R.id.img_actionbar_back)
    ivBack.visibility = View.GONE
    if (page.equals("moviedetails", true)) {
        ivBack.visibility = View.VISIBLE
    } else {
        ivBack.visibility = View.GONE
    }
    ivBack.setOnClickListener {
        activity.supportFragmentManager.popBackStack()
    }


}

fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo: NetworkInfo?
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

    btnContinue.setOnClickListener {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivityForResult(intent, 10)
        alertDialog.dismiss()
    }
    btnCancel.setOnClickListener {
        alertDialog.dismiss()
    }


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
    btnContinue.setOnClickListener {
        alertDialog.dismiss()
    }
    btnCancel.setOnClickListener {
        alertDialog.dismiss()
    }
}

fun retrofitClient(): Retrofit? {
    return RetrofitClient.getClient(BASE_URL)
}