package com.pavelprymak.propodcast.utils

import android.content.Context
import android.widget.Toast
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.network.ApiErrorCodes
import retrofit2.HttpException

object ApiErrorHandler {
    fun handleError(context: Context, httpException: HttpException) {
        when (httpException.code()) {
            ApiErrorCodes.WRONG_API_KEY -> {
                Toast.makeText(context, R.string.api_wrong_api_key, Toast.LENGTH_LONG).show()
            }
            ApiErrorCodes.WRONG_ITEM_ID -> {
                Toast.makeText(context, R.string.api_item_id_error, Toast.LENGTH_LONG).show()
            }
            ApiErrorCodes.FREE_PLAN_QUOTA_LIMIT -> {
                Toast.makeText(context, R.string.api_quota_limit, Toast.LENGTH_LONG).show()
            }
            ApiErrorCodes.SOMETHING_WRONG -> {
                Toast.makeText(context, R.string.api_something_wrong, Toast.LENGTH_LONG).show()
            }
        }
    }
}
