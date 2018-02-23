package com.example.dell.mymediaplay.tool;

import java.io.Serializable;

/**
 * Created by DELL on 2016/5/22.
 */
public class MusicInfo implements Serializable {

    private long id, duration, size;
    private String title, artist, url;

    public MusicInfo() {
        super();
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
