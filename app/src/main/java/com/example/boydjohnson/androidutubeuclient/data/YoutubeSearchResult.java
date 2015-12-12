package com.example.boydjohnson.androidutubeuclient.data;

/**
 * Created by boydjohnson on 12/12/15.
 */
public class YoutubeSearchResult {
    private String title;
    private String description;
    private String thumbnailURL;
    private String id;

    public YoutubeSearchResult(String title, String description, String thumbnailURL, String id) {
        this.title = title;
        this.description = description;
        this.thumbnailURL = thumbnailURL;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

}
