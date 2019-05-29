package com.adptest.usersdir.fragments

/**
 * Created by chevil on 23/05/19.
 */

import com.adptest.usersdir.R
import com.adptest.usersdir.Constants
import com.adptest.usersdir.MainActivity
import com.adptest.usersdir.pojo.User
 
import android.os.Bundle
import android.util.Log
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView

class FragmentDetails(user: User) : Fragment() {

    lateinit var mainView : View 
    lateinit var mActivity : MainActivity 
    var mUser : User 

    init {
      mUser = user
    }

    companion object {
      var isCreated : Boolean = false 
    }

    // Update the user once it has been created ( views are existing then )
    public fun updateUser( user: User ) {
        mUser = user
        mActivity.showBack()
    }

    // this is called everytime the fragment is shown
    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
     try {
        // Inflate the layout for this fragment
        Log.v( Constants.LOGTAG, "Details fragment created" )
        mActivity = this.getActivity() as MainActivity
        mainView = inflater.inflate(R.layout.fragment_details, container, false)
        mainView.findViewById<TextView>(R.id.named)?.setText( mUser.name )
        mainView.findViewById<TextView>(R.id.usernamed)?.setText( " : " + mUser.username )
        mainView.findViewById<TextView>(R.id.emaild)?.setText( " : " + mUser.email )
        mainView.findViewById<TextView>(R.id.addressd)?.setText( " : " + mUser.address.street + "\n   " + mUser.address.suite + "\n   " + mUser.address.zipcode + " - " + mUser.address.city)
        mainView.findViewById<TextView>(R.id.phoned)?.setText( " : " + mUser.phone )
        mainView.findViewById<TextView>(R.id.websited)?.setText( " : " + mUser.website )
        mainView.findViewById<TextView>(R.id.companyd)?.setText( " : " + mUser.company.name )
        mainView.findViewById<ImageView>(R.id.locicon)?.setOnClickListener( {
           mActivity.onLocationClicked( mUser )
        } )
        isCreated = true
      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't create details view", e )
      }
      var activity : MainActivity = this.getActivity() as MainActivity
      activity.showBack()
      return mainView

    }
    
    override public fun onPause() {
        super.onPause()
        var activity : MainActivity = this.getActivity() as MainActivity
        activity.hideBack() 
    }
}
