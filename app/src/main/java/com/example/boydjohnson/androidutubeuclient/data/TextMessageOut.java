package com.example.boydjohnson.androidutubeuclient.data;

/**
 * Created by boydjohnson on 12/1/15.
 */
public class TextMessageOut {
    private Integer chatroomID;
    private String username;
    private String message;


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
