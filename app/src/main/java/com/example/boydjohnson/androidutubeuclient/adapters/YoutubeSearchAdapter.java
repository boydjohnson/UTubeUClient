package com.example.boydjohnson.androidutubeuclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionOut;
import com.example.boydjohnson.androidutubeuclient.data.VoteOut;
import com.example.boydjohnson.androidutubeuclient.data.YoutubeSearchResult;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/12/15.
 */
public class YoutubeSearchAdapter extends ArrayAdapter<YoutubeSearchResult> {

    private ArrayList<YoutubeSearchResult> mSearchResults;
    private Context mContext;
    private LayoutInflater mInflater;

    private Integer mChatroomId;

    private Bus mMessageBus;

    public YoutubeSearchAdapter(Context context, int resourceID, ArrayList<YoutubeSearchResult> searchResults, Integer chatroomid){
        super(context, resourceID, searchResults);
        mSearchResults = searchResults;
        mContext = context;
        mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMessageBus = MessageBus.getInstance();
        mChatroomId = chatroomid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final YoutubeSearchResult searchResult = getItem(position);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.simple_search_list, parent, false);
        }
        TextView titleTV = (TextView)convertView.findViewById(R.id.suggestion_title);
        titleTV.setText(searchResult.getTitle());

        TextView descTV = (TextView)convertView.findViewById(R.id.suggestion_description);
        descTV.setText(searchResult.getDescription());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.youtube_image);
        Picasso.with(mContext).load(searchResult.getThumbnailURL()).into(imageView);


        Button suggestBtn = (Button)convertView.findViewById(R.id.suggest_button);
        suggestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SuggestionOut suggestionOut = new SuggestionOut(mChatroomId, searchResult.getId(),
                        searchResult.getTitle(), searchResult.getDescription(), searchResult.getThumbnailURL());
                mMessageBus.post(suggestionOut);
            }
        });


        return convertView;
    }

}
