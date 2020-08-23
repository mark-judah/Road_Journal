package com.example.road_journal.Pojos;

public class SaccoPOJO {
    public String sacco_email;
    public String sacco_name;
    public String sacco_phonenumber;

    public SaccoPOJO() {
    }

    public SaccoPOJO(String str, String str2, String str3) {
        this.sacco_name = str;
        this.sacco_email = str2;
        this.sacco_phonenumber = str3;
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
}
