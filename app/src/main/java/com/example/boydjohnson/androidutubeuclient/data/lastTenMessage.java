package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class lastTenMessage {
    @JsonProperty("username")
    private String username;

    @JsonProperty("msg")
    private String message;

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
