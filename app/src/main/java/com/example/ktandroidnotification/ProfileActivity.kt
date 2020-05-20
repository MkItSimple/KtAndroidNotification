package com.example.ktandroidnotification

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ktandroidnotification.utils.hide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    companion object {
        val TAG = "ProfileActivity"
        val NODE_USERS = "users"
        val USER_KEY = "USER_KEY"
    }


    private var mAuth: FirebaseAuth? = null

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()

        recyclerview.setAdapter(adapter)

        loadUsers();

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result!!.token
                    saveToken(token)
                }
            }
    }

    private fun loadUsers() {
        progressbar.hide()
        val userList = ArrayList<User>()
        recyclerview.setLayoutManager(LinearLayoutManager(this))
        val dbUsers = FirebaseDatabase.getInstance().getReference("users")
        dbUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressbar.hide()
                if (dataSnapshot.exists()) {
                    for (dsUser in dataSnapshot.children) {
                        val user =
                            dsUser.getValue(
                                User::class.java
                            )!!
                        //userList.add(user)
                        adapter.add(UserItem(user))
                    }

                    adapter.setOnItemClickListener { item, view ->
                        val userItem = item as UserItem

                        val intent = Intent(view.context, SendNotificationActivity::class.java)
                        intent.putExtra(USER_KEY, userItem.user)
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(this@ProfileActivity, "No user found", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onStart() {
        super.onStart()
        // if not logged in redirect to login activity
        if (mAuth!!.currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun saveToken(token: String) {
        val email = mAuth!!.currentUser!!.email
        val user = User(email!!, token)
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
