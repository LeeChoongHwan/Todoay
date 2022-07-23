package com.todoay.api.util

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ValidErrorResponse(
    timestamp: String,
    status: Int,
    error: String,
    code: String,
    path: String,
    @SerializedName("details")
    val details: List<ValidDetail>
) : ErrorResponse(timestamp, status, error, code, path) {
    override fun toString(): String {
        return super.toString() + "\n" +
                "ErrorResponse.Details{(${details.toString()}"
    }
}