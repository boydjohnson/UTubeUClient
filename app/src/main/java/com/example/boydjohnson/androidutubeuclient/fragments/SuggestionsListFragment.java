package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.SuggestionListAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.Start;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionIn;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionList;
import com.example.boydjohnson.androidutubeuclient.data.VoteIn;
import com.squareup.otto.Subscribe;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class SuggestionsListFragment extends ListFragment {

    public static final String CHATROOM_ID_TAG = "com.example.boydjohnson.androidutubeuclient.fragments.suggestionlistfragment.chatroom_id";
    public static final String USERNAME_TAG = "com.example.boydjohnson.username_tag";

    private ArrayList<SuggestionIn> mSuggestionInList;

    private SuggestionListAdapter mAdapter;

    private Integer mChatroomId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageBus.getInstance().register(this);
        mSuggestionInList = new ArrayList<>();
        mChatroomId = getArguments().getInt(CHATROOM_ID_TAG);
    }


    @Subscribe
    public void getSuggestionList(SuggestionList suggestionList){
        ArrayList<String> suggestions_to_be_parsed = suggestionList.getThe_list();
        ObjectMapper mapper = new ObjectMapper();
        for(String suggestionJson: suggestions_to_be_parsed){
            try {
                SuggestionIn suggestionIn = mapper.readValue(suggestionJson, SuggestionIn.class);
                mSuggestionInList.add(suggestionIn);
            }catch (Exception e){
                Log.e("PARSING", "Parsing suggestions", e);
            }
        }

            mAdapter = new SuggestionListAdapter(getActivity(), R.layout.fragment_container, mSuggestionInList, mChatroomId);
            this.setListAdapter(mAdapter);


    }

    @Subscribe
    public void getSuggestionIn(SuggestionIn suggestionIn){

        if(mAdapter==null){
            mAdapter = new SuggestionListAdapter(getActivity(), R.layout.fragment_container, mSuggestionInList, mChatroomId);
        }
        mAdapter.add(suggestionIn);
        mAdapter.notifyDataSetChanged();
    }


    //The code in the method doesn't seem the best but it is what I could think of
    @Subscribe
    public void getStartToRemoveSuggestion(Start start){
        int position=-1;
        for(int i=0; i<mAdapter.getCount(); i++){
            if(mAdapter.getItem(i).getYoutube_value().equals(start.getYoutube_value())){
                position = i;
                break;
            }
        }
        SuggestionIn suggestionToRemove = mAdapter.getItem(position);
        mAdapter.remove(suggestionToRemove);
        mAdapter.notifyDataSetChanged();
    }
    //This method's code seems suspect as well, but I couldn't implement another way
    @Subscribe
    public void getVoteIn(VoteIn vote){
        ArrayList<SuggestionIn> results = new ArrayList<>();
        for(int i=0; i<mAdapter.getCount(); i++){
            SuggestionIn suggestionIn = mAdapter.getItem(i);
            if(suggestionIn.getYoutube_value().equals(vote.getYoutube_value())){
                suggestionIn.setPercentage(vote.getPercentage());
            }
            results.add(suggestionIn);
        }
        mAdapter.clear();
        mAdapter.addAll(results);
        mAdapter.notifyDataSetChanged();
    }

}
