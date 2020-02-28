package com.example.bazadedateexplo;

import java.util.ArrayList;

import android.app.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListAdapter extends ArrayAdapter<String> {

    ListAdapter(Activity context, ArrayList<String> list) {
        super(context,-1,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item,parent, false);
        }
        String text = getItem(position);
        TextView textView = listView.findViewById(R.id.textView);
        textView.setText(text);

        return listView;
    }
}
