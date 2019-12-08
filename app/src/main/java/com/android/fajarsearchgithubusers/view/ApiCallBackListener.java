package com.android.fajarsearchgithubusers.view;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.android.fajarsearchgithubusers.model.Items;

import java.util.List;

public interface ApiCallBackListener extends MVCView{

    void bindDataToView();

    void onFetchComplete(@NonNull Activity activity, @NonNull List<Items> items, int totalPage);
    void onFetchNull(@NonNull Activity activity, int responseCode, String failReason);
}
