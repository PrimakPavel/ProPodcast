package com.pavelprymak.propodcast.utils;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ShareCompat;

import com.pavelprymak.propodcast.R;

public class ShareUtil {
    public static void shareData(Activity activity, String link) {
        if (activity != null)
            activity.startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(activity)
                    .setType("text/plain")
                    .setText(link)
                    .getIntent(), activity.getString(R.string.share_label)));
    }
}
