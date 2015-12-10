package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by boydjohnson on 12/1/15.
 */
public class TextMessageOut {
    @JsonProperty("chatroom_id")
    private Integer chatroomID;
    @JsonProperty("username")
    private String username;
    @JsonProperty("message")
    private String message;


    public TextMessageOut(){
    }

    public TextMessageOut(Integer chatroomID, String username, String message) {
        this.chatroomID = chatroomID;
        this.username = username;
        this.message = message;
    }

    public Integer getChatroomID() {
        return chatroomID;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

}
