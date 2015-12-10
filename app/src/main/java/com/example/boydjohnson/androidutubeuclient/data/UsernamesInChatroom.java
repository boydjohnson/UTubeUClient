package com.example.boydjohnson.androidutubeuclient.data;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class UsernamesInChatroom {

    private ArrayList<String> usernames;

    public UsernamesInChatroom(ArrayList<String> usernames) {
        this.usernames = usernames;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }
}
