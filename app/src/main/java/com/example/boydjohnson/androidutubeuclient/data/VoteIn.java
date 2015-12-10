package com.example.boydjohnson.androidutubeuclient.data;

/**
 * Created by boydjohnson on 12/10/15.
 */
public class VoteIn {
    private String youtube_value;
    private Float percentage;

    public VoteIn(String youtube_value, Float percentage) {
        this.youtube_value = youtube_value;
        this.percentage = percentage;
    }

    public String getYoutube_value() {
        return youtube_value;
    }

    public Float getPercentage() {
        return percentage;
    }
}
