package com.thoughtworks.rslist.domain;

public class RsEvent {
    public String eventName;
    public String keyWord;

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

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

}
