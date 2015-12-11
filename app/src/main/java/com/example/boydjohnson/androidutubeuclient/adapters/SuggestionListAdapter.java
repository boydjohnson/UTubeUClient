package com.example.boydjohnson.androidutubeuclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.data.Suggestion;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/11/15.
 */
public class SuggestionListAdapter extends ArrayAdapter<Suggestion> {

    private ArrayList<Suggestion> mSuggestionList;

    private Context mContext;

    private LayoutInflater mInflater;

    public SuggestionListAdapter(Context context, int resourceID, ArrayList<Suggestion> suggestionlist){
        super(context, resourceID, suggestionlist);
        mSuggestionList = suggestionlist;
        mContext = context;
        mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Suggestion suggestion = getItem(position);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.simple_suggestion_list, parent, false);
        }

        TextView titleTV = (TextView)convertView.findViewById(R.id.suggestion_title);



        return convertView;
    }

    @Override
    public Suggestion getItem(int pos){
        return this.mSuggestionList.get(pos);
    }
}
