package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by boydjohnson on 12/7/15.
 */
public class Chatroom {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;


    public Integer getid() {
        return id;
    }

    public String getname() {
        return name;
    }


    public String getdescription() {
        return description;
    }
}
