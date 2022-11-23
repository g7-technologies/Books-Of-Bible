package com.example.booksofbibles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterForGrid extends BaseAdapter {

    private String[] chapter_number;
    private Context context;
    LayoutInflater inflater;

    public AdapterForGrid(String[] chapter_number, Context context) {
        this.chapter_number = chapter_number;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chapter_number.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null){
            view = inflater.inflate(R.layout.grid_layout,null);
        }

        TextView tv_chapter_number = view.findViewById(R.id.chapter_number);
        tv_chapter_number.setText(chapter_number[i]);
        return view;
    }
}
