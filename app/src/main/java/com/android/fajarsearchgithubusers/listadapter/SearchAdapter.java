package com.android.fajarsearchgithubusers.listadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.android.fajarsearchgithubusers.R;
import com.android.fajarsearchgithubusers.databinding.SearchItemBinding;
import com.android.fajarsearchgithubusers.model.Items;

import java.util.List;

public class SearchAdapter extends ArrayAdapter<Items> {

    Activity activity;
    List<Items> items;

    public SearchAdapter(Activity activity, int textViewResourceId, List<Items> items) {
        super(activity, textViewResourceId, items);
        this.items = items;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        try {
            if(convertView == null) {
                SearchItemBinding searchItemBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                        R.layout.search_item, parent, false);
                holder = new ViewHolder(searchItemBinding);
                holder.view.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        holder.searchItemBinding.setUser(items.get(position));
        return holder.view;
    }

    private static class ViewHolder {
        View view;
        SearchItemBinding searchItemBinding;

        ViewHolder(SearchItemBinding searchItemBinding){
            this.view = searchItemBinding.getRoot();
            this.searchItemBinding = searchItemBinding;
        }
    }

}