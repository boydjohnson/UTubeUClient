package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.SuggestionListAdapter;
import com.example.boydjohnson.androidutubeuclient.adapters.UserListAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.Suggestion;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionList;
import com.squareup.otto.Subscribe;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class SuggestionsListFragment extends ListFragment {

    public static final String CHATROOM_ID_TAG = "com.example.boydjohnson.androidutubeuclient.fragments.suggestionlistfragment.chatroom_id";

    private ArrayList<Suggestion> mSuggestionList;

    private SuggestionListAdapter mAdapter;

    private Integer mChatroomId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageBus.getInstance().register(this);
        mSuggestionList = new ArrayList<>();
        mChatroomId = getArguments().getInt(CHATROOM_ID_TAG);
    }

    @Subscribe
    public void getSuggestionList(SuggestionList suggestionList){
        ArrayList<String> suggestions_to_be_parsed = suggestionList.getThe_list();
        ObjectMapper mapper = new ObjectMapper();
        for(String suggestionJson: suggestions_to_be_parsed){
            try {
                Suggestion suggestion = mapper.readValue(suggestionJson, Suggestion.class);
                mSuggestionList.add(suggestion);
            }catch (Exception e){
                Log.e("PARSING", "Parsing suggestions", e);
            }
        }
        if(getListAdapter()!=null){
            mAdapter.clear();
            mAdapter.addAll(mSuggestionList);
            mAdapter.notifyDataSetChanged();
            Log.i("SUGGLIST", "NotifyDataSetChanged");

        }else {
            mAdapter = new SuggestionListAdapter(getActivity(), R.layout.fragment_container, mSuggestionList, mChatroomId);
            this.setListAdapter(mAdapter);
            Log.i("SUGGLIST", "setlistadapter");
        }
    }



}
