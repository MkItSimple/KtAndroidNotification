package com.example.ktandroidnotification

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recyclerview_users.view.*

class UserItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewEmail.setText(user.email)
        //viewHolder.itemView.username_textview_new_message.text = user.username
        //Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.recyclerview_users
    }
}