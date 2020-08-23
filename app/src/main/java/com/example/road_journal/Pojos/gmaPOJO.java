package com.example.road_journal.Pojos;

import java.util.Date;

public class gmaPOJO {
    public static final int IMAGE_TYPE = 1;
    public static final int TEXT_TYPE = 0;
    String comment;
    Date date;
    String downloadurl;
    String email;
    String incidentlocation;
    String repostusername;
    public Integer type;
    String uid;
    String url;
    String username;

    public gmaPOJO() {
    }

    public gmaPOJO(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, Integer num, Date date2) {
        this.uid = str;
        this.comment = str2;
        this.url = str3;
        this.downloadurl = str4;
        this.email = str5;
        this.username = str6;
        this.repostusername = str7;
        this.incidentlocation = str8;
        this.type = num;
        this.date = date2;
    }

    public String getUid() {
        return this.uid;
    }

    public String getComment() {
        return this.comment;
    }

    public String getUrl() {
        return this.url;
    }

    public String getDownloadurl() {
        return this.downloadurl;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getRepostusername() {
        return this.repostusername;
    }

    public String getIncidentlocation() {
        return this.incidentlocation;
    }

    public Integer getType() {
        return this.type;
    }

    public Date getDate() {
        return this.date;
    }
}
