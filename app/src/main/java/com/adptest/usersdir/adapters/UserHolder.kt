package com.adptest.usersdir.adapters

import com.adptest.usersdir.Constants
import com.adptest.usersdir.R

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.RelativeLayout
import android.util.Log
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder

/**
 * Created by chevil on 23/05/19.
 */

// this class extends the classical view holder
// to update the contents dynamically

class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val NameView: TextView = itemView.findViewById<TextView>(R.id.name)
    public val ContainerView: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.container)
 
    // update the view
    fun update(name: String) {
      try {
        NameView.setText( name )
      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't update user name : " + name, e )
      }
    }
}
