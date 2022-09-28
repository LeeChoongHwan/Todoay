package com.todoay.api.domain.profile.dto.request

import com.google.gson.annotations.SerializedName
import com.todoay.data.profile.Profile
import java.io.File

data class ModifyProfileRequest(
//    @SerializedName("image")
//    val image : File?,
    @SerializedName("profile")
    val profile : Profile
)
