package com.example.ktandroidnotification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class User(
    val email: String,
    val token: String
): Parcelable {
    constructor() : this("", "")
}
