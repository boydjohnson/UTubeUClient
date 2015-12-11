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
import com.example.boydjohnson.androidutubeuclient.data.SuggestionIn;
import com.example.boydjohnson.androidutubeuclient.data.VoteOut;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/11/15.
 */
public class SuggestionListAdapter extends ArrayAdapter<SuggestionIn> {

    private ArrayList<SuggestionIn> mSuggestionInList;

    private Context mContext;

    private LayoutInflater mInflater;

    private Bus mMessageBus = MessageBus.getInstance();

    private Integer mChatroom_id;

    public SuggestionListAdapter(Context context, int resourceID, ArrayList<SuggestionIn> suggestionlist, Integer chatroom_id){
        super(context, resourceID, suggestionlist);
        mSuggestionInList = suggestionlist;
        mContext = context;
        mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mChatroom_id = chatroom_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SuggestionIn suggestionIn = getItem(position);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.simple_suggestion_list, parent, false);
        }

        TextView titleTV = (TextView)convertView.findViewById(R.id.suggestion_title);
        titleTV.setText(suggestionIn.getTitle());

        TextView descTV = (TextView)convertView.findViewById(R.id.suggestion_description);
        descTV.setText(suggestionIn.getDescription());


        ImageView imageView = (ImageView)convertView.findViewById(R.id.suggestion_image);
        Picasso.with(mContext).load(suggestionIn.getImage_url()).into(imageView);


        Button voter = (Button)convertView.findViewById(R.id.vote_checker);
        voter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VoteOut voteOut = new VoteOut(suggestionIn.getYoutube_value(), mChatroom_id, true);
                mMessageBus.post(voteOut);
            }
        });

        return convertView;
    }

    @Override
    public SuggestionIn getItem(int pos){
        return this.mSuggestionInList.get(pos);
    }



}
