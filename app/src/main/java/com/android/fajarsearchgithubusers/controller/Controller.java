package com.android.fajarsearchgithubusers.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.fajarsearchgithubusers.connection.APICallTask.search.GetUserSearch;
import com.android.fajarsearchgithubusers.view.ApiCallBackListener;

public class Controller {

    Activity activity;
    ProgressBar progress_search;
    LinearLayout layout_no_internet;
    LinearLayout layout_empty_result;

    ApiCallBackListener apiCallBackListener;

    public Controller(@NonNull Activity activity, @NonNull ProgressBar progress_search, @NonNull LinearLayout layout_no_internet, @NonNull LinearLayout layout_empty_result, @NonNull ApiCallBackListener apiCallBackListener) {
        this.activity = activity;
        this.progress_search = progress_search;
        this.layout_no_internet = layout_no_internet;
        this.layout_empty_result = layout_empty_result;

        this.apiCallBackListener = apiCallBackListener;
    }

    public boolean onClickSearch(@NonNull int actionId, @NonNull String name, @NonNull EditText user_search){
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            progress_search.setVisibility(View.VISIBLE);
            Search(name, 1);

            //hide keyboard
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(user_search.getWindowToken(), 0);

            //remove no internet
            layout_no_internet.setVisibility(View.GONE);
            //remove empty result
            layout_empty_result.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    public void onClickRetry(@NonNull String name){
        try {
            progress_search.setVisibility(View.VISIBLE);
            Search(name, 1);

            //remove no internet
            layout_no_internet.setVisibility(View.GONE);
            //remove empty result
            layout_empty_result.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void Search(@NonNull final String userSearch,@NonNull final int goPage){
        try {
            new GetUserSearch(activity) {
                @Override
                public void OnApiResult(int statusCode, String failReason) {
                    //dissmiss loading
                    progress_search.setVisibility(View.GONE);

                    if (statusCode == 1) {
                        apiCallBackListener.onFetchComplete(activity, getItems(), getTotalPage());
                    } else {
                        apiCallBackListener.onFetchNull(activity, statusCode, failReason);
                    }
                }
            }.callApi(userSearch, String.valueOf(goPage));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
