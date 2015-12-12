package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageOut;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class YoutubeSearchFragment extends Fragment {

    private YouTube youtube;
    private YouTube.Search.List query;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        youtube = new YouTube.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        }).setApplicationName(getResources().getString(R.string.app_name)).build();

        try {
            query = youtube.search().list("id,snippet");
            query.setKey(getYoutubeAPIkey());
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        } catch (IOException ioe) {
            Log.e("YTSFragment", ioe.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, parent, false);
        EditText textInputBox = (EditText)view.findViewById(R.id.youtube_search);
        textInputBox.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("KEYPRESSED", Integer.toString(actionId));

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText editText = (EditText) v;
                    if (!editText.getText().toString().equals("")) {

                    } else {
                        Toast.makeText(getActivity(), "No text entered!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        return view;
    }


    private String getYoutubeAPIkey() {
        String key = null;
        InputStream keyStream = getResources().openRawResource(R.raw.youtube_api_key);
        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
        try {
            key = keyStreamReader.readLine();


            return key;
        } catch (IOException e) {
            Log.e("Error", "Error reading youtube_api key from raw resource file", e);
            return null;
        }
    }
}
