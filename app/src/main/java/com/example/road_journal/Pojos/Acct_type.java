package com.example.road_journal.Pojos;

public class Acct_type {
    public String email;
    public String type;
    public String uid;
    public String user_name;

    public Acct_type() {
    }

    public Acct_type(String str, String str2, String str3, String str4) {
        this.user_name = str;
        this.email = str2;
        this.uid = str3;
        this.type = str4;
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

    public String getType() {
        return this.type;
    }
}
