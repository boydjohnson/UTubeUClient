package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/11/15.
 */
public class SuggestionList {

    private ArrayList<String> the_list;

    public SuggestionList(ArrayList<String> the_list) {
        this.the_list = the_list;
    }

    public ArrayList<String> getThe_list() {
        return the_list;
    }
}
