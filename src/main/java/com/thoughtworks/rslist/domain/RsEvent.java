package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class RsEvent {
    private String eventName;
    private String keyWord;
    @NotNull
    private int userId;
    private int voteNum = 0;

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public String getEventName() {
        return eventName;
    }

    public RsEvent() {
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public RsEvent(String eventName, String keyWord, int userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
    }

    //@JsonIgnore
    public int getUserId() {
        return userId;
    }

    //@JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
