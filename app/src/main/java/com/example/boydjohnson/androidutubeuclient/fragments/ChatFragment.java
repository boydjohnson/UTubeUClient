package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.LastTen;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageIn;
import com.example.boydjohnson.androidutubeuclient.data.LastTenMessage;

import com.squareup.otto.Subscribe;

import org.codehaus.jackson.map.ObjectMapper;


/**
 * Created by boydjohnson on 12/1/15.
 */
public class ChatFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        MessageBus.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_chat, parent, false);


        return view;
    }

    @Subscribe
    public void getTextMessage(TextMessageIn messageIn){
        Log.i("Chatroom::::", messageIn.getMessage() + messageIn.getUsername());
    }

    @Subscribe
    public void getLastTenMessages(LastTen lastTenMessages){
        ObjectMapper mapper = new ObjectMapper();
        for(String json: lastTenMessages.getLastTenMessages()) {
            try {
                LastTenMessage message = mapper.readValue(json, LastTenMessage.class);
                Log.i("LASTTEN:::", message.getMessage()+message.getUsername());
            }catch (Exception e){
                Log.e("LASTTEN", e.toString());
            }

        }

    }
}
