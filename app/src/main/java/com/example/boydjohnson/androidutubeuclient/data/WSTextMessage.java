package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class WSTextMessage {

    @JsonProperty("username")
    private String username;

    @JsonProperty("message")
    private String message;

    @JsonProperty("youtube_value")
    private String youtube_value;

    @JsonProperty("last_ten")
    private ArrayList<String> lastTenMessages;

    @JsonProperty("usernames")
    private ArrayList<String> usernamesInChatroom;

    @JsonProperty("start")
    private Boolean start;

    @JsonProperty("percentage")
    private Float percentage;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_url")
    private String image_url;

    @JsonProperty("suggestion_list")
    private ArrayList<String> suggestions_list;

    public ArrayList<String> getSuggestions_list() {
        return suggestions_list;
    }

    public String getUsername() {
        return username;
    }

    public Float getPercentage() {
        return percentage;
    }

    public String getMessage() {
        return message;
    }

    public String getYoutube_value() {
        return youtube_value;
    }

    public ArrayList<String> getLastTenMessages() {
        return lastTenMessages;
    }

    public ArrayList<String> getUsernamesInChatroom() {
        return usernamesInChatroom;
    }

    public Boolean getStart() {
        return start;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDescription() {
        return description;
    }
}
