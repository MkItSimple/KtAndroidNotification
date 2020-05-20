package com.example.ktandroidnotification

import java.io.Serializable

class User(
    val email: String,
    val token: String
): Serializable {
    constructor() : this("", "")
}