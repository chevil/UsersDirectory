package com.adptest.usersdir.pojo

import java.io.Serializable
import com.google.gson.annotations.SerializedName

/**
 * Created by chevil on 23/05/19.
 * JSON to object mapping for the geo field
 */
class GeoPosition : Serializable {

    @SerializedName("lat")
    var lat: Double = 0.0

    @SerializedName("lng")
    var lng: Double = 0.0

}
