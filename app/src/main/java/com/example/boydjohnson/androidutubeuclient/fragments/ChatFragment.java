package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.ChatroomViewAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.LastTen;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageIn;
import com.example.boydjohnson.androidutubeuclient.data.LastTenMessage;

import com.squareup.otto.Subscribe;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;



/**
 * Created by boydjohnson on 12/1/15.
 */
public class ChatFragment extends Fragment {

    private ArrayList<String> mMessagesSaved;

    private LinearLayout mChatterTextDock;
    private ScrollView mScroller;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        MessageBus.getInstance().register(this);
        mMessagesSaved = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_chat, parent, false);
        mScroller = (ScrollView)view.findViewById(R.id.scroller);
        mChatterTextDock = (LinearLayout)view.findViewById(R.id.chatter_text_dock);
        for(String message: mMessagesSaved){
            TextView textView = new TextView(getActivity());
            textView.setText(message);
            mChatterTextDock.addView(textView);
        }

        return view;
    }

    @Subscribe
    public void getTextMessage(TextMessageIn messageIn){
        TextView textView = new TextView(getActivity());
        String text;
        if(messageIn.getUsername()==null){
            text = "You : "+messageIn.getMessage();
        }else {
            text = messageIn.getUsername() + " : " + messageIn.getMessage();
        }
        mMessagesSaved.add(text);
        textView.setText(text);
        mChatterTextDock.addView(textView);
        mScroller.fullScroll(View.FOCUS_DOWN);
    }

    @Subscribe
    public void getLastTenMessages(LastTen lastTenMessages){
        ObjectMapper mapper = new ObjectMapper();
        for(String json: lastTenMessages.getLastTenMessages()) {
            try {
                LastTenMessage message = mapper.readValue(json, LastTenMessage.class);
                TextView textView = new TextView(getActivity());
                String text = message.getUsername()+" : "+message.getMessage();
                mMessagesSaved.add(text);
                textView.setText(text);
                mChatterTextDock.addView(textView);
            }catch (Exception e){
                Log.e("LASTTEN", e.toString());
            }
            mScroller.fullScroll(View.FOCUS_DOWN);

        }

    }

}
