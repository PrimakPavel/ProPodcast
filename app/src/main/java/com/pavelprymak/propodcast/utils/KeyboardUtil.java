package com.pavelprymak.propodcast.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignore) {
        }
    }

    public static void showInputMethod(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        } catch (Exception ignore) {
        }
    }
}
