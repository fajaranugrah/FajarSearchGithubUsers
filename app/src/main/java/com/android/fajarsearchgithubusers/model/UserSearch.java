package com.android.fajarsearchgithubusers.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserSearch {

    @SerializedName("total_count")
    public Integer total_count;
    @SerializedName("incomplete_results")
    public Boolean incomplete_results;
    @SerializedName("items")
    public List<Items> items = new ArrayList();

}
