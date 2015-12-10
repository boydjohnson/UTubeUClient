package com.example.boydjohnson.androidutubeuclient.fragments;

import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.Chatroom;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageIn;
import com.squareup.otto.Subscribe;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/7/15.
 */
public class ChatroomListFragment extends Fragment {

    public static final String USER_TOKEN_KEY = "com.example.boydjohnson.androidutubeuclient.fragments.chatroomlistfragment.user_token_key";

    private String mUserToken;

    private String mAPItoken;

    private ArrayList<Chatroom> mOwnedChatrooms;

    private LinearLayout mOwnedChatroomDock;

    private OpenChatroomAndWSListener mListener;

    public interface OpenChatroomAndWSListener {
        void openChatroom(Chatroom chatroom);
    }

    public void setmListener(OpenChatroomAndWSListener listener){
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mUserToken = bundle.getString(USER_TOKEN_KEY);
        MessageBus.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom_list, parent, false);

        new GetAPIToken().execute("http://utubeu.herokuapp.com/api/convert-token/google-oauth2", "GET", mUserToken);
        new GetChatrooms().execute("ownedchatrooms", mAPItoken);
        //Makeownedbuttons method is called in onpostexecute of GetChatrooms
        mOwnedChatroomDock = (LinearLayout) view.findViewById(R.id.chatrooms_dock);



        return view;
    }


    private class GetAPIToken extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... args) {
            String responseString = null;


            try {

                String[] client_and_secret = getKeysandTokens();
                String the_url = String.format(args[0] + "?client_id=%s&client_secret=%s&token=%s",
                        client_and_secret[0], client_and_secret[1], args[2]);


                URL url = new URL(the_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(args[1]);

                int status = connection.getResponseCode();
                Log.i("Http Code", Integer.toString(status));
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(streamReader);


                responseString = bufferedReader.readLine();


            } catch (Exception e) {
                Log.e("Error", "Error fetching chatroom data ", e);
            }
            return responseString;
        }

        @Override
        public void onPostExecute(String response) {
            if (response != null) {
                try {


                    JSONObject jsonObject = new JSONObject(response);


                    mAPItoken = jsonObject.getString("access_token");


                } catch (Exception e) {
                    Log.e("GETTOKEN__", e.toString());
                    mAPItoken = null;
                }
            }
        }
    }

    private class GetChatrooms extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... args) {
            String base_url = "http://utubeu.herokuapp.com/api/";
            String url_string = base_url + args[0];

            String responseString = null;


            try {

                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("Authorization", "Bearer " + mAPItoken);
                int status = connection.getResponseCode();

                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                responseString = bufferedReader.readLine();


            } catch (Exception e) {
                Log.e("Error", "Error fetching chatroom data ", e);
            }
            return responseString;


        }

        @Override
        public void onPostExecute(String response) {

            Log.i("ChatroomsJSON ", response);
            ObjectMapper mapper = new ObjectMapper();
            try {
                mOwnedChatrooms = mapper.readValue(response, mapper.getTypeFactory()
                        .constructCollectionType(ArrayList.class, Chatroom.class));
                Log.i("OWNEDCHATROOMS:", Integer.toString(mOwnedChatrooms.size()));
                makeOwnedButtons();

            } catch (Exception e) {
                Log.e("ParseChatrooms", e.toString());
            }
        }
    }

    private void makeOwnedButtons() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText("Owned Chatrooms");
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);

        for (final Chatroom chatroom : mOwnedChatrooms) {

            Log.i("CHATROOMNAME:::", chatroom.getname());
            Button button = new Button(getActivity());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatFragment chatFragment = new ChatFragment();
                    mListener.openChatroom(chatroom);



                }
            });


            linearLayout.addView(button);
        }
        mOwnedChatroomDock.addView(linearLayout);
    }


    private String[] getKeysandTokens() {
        String[] keys = new String[24];
        InputStream keyStream = getResources().openRawResource(R.raw.tokens);
        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
        try {
            int i = 0;
            while (i < 2) {
                keys[i] = keyStreamReader.readLine();
                i++;
            }
            return keys;
        } catch (IOException e) {
            Log.e("Error", "Error reading secret key from raw resource file");
            return null;
        }
    }



}