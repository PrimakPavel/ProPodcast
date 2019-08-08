package com.pavelprymak.propodcast.utils

import android.app.Activity
import android.content.Intent

import androidx.core.app.ShareCompat

import com.pavelprymak.propodcast.R

object ShareUtil {
    fun shareData(activity: Activity?, link: String?) {
        link?.let {
            activity?.startActivity(
                Intent.createChooser(
                    ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setText(link)
                        .intent, activity.getString(R.string.share_label)
                )
            )
        }

    }
}
