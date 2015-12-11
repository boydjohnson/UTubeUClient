package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.ChatroomViewAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.Chatroom;
import com.example.boydjohnson.androidutubeuclient.data.LastTen;
import com.example.boydjohnson.androidutubeuclient.data.Start;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageIn;
import com.example.boydjohnson.androidutubeuclient.data.LastTenMessage;

import com.example.boydjohnson.androidutubeuclient.data.TextMessageOut;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;



/**
 * Created by boydjohnson on 12/1/15.
 */
public class ChatFragment extends Fragment {

    private LinearLayout mChatterTextDock;
    private ScrollView mScroller;

    private FragmentManager mFragmentManager;

    private Bus mMessageBus;

    private Integer mChatroomId;
    private String mUsername;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mMessageBus = MessageBus.getInstance();
        mMessageBus.register(this);
        Bundle bundle = getArguments();
        mChatroomId = bundle.getInt(SuggestionsListFragment.CHATROOM_ID_TAG);
        mUsername = bundle.getString(SuggestionsListFragment.USERNAME_TAG);
        mFragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_chat, parent, false);
        mScroller = (ScrollView)view.findViewById(R.id.scroller);
        mChatterTextDock = (LinearLayout)view.findViewById(R.id.chatter_text_dock);


        EditText textInputBox = (EditText)view.findViewById(R.id.chatter_text_box);
        textInputBox.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("KEYPRESSED", Integer.toString(actionId));

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText editText = (EditText) v;
                    if(!editText.getText().toString().equals("")) {
                        TextMessageOut textMessageOut = new TextMessageOut(mChatroomId, mUsername, editText.getText().toString());
                        editText.setText("");
                        mMessageBus.post(textMessageOut);
                    }else{
                        Toast.makeText(getActivity(), "No text entered!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

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
        textView.setText(text);
        mChatterTextDock.addView(textView);
        mScroller.post(new Runnable() {
            @Override
            public void run() {
                mScroller.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Subscribe
    public void getStartTheVideo(Start start){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.container_for_fragments,);

    }

    @Subscribe
    public void getLastTenMessages(LastTen lastTenMessages){
        Log.i("LASTTEN::", "RECEIVED");
        ObjectMapper mapper = new ObjectMapper();
        for(String json: lastTenMessages.getLastTenMessages()) {
            try {
                LastTenMessage message = mapper.readValue(json, LastTenMessage.class);
                TextView textView = new TextView(getActivity());
                String text = message.getUsername()+" : "+message.getMessage();
                textView.setText(text);
                mChatterTextDock.addView(textView);
            }catch (Exception e){
                Log.e("LASTTEN", e.toString());
            }


        }
        mScroller.fullScroll(View.FOCUS_DOWN);

    }

}
