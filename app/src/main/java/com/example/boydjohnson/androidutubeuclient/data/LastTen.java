package com.example.boydjohnson.androidutubeuclient.data;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class LastTen {
    private ArrayList<String> lastTenMessages;

    public LastTen(ArrayList<String> lastTenMessages) {
        this.lastTenMessages = lastTenMessages;
    }

    public ArrayList<String> getLastTenMessages() {
        return lastTenMessages;
    }
}
