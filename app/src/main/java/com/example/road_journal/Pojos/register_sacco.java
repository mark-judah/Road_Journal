package com.example.road_journal.Pojos;

public class register_sacco {
    public String sacco_email;
    public String sacco_name;
    public String sacco_pass;
    public String sacco_phonenumber;

    public register_sacco() {
    }

    public register_sacco(String str, String str2, String str3, String str4) {
        this.sacco_name = str;
        this.sacco_email = str2;
        this.sacco_phonenumber = str3;
        this.sacco_pass = str4;
    }

    public String getSacco_name() {
        return this.sacco_name;
    }

    public String getSacco_email() {
        return this.sacco_email;
    }

    public String getSacco_phonenumber() {
        return this.sacco_phonenumber;
    }

    public String getSacco_pass() {
        return this.sacco_pass;
    }
}
