package com.example.boydjohnson.androidutubeuclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.boydjohnson.androidutubeuclient.R;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class UserListAdapter extends ArrayAdapter {

    private ArrayList<String> mUserList;
    private Context mContext;
    private LayoutInflater mInflater;

    public UserListAdapter(Context context, int resourceID, ArrayList<String> userlist){
        super(context, resourceID, userlist);
        mUserList = userlist;
        mContext = context;
        mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String username = getItem(position);
        if(convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
        textView.setText(username);


        return convertView;
    }

    @Override
    public String getItem(int pos){
        return this.mUserList.get(pos);
    }
}
