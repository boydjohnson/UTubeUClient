package com.example.boydjohnson.androidutubeuclient.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.boydjohnson.androidutubeuclient.fragments.ChatFragment;
import com.example.boydjohnson.androidutubeuclient.fragments.SuggestionsListFragment;
import com.example.boydjohnson.androidutubeuclient.fragments.UserListFragment;
import com.example.boydjohnson.androidutubeuclient.fragments.YoutubeSearchFragment;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class ChatroomViewAdapter extends FragmentPagerAdapter {

    private Integer chatroom_id;

    public ChatroomViewAdapter(FragmentManager fm, Integer chatroom_id){
        super(fm);
        this.chatroom_id = chatroom_id;
    }

    @Override
    public Fragment getItem(int pos){

        int w = pos % 4;
        switch (w){

            case 0: return new ChatFragment();

            case 1: return new UserListFragment();

            case 2:
                SuggestionsListFragment suggestionFragment = new SuggestionsListFragment();
                Bundle b = new Bundle();
                b.putInt(SuggestionsListFragment.CHATROOM_ID_TAG, chatroom_id);
                suggestionFragment.setArguments(b);
                return suggestionFragment;

            case 3: return new YoutubeSearchFragment();

            default: return new ChatFragment();
        }
    }

    @Override
    public int getCount(){
        return 4;
    }


    public interface FragmentLifecycle{
        void onPauseFragment();
        void onResumeFragment();
    }
}
