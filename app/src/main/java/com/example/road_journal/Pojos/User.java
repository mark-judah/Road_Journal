package com.example.road_journal.Pojos;

public class User {
    public String email;
    public String profileurl;
    public String uid;
    public String user_name;

    public User() {
    }

    public User(String str, String str2, String str3, String str4) {
        this.user_name = str;
        this.email = str2;
        this.uid = str3;
        this.profileurl = str4;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUid() {
        return this.uid;
    }

    public String getProfileurl() {
        return this.profileurl;
    }

    public void setUser_name(String str) {
        this.user_name = str;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public void setProfileurl(String str) {
        this.profileurl = str;
    }
}
