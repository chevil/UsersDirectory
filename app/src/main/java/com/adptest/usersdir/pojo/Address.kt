package com.adptest.usersdir.pojo

import java.io.Serializable
import com.google.gson.annotations.SerializedName

/**
 * Created by chevil on 23/05/19.
 * JSON to object mapping for the address field
 */
class Address : Serializable {

    @SerializedName("street")
    var street: String = ""

    @SerializedName("suite")
    var suite: String = ""

    @SerializedName("city")
    var city: String = ""

    @SerializedName("zipcode")
    var zipcode: String = ""

    @SerializedName("geo")
    var geo: GeoPosition = GeoPosition()

}
