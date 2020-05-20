package com.example.ktandroidnotification

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId


class ProfileActivity : AppCompatActivity() {

    val NODE_USERS = "users"
    private var mAuth: FirebaseAuth? = null

    private val recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result!!.token
                    saveToken(token)
                }
            }
    }

    private fun saveToken(token: String) {
        val email = mAuth!!.currentUser!!.email
        val user =
            User(email!!, token)
        val dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS)
        dbUsers.child(mAuth!!.currentUser!!.uid)
            .setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Token Saved", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}
