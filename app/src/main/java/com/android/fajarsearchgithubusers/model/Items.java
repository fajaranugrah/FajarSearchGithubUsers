package com.android.fajarsearchgithubusers.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Items {

    @SerializedName("login")
    public String login;
    @SerializedName("id")
    public Integer id;
    @SerializedName("node_id")
    public String node_id;
    @SerializedName("avatar_url")
    public String avatar_url;
    @SerializedName("gravatar_id")
    public String gravatar_id;
    @SerializedName("url")
    public String url;
    @SerializedName("html_url")
    public String html_url;
    @SerializedName("followers_url")
    public String followers_url;
    @SerializedName("following_url")
    public String following_url;
    @SerializedName("gists_url")
    public String gists_url;
    @SerializedName("starred_url")
    public String starred_url;
    @SerializedName("subscriptions_url")
    public String subscriptions_url;
    @SerializedName("organizations_url")
    public String organizations_url;
    @SerializedName("repos_url")
    public String repos_url;
    @SerializedName("events_url")
    public String events_url;
    @SerializedName("received_events_url")
    public String received_events_url;
    @SerializedName("type")
    public String type;
    @SerializedName("site_admin")
    public Boolean site_admin;
    @SerializedName("score")
    public String score;

    @BindingAdapter({ "avatar" })
    public static void loadImage(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext())
                .load(imageURL)
                .override(96,96)
                .centerCrop()
                .transition(withCrossFade())
                .into(imageView);
    }
}
