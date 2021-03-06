package com.example.boydjohnson.androidutubeuclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

        TextView votingPercentageTV = (TextView)convertView.findViewById(R.id.vote_percentage);

        //This piece of code is forward-thinking. Right now getPercentage is always null on a suggestion.
        if(suggestionIn.getPercentage()==null) {
            RelativeLayout relativeLayout = (RelativeLayout)convertView.findViewById(R.id.suggestion_container);

            votingPercentageTV.setText("0.00");

            Button voter = new Button(mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            voter.setLayoutParams(params);
            voter.setText("Vote!");
            voter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button votingButton = (Button) v;
                    VoteOut voteOut = new VoteOut(suggestionIn.getYoutube_value(), mChatroom_id, true);
                    mMessageBus.post(voteOut);
                }
            });
            relativeLayout.addView(voter);

        }else{
            votingPercentageTV.setText(Float.toString(suggestionIn.getPercentage()));
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.suggestion_image);
        Picasso.with(mContext).load(suggestionIn.getImage_url()).into(imageView);




        return convertView;
    }

    @Override
    public SuggestionIn getItem(int pos){
        return this.mSuggestionInList.get(pos);
    }



}
