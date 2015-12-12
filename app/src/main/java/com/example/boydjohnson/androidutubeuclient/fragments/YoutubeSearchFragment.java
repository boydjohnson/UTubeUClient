package com.example.boydjohnson.androidutubeuclient.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boydjohnson.androidutubeuclient.R;
import com.example.boydjohnson.androidutubeuclient.adapters.YoutubeSearchAdapter;
import com.example.boydjohnson.androidutubeuclient.data.TextMessageOut;
import com.example.boydjohnson.androidutubeuclient.data.YoutubeSearchResult;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class YoutubeSearchFragment extends Fragment {

    private YouTube youtube;
    private YouTube.Search.List query;
    private Integer mChatroomId;

    private ListView listView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mChatroomId = bundle.getInt(SuggestionsListFragment.CHATROOM_ID_TAG);

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

        listView = (ListView)view.findViewById(R.id.youtube_search_container);

        EditText textInputBox = (EditText)view.findViewById(R.id.youtube_search);




        textInputBox.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("KEYPRESSED", Integer.toString(actionId));

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText editText = (EditText) v;
                    if (!editText.getText().toString().equals("")) {
                       new GetYoutubeResults().execute(editText.getText().toString());

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

    private class GetYoutubeResults extends AsyncTask<String,String,SearchListResponse>{

        @Override
        public SearchListResponse doInBackground(String...args){

            query.setQ(args[0]);
            SearchListResponse response = null;
            try{
                response = query.execute();

            }catch (Exception e){
                Log.e("GETYOUTUBERESULTS", e.toString());
            }
            return response;
    }
        @Override
        public void onPostExecute(SearchListResponse response){
            List<SearchResult> resultList = response.getItems();

            ArrayList<YoutubeSearchResult> items = new ArrayList<>();
            for(SearchResult result:resultList){
                YoutubeSearchResult item = new YoutubeSearchResult(result.getSnippet().getTitle(),
                        result.getSnippet().getDescription(),
                        result.getSnippet().getThumbnails().getDefault().getUrl(),
                        result.getId().getVideoId());
                items.add(item);
                Log.i("WHAT?", item.getTitle());
            }


            if(listView.getAdapter()==null) {
                YoutubeSearchAdapter adapter = new YoutubeSearchAdapter(getActivity(), R.layout.simple_search_list, items, mChatroomId);
                listView.setAdapter(adapter);
            }else{
                YoutubeSearchAdapter adapter = (YoutubeSearchAdapter)listView.getAdapter();
                adapter.clear();
                adapter.addAll(items);
                adapter.notifyDataSetChanged();

            }
        }
    }


    private String getYoutubeAPIkey() {
        String key = null;
        InputStream keyStream = getResources().openRawResource(R.raw.youtube_data_key);
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
