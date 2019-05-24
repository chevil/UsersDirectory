package com.adptest.usersdir

/**
 * Created by chevil on 23/05/19.
 */

import com.adptest.usersdir.api.UsersService
import com.adptest.usersdir.pojo.User
import com.adptest.usersdir.pojo.Address
import com.adptest.usersdir.pojo.GeoPosition
import com.adptest.usersdir.pojo.Company
import com.adptest.usersdir.utils.ProgressDialog
import com.adptest.usersdir.adapters.UserAdapter
import com.adptest.usersdir.fragments.FragmentList
import com.adptest.usersdir.fragments.FragmentDetails
import com.adptest.usersdir.fragments.FragmentMap

import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.ObjectOutputStream
import java.io.ObjectInputStream

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.os.Bundle
import android.content.Context
import android.util.Log
import android.widget.Toast
import android.app.Dialog
import android.view.View 

// retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.OkHttpClient

// rxJava
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

// access views with their ids
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    // static data
    companion object {
      lateinit var loadingDialog : Dialog
      lateinit var usersList : List<User>
      lateinit var listFragment : FragmentList
      lateinit var detailsFragment : FragmentDetails
      lateinit var mapFragment : FragmentMap
    }

    private lateinit var disposable : Disposable

    // This function is called when we click on a user : we open the user's details fragment
    public fun onUserClicked(position: Int) {
        try {
          Log.v( Constants.LOGTAG, "User cliked : " + position )
          // check if the fragment already exists and update it or create it 
          if (FragmentDetails.isCreated)
          {
             detailsFragment.updateUser( usersList.get(position) )
          }
          else
          {
             detailsFragment = FragmentDetails( usersList.get(position) )
          }
          supportFragmentManager
          .beginTransaction()
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
          .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
          .replace(R.id.main_layout, detailsFragment as Fragment, "" )
          .addToBackStack(null)
          .commit()

        } catch ( e: Exception ) {
          Log.e( Constants.LOGTAG, "Couldn't show details fragment", e )
        }
    }

    // This function is called when we click on the location in icon on details fragment 
    // We open and display the map fragment
    public fun onLocationClicked(user: User) {
        try {
          Log.v( Constants.LOGTAG, "Location clicked : (" + user.address.geo.lat + "," + user.address.geo.lng + ")" )
          // check if the fragment already exists and update it or create it 
          if (FragmentMap.isCreated)
          {
             mapFragment.updateUser( user )
          }
          else
          {
             mapFragment = FragmentMap( user )
          }
          supportFragmentManager
          .beginTransaction()
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
          .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
          .replace(R.id.main_layout, mapFragment as Fragment, "" )
          .addToBackStack(null)
          .commit()

        } catch ( e: Exception ) {
          Log.e( Constants.LOGTAG, "Couldn't show map fragment", e )
        }
    }

    // This function is called when a sub fragment is paused
    public fun hideBack() {
        back.visibility = View.GONE
    }

    // This function is called when a sub fragment is shown
    public fun showBack() {
        back.visibility = View.VISIBLE
    }

    // Creating an empty UI
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
       back.setOnClickListener {
             supportFragmentManager.popBackStack()
             hideBack()
       }
       getUsers()
    }

    // Getting the list of users
    override fun onResume() {
       super.onResume()
    }

    fun getUsers() {

      try {

        // show loading dialog 
        loadingDialog = ProgressDialog.progressDialog(this@MainActivity)
        loadingDialog.show()

        // build a specific client to debug requests
        var client : OkHttpClient = OkHttpClient.Builder()
                              .addInterceptor(HttpLoggingInterceptor().apply {
                                 level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
                              })
                              .build();

        // build the retrofit service using a Gson converter
        // and a RxJava adapter to get result as an observable
        var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        // build the object to process the requests to the API
        var service = retrofit.create(UsersService::class.java)

        // call the service on a new thread (not the UI thread )
        // but the result should be processed on the main UI thread
        // the result is then processed by handleUsersList
        disposable = service.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleUsersList, this::handleRxError)

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_retrofit ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't get users list with retrofit", e )
      }

    }

    // This function handle the response from the retrofit call : a list of users
    private fun handleUsersList(rfUsersList: List<User>) {
 
      try {
        if ( rfUsersList.size > 0 )
        {
           Log.v( Constants.LOGTAG, "Got users : " + rfUsersList.get(0).toString() )
           // set the users list for the app
           usersList = rfUsersList
           showUsersFragment()

           // save the users list with serialization
           saveUsersList( usersList )
        }
        // free request data
        disposable.dispose()

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_display ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't show users list", e )
        return
      }

      // hiding loading dialog
      if ( loadingDialog.isShowing) loadingDialog.dismiss()
    }

    // This function is called when Rx returns an error,
    // so we use the cached data ( if any )
    private fun handleRxError(t: Throwable) {
      try {
        if ( loadingDialog.isShowing) loadingDialog.dismiss()
        Toast.makeText(this, getString( R.string.error_live ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't get live data", t )
        usersList = loadUsersList()
        if ( usersList.size >= 0 )
        {
           showUsersFragment()
        }
      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't handle rx error", e )
      }
    }
 
    // This function is called to create and show the users fragment ( called only once )
    // This is the root fragment and it is not added to the back stack
    private fun showUsersFragment() {
     try {
       listFragment = FragmentList(usersList)
       supportFragmentManager
       .beginTransaction()
       .replace(R.id.main_layout, listFragment as Fragment, getString( R.string.app_name ))
       // .addToBackStack(null)
       .commit()
      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't show users fragment", e )
      }
    }

    // This function save the users list in a file
    private fun saveUsersList(users: List<User>) {
     try {

        var directory : String = getApplicationContext().getFilesDir().toString();
        var outStream : FileOutputStream = FileOutputStream(directory + Constants.SAVED_LIST);
        var objectOutStream : ObjectOutputStream = ObjectOutputStream(outStream);
        objectOutStream.writeInt(users.size); // Save size first
        Log.v( Constants.LOGTAG, "Saving " + users.size + " users" )
        for( u in users)
        {
          objectOutStream.writeObject(u);
        }
        objectOutStream.close();
        Log.v( Constants.LOGTAG, "Saved users list" )

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_save ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't save users list", e )
      }
    }

    // This function restore the users list from the same file
    private fun loadUsersList() : List<User> {
     var users : List<User> = mutableListOf() 
     try {

        var directory : String = getApplicationContext().getFilesDir().toString();
        var inStream : FileInputStream = FileInputStream(directory + Constants.SAVED_LIST);
        var objectInStream : ObjectInputStream = ObjectInputStream(inStream);
        var count : Int = objectInStream.readInt(); // Get the number of users
        Log.v( Constants.LOGTAG, "Loading " + count + " users" )
        for (pi in 1 until count)
        {
           val storedUser = objectInStream.readObject()
           when ( storedUser ) {
             is User -> users += storedUser
             else -> Log.e( Constants.LOGTAG, "Corrupted data" );
           }
        }
        objectInStream.close();

      } catch ( e: Exception ) {
        Toast.makeText(this, getString( R.string.error_load ) , Toast.LENGTH_LONG).show()
        Log.e( Constants.LOGTAG, "Couldn't load users list", e )
      }
      return users;
    }

}
