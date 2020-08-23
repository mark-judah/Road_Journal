package com.example.road_journal.Activities;

public class Acct_type {
    public String email;
    public String type;
    public String uid;
    public String user_name;

    public Acct_type() {
    }

    public Acct_type(String str, String str2, String str3, String str4) {
        user_name = str;
        email = str2;
        uid = str3;
        type = str4;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getType() {
        return type;
    }
}
