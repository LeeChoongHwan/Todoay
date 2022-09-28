package com.todoay.data.profile

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("introMsg")
    var introMsg: String?,
    @SerializedName("imageUrl")
    var imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nickname)
        parcel.writeString(introMsg)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Profile> {
        override fun createFromParcel(parcel: Parcel): Profile {
            return Profile(parcel)
        }

        override fun newArray(size: Int): Array<Profile?> {
            return arrayOfNulls(size)
        }
    }
}
