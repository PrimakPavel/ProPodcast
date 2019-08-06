package com.pavelprymak.propodcast.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.model.network.Constants;

import retrofit2.HttpException;

public class ApiErrorHandler {
    public static void handleError(@NonNull Context context, @NonNull HttpException httpException) {
        switch (httpException.code()) {
            case Constants.ApiErrorCodes.WRONG_API_KEY: {
                Toast.makeText(context, R.string.api_wrong_api_key, Toast.LENGTH_LONG).show();
                break;
            }
            case Constants.ApiErrorCodes.WRONG_ITEM_ID: {
                Toast.makeText(context, R.string.api_item_id_error, Toast.LENGTH_LONG).show();
                break;
            }
            case Constants.ApiErrorCodes.FREE_PLAN_QUOTA_LIMIT: {
                Toast.makeText(context, R.string.api_quota_limit, Toast.LENGTH_LONG).show();
                break;
            }
            case Constants.ApiErrorCodes.SOMETHING_WRONG: {
                Toast.makeText(context, R.string.api_something_wrong, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
}
