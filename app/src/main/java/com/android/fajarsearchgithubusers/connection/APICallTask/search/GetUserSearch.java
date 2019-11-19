package com.android.fajarsearchgithubusers.connection.APICallTask.search;

import android.app.Activity;

import com.android.fajarsearchgithubusers.connection.APICallTask.ApiCalling;
import com.android.fajarsearchgithubusers.connection.APIClient;
import com.android.fajarsearchgithubusers.connection.APIInterface;
import com.android.fajarsearchgithubusers.model.Items;
import com.android.fajarsearchgithubusers.model.UserSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class GetUserSearch extends ApiCalling {

    Activity activity;
    UserSearch userSearch;
    List<Items> items = new ArrayList<>();
    int totalPage;

    public GetUserSearch(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void callApi(String... params){

        //default limit of 10 hits per minute
        String user = params[0];
        String page = params[1];

        try {
            APIClient.getClient()
                    .create(APIInterface.class)
                    .doGetUserSearchList(user, page)
                    .enqueue(new Callback<UserSearch>() {
                        @Override
                        public void onResponse(Call<UserSearch> call, Response<UserSearch> response) {
                            switch (response.code()){
                                case 200:
                                    try {
                                        userSearch = response.body();
                                        items = userSearch.items;
                                        if (response.headers().get("Link")!=null){
                                            String headerLink = Objects.requireNonNull(response.headers().get("Link"));

                                            //get last page
                                            List<String> items = Arrays.asList(headerLink.split(", "));
                                            for (int i=0; i < items.size(); i++){
                                                if (items.get(i).contains("rel=\"last\"")){
                                                    String page = items.get(i).substring(items.get(i).lastIndexOf("page="),
                                                            items.get(i).lastIndexOf(">"));
                                                    totalPage = Integer.parseInt(page.substring(5));
                                                }
                                            }
                                        }
                                        OnApiResult(1, "");
                                    } catch (Exception convert){
                                        convert.printStackTrace();
                                        OnApiResult( 2, "");
                                    }
                                    break;
                                case 403:
                                    OnApiResult(2, response.message());
                                    break;
                                default:
                                    OnApiResult(3, response.message());
                                    break;
                            }

                        }

                        @Override
                        public void onFailure(Call<UserSearch> call, Throwable t) {
                            call.cancel();
                            OnApiResult(4, t.getMessage());
                        }
                    });
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public abstract void OnApiResult(int statusCode, String failReason);

    public List<Items> getItems() {
        return this.items;
    }

    public int getTotalPage() {
        return this.totalPage;
    }
}
