package com.pavelprymak.propodcast.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {
    fun hideKeyboard(activity: Activity?) {
        try {
            activity?.let {
                val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                //Find the currently focused view, so we can grab the correct window token from it.
                var view = activity.currentFocus
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = View(activity)
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
            //ignore
        }

    }

    fun hideKeyboardFrom(context: Context?, view: View) {
        try {
            context?.let {
                val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (ignore: Exception) {
        }

    }

    fun showInputMethod(context: Context?, view: View) {
        try {
            context?.let {
                val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, 0)
            }
        } catch (ignore: Exception) {
        }

    }
}
