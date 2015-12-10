package com.example.boydjohnson.androidutubeuclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.boydjohnson.androidutubeuclient.bus.MessageBus;
import com.example.boydjohnson.androidutubeuclient.data.Chatroom;
import com.example.boydjohnson.androidutubeuclient.fragments.ChatFragment;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, ChatroomListFragment.OpenChatroomAndWSListener {

    private GoogleApiClient mGoogleApiClient;

    private WebSocketConnection mConnection = new WebSocketConnection();

    private FragmentManager mFragmentManager;

    String mUsername;

    private static Bus mMessageBus;

    final Integer RESULT_CODE_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFragmentManager = getSupportFragmentManager();

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

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        setContentView(R.layout.fragment_container);
        ChatroomListFragment fragment = new ChatroomListFragment();
        fragment.setmListener(this);
        Bundle b = new Bundle();
        b.putString(ChatroomListFragment.USER_TOKEN_KEY, result.getSignInAccount().getServerAuthCode());
        fragment.setArguments(b);

        ft.add(R.id.container_for_fragments, fragment).commit();

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

    }

    private class UTubeUWSHandler extends WebSocketConnectionHandler{

        @Override
        public void onTextMessage(String payload){
            Log.i("WS_MESSAGE", payload);
        }

    }

}
