package com.androidcamp.liveflix.utils

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar

class Helper : Application() {
    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object{
        fun toast(msg: String) {
            Toast.makeText(Helper().context, msg, LENGTH_LONG).show()
        }

        fun snack(msg: String, view: View) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
        }

        fun log(tag: String = "abd", msg: String) {
            Log.e(tag,msg)
        }
    }
}