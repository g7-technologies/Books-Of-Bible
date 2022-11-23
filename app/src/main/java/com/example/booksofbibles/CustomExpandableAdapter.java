package com.example.booksofbibles;

import com.example.booksofbibles.classes.Deal;
import com.example.booksofbibles.classes.Token;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomExpandableAdapter extends BaseExpandableListAdapter {
    private List<Deal> catList;
    private Context ctx;

    public CustomExpandableAdapter(List<Deal> catList, Context ctx) {
        this.catList = catList;
        this.ctx = ctx;
    }


    @Override
    public int getGroupCount() {
        return catList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return catList.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getItemList();
    }

    @Override
    public long getGroupId(int i) {
        return catList.get(i).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getItemList().get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentView) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_for_food, parentView, false);

        }

        TextView textViewParent = convertView.findViewById(R.id.list_text_food);
        Deal deal = catList.get(parent);
        textViewParent.setText(deal.getName());
        return convertView;
    }

    @Override
    public View getChildView(int parent, int child, boolean isLastChild, View convertView, ViewGroup parentView) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_for_food, parentView, false);

        }
        Token token = catList.get(parent).getItemList().get(child);
        TextView textViewChild = convertView.findViewById(R.id.token_in_food);
        textViewChild.setText(token.getRequired_token());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
