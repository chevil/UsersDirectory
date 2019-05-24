package com.adptest.usersdir.pojo

import java.io.Serializable
import com.google.gson.annotations.SerializedName

/**
 * Created by chevil on 23/05/19.
 * JSON to object mapping
 */
class User : Serializable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("username")
    var username: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("address")
    var address: Address = Address()

    @SerializedName("phone")
    var phone: String = ""

    @SerializedName("website")
    var website: String = ""

    @SerializedName("company")
    var company: Company = Company()

}
