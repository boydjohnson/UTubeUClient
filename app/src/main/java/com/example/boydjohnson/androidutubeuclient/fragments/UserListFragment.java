package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.UserListAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.UsernamesInChatroom;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class UserListFragment extends ListFragment {

    private ArrayList<String> mUserList;

    private UserListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserList = new ArrayList<>();
        MessageBus.getInstance().register(this);


    }


    @Subscribe
    public void getUserList(UsernamesInChatroom userlist){
        mUserList = userlist.getUsernames();
        Log.i("USERLIST", Integer.toString(mUserList.size()));
        if(getListAdapter()!=null){
            mAdapter.clear();
            mAdapter.addAll(mUserList);
            mAdapter.notifyDataSetChanged();
            Log.i("USERLIST", "NotifyDataSetChanged");

        }else {
            mAdapter = new UserListAdapter(getActivity(), R.layout.fragment_container, mUserList);
            this.setListAdapter(mAdapter);
            Log.i("USERLIST", "setlistadapter");
        }
    }





}
