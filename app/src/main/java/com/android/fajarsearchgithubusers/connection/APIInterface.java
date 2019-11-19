package com.android.fajarsearchgithubusers.connection;

import com.android.fajarsearchgithubusers.model.UserSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/search/users?")
    Call<UserSearch> doGetUserSearchList(
            @Query("q") String userSearch,
            @Query("page") String page
    );
}
