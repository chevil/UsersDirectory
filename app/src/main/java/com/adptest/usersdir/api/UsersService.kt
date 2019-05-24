package com.adptest.usersdir.api

import com.adptest.usersdir.pojo.User 
import com.adptest.usersdir.pojo.Address 
import com.adptest.usersdir.pojo.GeoPosition 
import com.adptest.usersdir.pojo.Company 

import retrofit2.Call;
import retrofit2.http.GET;

// rxJava
import io.reactivex.Observable

/**
 * Created by chevil on 23/05/19.
 */

public interface UsersService {

    @GET("users")
    fun getUsers() : Observable<List<User>>
}
