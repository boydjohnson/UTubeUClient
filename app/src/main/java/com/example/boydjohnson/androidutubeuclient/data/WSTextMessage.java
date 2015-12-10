package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class WSTextMessage {

    @JsonProperty("chatroom_id")
    private Integer chatroom_id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("message")
    private String message;

    @JsonProperty("youtube_value")
    private String youtube_value;

    @JsonProperty("last_ten")
    private ArrayList<lastTenMessage> lastTenMessages;

    @JsonProperty("usernames")
    private ArrayList<String> usernamesInChatroom;

    @JsonProperty("start")
    private Boolean start;

    public Integer getChatroom_id() {
        return chatroom_id;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getYoutube_value() {
        return youtube_value;
    }

    public ArrayList<lastTenMessage> getLastTenMessages() {
        return lastTenMessages;
    }

    public ArrayList<String> getUsernamesInChatroom() {
        return usernamesInChatroom;
    }

    public Boolean getStart() {
        return start;
    }
}
