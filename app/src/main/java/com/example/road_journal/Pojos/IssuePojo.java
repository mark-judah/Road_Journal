package com.example.road_journal.Pojos;

public class IssuePojo {
    public String email;
    public String issue;
    public String message;
    public String name;
    public String phone;
    public String sacconame;
    public String uid;

    public IssuePojo(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.name = str;
        this.email = str2;
        this.phone = str3;
        this.message = str4;
        this.sacconame = str5;
        this.issue = str6;
        this.uid = str7;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getSacconame() {
        return this.sacconame;
    }

    public String getMessage() {
        return this.message;
    }

    public String getIssue() {
        return this.issue;
    }

    public String getUid() {
        return this.uid;
    }
}
