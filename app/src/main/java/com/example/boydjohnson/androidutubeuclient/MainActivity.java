package com.example.boydjohnson.androidutubeuclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.boydjohnson.androidutubeuclient.adapters.ChatroomViewAdapter;
import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.Chatroom;
import com.example.boydjohnson.androidutubeuclient.data.LastTen;
import com.example.boydjohnson.androidutubeuclient.data.Start;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionIn;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionList;
import com.example.boydjohnson.androidutubeuclient.data.SuggestionOut;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageIn;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageOut;
import com.example.boydjohnson.androidutubeuclient.data.UsernamesInChatroom;
import com.example.boydjohnson.androidutubeuclient.data.VoteIn;
import com.example.boydjohnson.androidutubeuclient.data.VoteOut;
import com.example.boydjohnson.androidutubeuclient.data.WSTextMessage;
import com.example.boydjohnson.androidutubeuclient.fragments.ChatroomListFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

/*
https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/SignInActivity.java
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        ChatroomListFragment.OpenChatroomAndWSListener,
        ChatroomListFragment.GetBackInternalAPIKey {

    private GoogleApiClient mGoogleApiClient;

    private WebSocketConnection mConnection = new WebSocketConnection();

    private FragmentManager mFragmentManager;

    private String mUsername;
    private String mServerAuthToken;
    private String mInternalAPIKey;

    private static Bus mMessageBus;

    final Integer RESULT_CODE_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ARRRGGG Google Youtube api problems https://code.google.com/p/gdata-issues/issues/detail?id=7688
        getLayoutInflater().setFactory(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFragmentManager = getSupportFragmentManager();

        if(mUsername!=null && mServerAuthToken!=null){
            getChatrooms();
        }else {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestServerAuthCode(get_google_client_id())
                    .requestEmail().build();

            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            findViewById(R.id.sign_in_button).setOnClickListener(this);
            findViewById(R.id.sign_out_button).setOnClickListener(this);

            SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setScopes(gso.getScopeArray());
        }
        mMessageBus = MessageBus.getInstance();
        mMessageBus.register(this);


        }


    @Override
    public void onStart(){
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("UTUBEU__ACTIVITY", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    public void handleSignInResult(GoogleSignInResult result){
        mUsername = result.getSignInAccount().getEmail().split("@")[0];
        mServerAuthToken = result.getSignInAccount().getServerAuthCode();

        getChatrooms();
    }

    private void getChatrooms(){

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        setContentView(R.layout.fragment_container);
        ChatroomListFragment fragment = new ChatroomListFragment();
        fragment.setmListener(this);
        fragment.setmListener2(this);
        Bundle b = new Bundle();
        b.putString(ChatroomListFragment.USER_TOKEN_KEY, mServerAuthToken);
        fragment.setArguments(b);

        ft.add(R.id.container_for_fragments, fragment, "CHATROOMS").addToBackStack("CHATROOMS").commit();

    }

    private void getChatroomsOnBackpressed(){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        setContentView(R.layout.fragment_container);
        ChatroomListFragment fragment = new ChatroomListFragment();
        fragment.setmListener2(this);
        fragment.setmListener(this);
        Bundle b = new Bundle();
        b.putString(ChatroomListFragment.INTERNAL_API_TOKEN_KEY, mInternalAPIKey);
        fragment.setArguments(b);

        ft.replace(R.id.container_for_fragments, fragment, "CHATROOMS").addToBackStack("CHATROOMS").commit();

    }

    public void startWS(Integer chatroom_id, String username){
        String raw_WS_URI = "ws://utubeu.herokuapp.com/ws?chatroom-id=%d&user-name=%s";
        String ws_url = String.format(raw_WS_URI, chatroom_id, username);

        try {
            mConnection.connect(ws_url, new UTubeUWSHandler());
        }catch(WebSocketException wse) {
            Log.e("WEBSOCKETS", wse.toString());

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        Log.i("GOOGLE PLAY", "YOU don't have google play");
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RESULT_CODE_SIGN_IN);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                mConnection.disconnect();
                break;
            default:
                Log.i("Activity", "What just happened?");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("$$$$$$$", Integer.toString(resultCode));
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RESULT_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }
    }

    private String get_google_client_id(){
        String key = null;
        InputStream keyStream = getResources().openRawResource(R.raw.google_client_id);
        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
        try{
                key = keyStreamReader.readLine();


            return key;
        } catch (IOException e){
            Log.e("Error","Error reading google_client_id from raw resource file");
            return null;
        }
    }

    @Override
    public void openChatroom(Chatroom chatroom){
        startWS(chatroom.getid(), mUsername);
        setContentView(R.layout.viewpager_container);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);
        ChatroomViewAdapter adapter = new ChatroomViewAdapter(mFragmentManager, chatroom.getid(), mUsername);
        pager.setAdapter(adapter);
    }

    @Override
    public void getInternalApiKey(String apiKey){
        mInternalAPIKey = apiKey;
    }

    @Override
    public void onBackPressed(){
        Log.i("BACKPRESSED::", Integer.toString(mFragmentManager.getBackStackEntryCount()));


        if(mConnection.isConnected()){
            //Seems like hacky way to flush out all the fragments
            for(Fragment f:mFragmentManager.getFragments()){
                mFragmentManager.beginTransaction().remove(f).commit();
            }
            mConnection.disconnect();
            getChatroomsOnBackpressed();

        }else{
            super.onBackPressed();
        }
    }

    @Subscribe
    public void getVoteFromSuggestionListFragment(VoteOut vote){
        ObjectMapper mapper = new ObjectMapper();
        try{
            mConnection.sendTextMessage(mapper.writeValueAsString(vote));
        }catch(Exception e){
            Log.e("VOTEOUTOUT::", e.toString());
        }
    }

    @Subscribe
    public void getTextMessageOut(TextMessageOut textMessageOut){
        ObjectMapper mapper = new ObjectMapper();
        try{
            mConnection.sendTextMessage(mapper.writeValueAsString(textMessageOut));
        }catch (Exception e){
            Log.e("MESSAGEOUT", e.toString());
        }
    }

    @Subscribe
    public void getSuggestionOut(SuggestionOut suggestionOut){
        ObjectMapper mapper = new ObjectMapper();
        try{
            mConnection.sendTextMessage(mapper.writeValueAsString(suggestionOut));
        }catch (Exception e){
            Log.e("SuggestionOUt", e.toString());
        }
    }


    private class UTubeUWSHandler extends WebSocketConnectionHandler{

        @Override
        public void onTextMessage(String payload) {
            ObjectMapper mapper = new ObjectMapper();
            WSTextMessage baseMessage = null;
            try {
                 baseMessage = mapper.readValue(payload, WSTextMessage.class);
            }catch(Exception e){
                Log.i("WSHandler", e.toString());
            }
            if(baseMessage!=null){
                if(baseMessage.getUsernamesInChatroom()!=null){
                    UsernamesInChatroom usernames = new UsernamesInChatroom(baseMessage.getUsernamesInChatroom());
                    mMessageBus.post(usernames);
                }

                if(baseMessage.getLastTenMessages()!=null){
                    LastTen lastTen = new LastTen(baseMessage.getLastTenMessages());
                    mMessageBus.post(lastTen);

                }
                if(baseMessage.getMessage()!=null){
                    TextMessageIn textMessage = new TextMessageIn(baseMessage.getUsername(), baseMessage.getMessage());
                    mMessageBus.post(textMessage);
                }
                if(baseMessage.getPercentage()!=null){
                    VoteIn voteIn = new VoteIn(baseMessage.getYoutube_value(), baseMessage.getPercentage());
                    mMessageBus.post(voteIn);
                }
                if(baseMessage.getStart()!=null){
                    Start start = new Start(baseMessage.getYoutube_value());
                    mMessageBus.post(start);
                }
                if(baseMessage.getSuggestions_list()!=null){
                    SuggestionList list = new SuggestionList(baseMessage.getSuggestions_list());
                    mMessageBus.post(list);
                }
                if(baseMessage.getYoutube_value()!=null&&baseMessage.getPercentage()==null
                        &&baseMessage.getStart()==null){
                    SuggestionIn suggestionIn = new SuggestionIn(baseMessage.getYoutube_value(),
                            baseMessage.getTitle(), baseMessage.getDescription(), baseMessage.getImage_url(), null);
                    mMessageBus.post(suggestionIn);
                }
            }
        }
    }

}
