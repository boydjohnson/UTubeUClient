package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.SuggestionListAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionIn;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionList;
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
        if(getListAdapter()!=null){
            mAdapter.clear();
            mAdapter.addAll(mSuggestionInList);
            mAdapter.notifyDataSetChanged();
            Log.i("SUGGLIST", "NotifyDataSetChanged");

        }else {
            mAdapter = new SuggestionListAdapter(getActivity(), R.layout.fragment_container, mSuggestionInList, mChatroomId);
            this.setListAdapter(mAdapter);
            Log.i("SUGGLIST", "setlistadapter");
        }
    }

    @Subscribe
    public void getSuggestionIn(SuggestionIn suggestionIn){

        if(mAdapter==null){
            mAdapter = new SuggestionListAdapter(getActivity(), R.layout.fragment_container, mSuggestionInList, mChatroomId);
        }
        mAdapter.add(suggestionIn);
        mAdapter.notifyDataSetChanged();
    }

}
