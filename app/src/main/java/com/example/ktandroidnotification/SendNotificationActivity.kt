package com.example.ktandroidnotification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ktandroidnotification.utils.toast
import kotlinx.android.synthetic.main.activity_send_notification.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class SendNotificationActivity : AppCompatActivity() {

    companion object {
        val TAG = "SendNotification"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notification)

        val user: User? = intent.getParcelableExtra<User>(ProfileActivity.USER_KEY)

        textViewUser.setText("Sending to : " + user!!.email)

        buttonSend.setOnClickListener {
            sendNotification(user);
        }
    }

    private fun sendNotification(user: User) {

        val title = editTextTitle.text.toString().trim()
        val body = editTextBody.text.toString().trim()

        if (title.isEmpty()) {
            editTextTitle.error = "Title required"
            editTextTitle.requestFocus()
            return
        }

        if (body.isEmpty()) {
            editTextBody!!.error = "Body required"
            editTextBody!!.requestFocus()
            return
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fir-sampleapplication-23d5f.web.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api =
            retrofit.create(
                Api::class.java
            )


        val call = api.sendNotification(user.token, title, body)

        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                try {
                    toast(response.body()!!.string())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                t: Throwable
            ) {
            }
        })
    }
}
