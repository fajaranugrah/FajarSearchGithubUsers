package com.android.fajarsearchgithubusers.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.fajarsearchgithubusers.R;
import com.android.fajarsearchgithubusers.controller.Controller;
import com.android.fajarsearchgithubusers.listadapter.SearchAdapter;
import com.android.fajarsearchgithubusers.model.Items;
import com.android.fajarsearchgithubusers.utilities.PopupMsg;

import java.util.ArrayList;
import java.util.List;

public class MainActivityView implements ApiCallBackListener{

    View rootView;
    Activity activity;

    TextView search_symbol;
    EditText user_search;
    ProgressBar progress_search;
    ListView listview_search;

    //internet
    LinearLayout layout_no_internet;
    Button retry_btn;

    //empty result
    LinearLayout layout_empty_result;

    //function adapter
    SearchAdapter searchAdapter;
    int page = 1;
    int totalPage = 1;
    boolean loading = false;
    List<Items> tempItems = new ArrayList<>();

    View mProgressBarFooter;

    Controller controller;
    public MainActivityView (Activity activity, ViewGroup continer){
        rootView = LayoutInflater.from(activity).inflate(R.layout.activity_main,continer);
        this.activity = activity;
    }

    @Override
    public void bindDataToView() {

    }

    @Override
    public void onFetchComplete(@NonNull Activity activity, @NonNull List<Items> items, int totalPage) {
        //success
        if (items.size() > 0){
            listview_search.setVisibility(View.VISIBLE);

            this.totalPage = totalPage;
            init(items, user_search.getText().toString());

            //remove empty result
            layout_empty_result.setVisibility(View.GONE);
        } else {
            listview_search.setVisibility(View.GONE);

            //show empty result
            layout_empty_result.setVisibility(View.VISIBLE);
        }

        //remove no internet
        layout_no_internet.setVisibility(View.GONE);
    }

    @Override
    public void onFetchNull(@NonNull Activity activity, int responseCode, String failReason) {
        if (responseCode == 2){
            //get limit search
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            page--;
                            loading = false;

                            break;
                    }
                }
            };
            PopupMsg.msgCanReturn(activity, dialogClickListener, activity.getResources().getString(R.string.oops), activity.getResources().getString(R.string.limit_search));

            //remove loading
            listview_search.removeFooterView(mProgressBarFooter);

            //remove no internet
            layout_no_internet.setVisibility(View.GONE);
        } else {
            //failed
            if (responseCode == 4){//no internet
                if (tempItems.size() > 0){
                    PopupMsg.msgOk(activity, activity.getResources().getString(R.string.oops), activity.getResources().getString(R.string.issue_internet_connection_try_again));

                    //remove no internet
                    layout_no_internet.setVisibility(View.GONE);
                } else {
                    //show no internet
                    layout_no_internet.setVisibility(View.VISIBLE);
                }
            } else {
                PopupMsg.msgOk(activity, activity.getResources().getString(R.string.oops), failReason);

                //remove no internet
                layout_no_internet.setVisibility(View.GONE);
            }

            //remove loading
            listview_search.removeFooterView(mProgressBarFooter);
        }
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void initView() {
        search_symbol = (TextView) rootView.findViewById(R.id.search_symbol);
        user_search = (EditText) rootView.findViewById(R.id.user_search);
        listview_search = (ListView) rootView.findViewById(R.id.listview_search);
        progress_search = (ProgressBar) rootView.findViewById(R.id.progress_search);
        layout_no_internet = (LinearLayout) rootView.findViewById(R.id.layout_no_internet);
        retry_btn = (Button) rootView.findViewById(R.id.retry_btn);
        layout_empty_result = (LinearLayout) rootView.findViewById(R.id.layout_empty_result);

        mProgressBarFooter = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar_footer, null, false);

        //change the symbol
        Typeface typeFace=Typeface.createFromAsset(activity.getAssets(), "fonts/fontawesome-webfont.ttf");
        search_symbol.setTypeface(typeFace);

        user_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return controller.onClickSearch(actionId, v.getText().toString(), user_search);
            }
        });

        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.onClickRetry(user_search.getText().toString());
            }
        });

        controller = new Controller(activity, progress_search, layout_no_internet, layout_empty_result, MainActivityView.this);
    }

    public void init(@NonNull List<Items> items, @NonNull final String userName){
        try {
            if (loading){
                tempItems.addAll(items);
                searchAdapter.notifyDataSetChanged();

                //change to false for loadmore again
                loading = false;

                //remove loading
                listview_search.removeFooterView(mProgressBarFooter);
            } else {
                tempItems = new ArrayList<>();
                tempItems.addAll(items);
                searchAdapter = new SearchAdapter(activity, R.layout.search_item, tempItems);
                listview_search.setFastScrollEnabled(true);
                listview_search.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
                listview_search.setAdapter(searchAdapter);
            }

            listview_search.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if(lastInScreen == totalItemCount && !loading){
                        if (page < totalPage){
                            //create loading
                            listview_search.addFooterView(mProgressBarFooter);

                            loading = true;
                            page++;
                            controller.Search(userName, page);
                        }
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
