package com.adptest.usersdir.fragments

/**
 * Created by chevil on 24/05/19.
 */

import java.io.File

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
import android.widget.Button;
import android.graphics.PointF

// here SDK
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.*;
import com.here.android.mpa.common.*;

class FragmentMap(user: User) : Fragment() {

    lateinit var mainView : View 
    var mUser : User 
    var mZoomLevel : Double = 2.0
    lateinit var mMapFragment : SupportMapFragment
    lateinit var mMap : com.here.android.mpa.mapping.Map
    lateinit var mMarker : MapMarker
    lateinit var mCenter : GeoCoordinate
    lateinit var mActivity : MainActivity

    init {
      mUser = user
    }

    companion object {
      var isCreated : Boolean = false 
    }

    // Update the user once it has been created ( views are existing then )
    public fun updateUser( user: User ) {
        mUser = user
        // set center
        mCenter = GeoCoordinate( mUser.address.geo.lat, mUser.address.geo.lng );
        mMap.setCenter(mCenter, 
                       com.here.android.mpa.mapping.Map.Animation.LINEAR, 
                       mZoomLevel, 
                       com.here.android.mpa.mapping.Map.MOVE_PRESERVE_ORIENTATION, 
                       com.here.android.mpa.mapping.Map.MOVE_PRESERVE_TILT );
        mMap.setZoomLevel(mZoomLevel) 

        // create the marker on the map
        mMarker.setCoordinate( mCenter );
        mMarker.setTitle( mUser.name );
        var hAddress : String = mUser.address.street + "\n" + mUser.address.suite + "\n" + 
                               mUser.address.zipcode + " - " + mUser.address.city
        mMarker.setDescription( hAddress );
        mMarker.showInfoBubble()
        mActivity.showBack()
    }

    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
      try {
        // Inflate the layout for this fragment
        Log.v( Constants.LOGTAG, "Map fragment created" )
        mActivity = this.getActivity() as MainActivity
        mainView = inflater.inflate(R.layout.fragment_map, container, false)
        // Search for the Map Fragment
        mMapFragment = getChildFragmentManager().findFragmentById(R.id.mapfragment) as SupportMapFragment
        // initialize the Map Fragment and
        // retrieve the map that is associated to the fragment
        mMapFragment.init( object: OnEngineInitListener {
          override fun onEngineInitializationCompleted(error: OnEngineInitListener.Error) { 
            if (error == OnEngineInitListener.Error.NONE) {
             try {
               // now the map is ready to be used
               mMap = mMapFragment.getMap();

               // set center
               mCenter = GeoCoordinate( mUser.address.geo.lat, mUser.address.geo.lng );
               mMap.setCenter(mCenter, 
                              com.here.android.mpa.mapping.Map.Animation.LINEAR, 
                              mZoomLevel, 
                              com.here.android.mpa.mapping.Map.MOVE_PRESERVE_ORIENTATION, 
                              com.here.android.mpa.mapping.Map.MOVE_PRESERVE_TILT );

               // create image for the marker
               var iLoc : Image = Image();
               iLoc.setImageResource(R.drawable.locicont);

               // create the marker on the map
               mMarker = MapMarker(mCenter, iLoc);
               mMarker.setTitle( mUser.name );
               var hAddress : String = mUser.address.street + "\n" + mUser.address.suite + "\n" + 
                                     mUser.address.zipcode + " - " + mUser.address.city
               mMarker.setDescription( hAddress );
               mMarker.setZIndex( 2 );
               mMarker.setAnchorPoint( PointF( 24.0f, 48.0f ) );
               mMap.addMapObject(mMarker);
               mMarker.showInfoBubble()

             } catch ( e: Exception ) {
               Log.e( Constants.LOGTAG, "Couldn't create marker", e );
             }

            } else {
              Log.e( Constants.LOGTAG, "Couldn't initialize map : " + error.getDetails()  );
            }
          }
        })

      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't create map view", e )
      }
      mActivity.showBack()

      return mainView
    }
    
}
