package com.android.fajarsearchgithubusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.fajarsearchgithubusers.connection.APICallTask.search.GetUserSearch;
import com.android.fajarsearchgithubusers.listadapter.SearchAdapter;
import com.android.fajarsearchgithubusers.model.Items;
import com.android.fajarsearchgithubusers.utilities.PopupMsg;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_symbol = (TextView) findViewById(R.id.search_symbol);
        user_search = (EditText) findViewById(R.id.user_search);
        listview_search = (ListView) findViewById(R.id.listview_search);
        progress_search = (ProgressBar) findViewById(R.id.progress_search);
        layout_no_internet = (LinearLayout) findViewById(R.id.layout_no_internet);
        retry_btn = (Button) findViewById(R.id.retry_btn);
        layout_empty_result = (LinearLayout) findViewById(R.id.layout_empty_result);

        mProgressBarFooter = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar_footer, null, false);

        //change the symbol
        Typeface typeFace=Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf");
        search_symbol.setTypeface(typeFace);

        user_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progress_search.setVisibility(View.VISIBLE);
                    Search(v.getText().toString(), page);

                    //hide keyboard
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(user_search.getWindowToken(), 0);

                    //remove no internet
                    layout_no_internet.setVisibility(View.GONE);
                    //remove empty result
                    layout_empty_result.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });

        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress_search.setVisibility(View.VISIBLE);
                Search(user_search.getText().toString(), page);

                //remove no internet
                layout_no_internet.setVisibility(View.GONE);
                //remove empty result
                layout_empty_result.setVisibility(View.GONE);
            }
        });
    }

    public void Search(@NonNull final String userSearch,@NonNull final int goPage){
        try {
            new GetUserSearch(MainActivity.this) {
                @Override
                public void OnApiResult(int statusCode, String failReason) {
                    //dissmiss loading
                    progress_search.setVisibility(View.GONE);

                    if (statusCode == 1) {
                        //success
                        if (getItems().size() > 0){
                            listview_search.setVisibility(View.VISIBLE);

                            totalPage = getTotalPage();
                            init(getItems(), userSearch);

                            //remove empty result
                            layout_empty_result.setVisibility(View.GONE);
                        } else {
                            listview_search.setVisibility(View.GONE);

                            //show empty result
                            layout_empty_result.setVisibility(View.VISIBLE);
                        }

                        //remove no internet
                        layout_no_internet.setVisibility(View.GONE);
                    } else if (statusCode == 2){
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
                        PopupMsg.msgCanReturn(MainActivity.this, dialogClickListener, getResources().getString(R.string.oops), getResources().getString(R.string.limit_search));

                        //remove loading
                        listview_search.removeFooterView(mProgressBarFooter);

                        //remove no internet
                        layout_no_internet.setVisibility(View.GONE);
                    } else {
                        //failed
                        if (statusCode == 4){//no internet
                            if (tempItems.size() > 0){
                                PopupMsg.msgOk(MainActivity.this, getResources().getString(R.string.oops), getResources().getString(R.string.issue_internet_connection_try_again));

                                //remove no internet
                                layout_no_internet.setVisibility(View.GONE);
                            } else {
                                //show no internet
                                layout_no_internet.setVisibility(View.VISIBLE);
                            }
                        } else {
                            PopupMsg.msgOk(MainActivity.this, getResources().getString(R.string.oops), failReason);

                            //remove no internet
                            layout_no_internet.setVisibility(View.GONE);
                        }

                        //remove loading
                        listview_search.removeFooterView(mProgressBarFooter);
                    }
                }
            }.callApi(userSearch, String.valueOf(goPage));
        } catch (Exception e){
            e.printStackTrace();
        }
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
                searchAdapter = new SearchAdapter(this, R.layout.search_item, tempItems);
                listview_search.setFastScrollEnabled(true);
                listview_search.setBackgroundColor(getResources().getColor(android.R.color.white));
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
                            Search(userName, page);
                        }
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
