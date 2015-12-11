package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by boydjohnson on 12/11/15.
 */
public class Suggestion {

    @JsonProperty("chatroom_id")
    private String chatroom_id;

    @JsonProperty("youtube_value")
    private String youtube_value;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_url")
    private String image_url;

    @JsonProperty("username")
    private String username;

    public Suggestion(String chatroom_id, String youtube_value, String title, String description, String image_url, String username) {
        this.chatroom_id = chatroom_id;
        this.youtube_value = youtube_value;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.username = username;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public String getYoutube_value() {
        return youtube_value;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }
}