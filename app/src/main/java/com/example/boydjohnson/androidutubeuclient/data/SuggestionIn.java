package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by boydjohnson on 12/11/15.
 */
public class SuggestionIn {

    @JsonProperty("youtube_value")
    private String youtube_value;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_url")
    private String image_url;


    public SuggestionIn(String youtube_value, String title, String description, String image_url) {
        this.youtube_value = youtube_value;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
    }

    public SuggestionIn(){

    }

    public String getImage_url() {
        return image_url;
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

}
