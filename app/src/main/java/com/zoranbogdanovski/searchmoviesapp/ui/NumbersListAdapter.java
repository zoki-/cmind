package com.zoranbogdanovski.searchmoviesapp.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zoranbogdanovski.searchmoviesapp.R;

import java.util.List;

/**
 * Adapter for movie numbers list.
 */
public class NumbersListAdapter extends ArrayAdapter<String> {

    public NumbersListAdapter(Context context, List<String> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = View.inflate(getContext(), R.layout.list_item_numbers, null);
        }

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        tvTitle.setText(getItem(position));

        return view;
    }
}
