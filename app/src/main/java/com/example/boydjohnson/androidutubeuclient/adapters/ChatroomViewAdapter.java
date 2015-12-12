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
    private String username;

    public ChatroomViewAdapter(FragmentManager fm, Integer chatroom_id, String username){
        super(fm);
        this.chatroom_id = chatroom_id;
        this.username = username;
    }

    @Override
    public Fragment getItem(int pos){

        Bundle b = new Bundle();
        b.putInt(SuggestionsListFragment.CHATROOM_ID_TAG, chatroom_id);
        b.putString(SuggestionsListFragment.USERNAME_TAG, username);
        int w = pos % 4;
        switch (w){

            case 0:
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setArguments(b);
                return chatFragment;

            case 1: return new UserListFragment();

            case 2:
                SuggestionsListFragment suggestionFragment = new SuggestionsListFragment();
                suggestionFragment.setArguments(b);
                return suggestionFragment;

            case 3:
                YoutubeSearchFragment youtubeFragment= new  YoutubeSearchFragment();
                youtubeFragment.setArguments(b);
                return youtubeFragment;

            default:
                ChatFragment cFragment = new ChatFragment();
                cFragment.setArguments(b);
                return cFragment;
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
