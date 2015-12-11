package com.example.boydjohnson.androidutubeuclient.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by boydjohnson on 12/11/15.
 */
public class VoteOut {

    @JsonProperty("youtube_value")
    private String youtube_value;

    @JsonProperty("chatroom_id")
    private Integer chatroom_id;

    @JsonProperty("vote")
    private Boolean vote;

    public VoteOut(String youtube_value, Integer chatroom_id, Boolean vote) {
        this.youtube_value = youtube_value;
        this.chatroom_id = chatroom_id;
        this.vote = vote;
    }

    public String getYoutube_value() {
        return youtube_value;
    }

    public Integer getChatroom_id() {
        return chatroom_id;
    }

    public Boolean getVote() {
        return vote;
    }
}
