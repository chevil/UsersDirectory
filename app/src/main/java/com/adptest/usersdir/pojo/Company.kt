package com.adptest.usersdir.pojo

import java.io.Serializable
import com.google.gson.annotations.SerializedName

/**
 * Created by chevil on 23/05/19.
 * JSON to object mapping for the company field
 */
class Company : Serializable {

    @SerializedName("name")
    var name: String = ""

    @SerializedName("catchPhrase")
    var catchPhrase: String = ""

    @SerializedName("bs")
    var bs: String = ""

}
