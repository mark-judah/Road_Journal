package com.example.road_journal.Pojos;

public class SaccoResponsePojo {
    public String key;
    public String response;
    public String sacconame;
    public String uid;

    public SaccoResponsePojo() {
    }

    public SaccoResponsePojo(String str, String str2, String str3, String str4) {
        this.sacconame = str;
        this.uid = str2;
        this.response = str3;
        this.key = str4;
    }

    public String getSacconame() {
        return this.sacconame;
    }

    public String getUid() {
        return this.uid;
    }

    public String getResponse() {
        return this.response;
    }

    public String getKey() {
        return this.key;
    }
}
