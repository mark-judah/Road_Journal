package com.example.road_journal.Pojos;

import java.util.Date;

public class travel_pojo {
    String key;
    public String story;
    public Date time;
    public String title;
    public int type;
    public String uid;
    public String url;
    public String username;

    public travel_pojo() {
    }

    public travel_pojo(String str, String str2, String str3, Date date, String str4, int i, String str5, String str6) {
        this.title = str;
        this.story = str2;
        this.url = str3;
        this.time = date;
        this.key = str4;
        this.type = i;
        this.username = str5;
        this.uid = str6;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStory() {
        return this.story;
    }

    public Date getTime() {
        return this.time;
    }

    public String getUrl() {
        return this.url;
    }

    public String getKey() {
        return this.key;
    }

    public int getType() {
        return this.type;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUid() {
        return this.uid;
    }
}
