package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.UserListAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.squareup.otto.Subscribe;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class SuggestionsListFragment extends ListFragment {

    public static final String CHATROOM_ID_TAG = "com.example.boydjohnson.androidutubeuclient.fragments.suggestionlistfragment.chatroom_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageBus.getInstance().register(this);



    }





}
