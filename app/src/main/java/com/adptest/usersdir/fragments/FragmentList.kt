package com.adptest.usersdir.fragments

/**
 * Created by chevil on 23/05/19.
 */

import com.adptest.usersdir.R
import com.adptest.usersdir.Constants
import com.adptest.usersdir.MainActivity
import com.adptest.usersdir.pojo.User
import com.adptest.usersdir.adapters.UserAdapter
 
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FragmentList(val users: List<User>) : Fragment() {
 
    lateinit var mainView : View
    lateinit var usersView : RecyclerView

    companion object {
      var isCreated : Boolean = false
    }
 
    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
      try {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_list, container, false)
        usersView = mainView.findViewById<RecyclerView>(R.id.usersView)
        usersView.layoutManager = LinearLayoutManager(this.getActivity())
        // create an adapter with the constructor list
        var activity : MainActivity = this.getActivity() as MainActivity
        usersView.adapter = UserAdapter( users) { position -> activity.onUserClicked( position ) }
        isCreated = true
      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't create users view", e )
      }
      return mainView
    }
}
