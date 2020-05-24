package com.example.ktandroidnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ktandroidnotification.utils.hide
import com.example.ktandroidnotification.utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

// for reference branch
class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity"
        val CHANNEL_ID = "MainActivity"
        val CHANNEL_NAME = "Simplified Coding"
        val CHANNEL_DESC = "Simplified Coding Notifications"
    }

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();

        // https://developer.android.com/training/notify-user/build-notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            val manager =
                getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        progressbar.hide()

        buttonSignUp.setOnClickListener {
            createUser();
        }
    }

    private fun createUser(){
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        Log.d(TAG, "createUser")

        if (email.isEmpty()) {
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length < 6) {
            editTextPassword.setError("Password should be atleast 6 char long");
            editTextPassword.requestFocus();
            return;
        }

        progressbar.hide()

        mAuth?.createUserWithEmailAndPassword(email,password)
            ?.addOnCompleteListener {
                if (it.isSuccessful){
                    startProfileActivity()
                } else {
                    if (it.exception == null){
                        userLogin(email,password)
                    } else {
                        progressbar.hide()
                        toast(it.exception?.message!!)
                    }


                }
            }
    }

    private fun startProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun userLogin(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startProfileActivity()
                } else {
                    progressbar.hide()
                    Toast.makeText(
                        this@MainActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        // if logged in
        if (mAuth!!.currentUser != null) {
            startProfileActivity()
        }
    }
}
