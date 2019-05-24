package com.adptest.usersdir.adapters

import com.adptest.usersdir.R

import com.adptest.usersdir.pojo.User
import com.adptest.usersdir.pojo.Address
import com.adptest.usersdir.pojo.GeoPosition
import com.adptest.usersdir.pojo.Company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder

/**
 * Created by chevil on 23/05/19.
 */

// this is the adapter for the list of users
class UserAdapter( private val users : List<User>, val clickListener: (Int) -> Unit ) : RecyclerView.Adapter<UserHolder>() {
 
    override fun getItemCount(): Int {
        return users.size;
    }
 
    // update the view when necessary
    override public fun onBindViewHolder(holder: UserHolder, position: Int) : Unit {
        // Setting the clickListener sent by the constructor
        holder.ContainerView.setOnClickListener { clickListener(position) }
        var name = users.get(position).name
        holder.update(name)
    }
 
    override public fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        var userItem = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserHolder(userItem)
    }
}
