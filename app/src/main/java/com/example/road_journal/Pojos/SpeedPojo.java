package com.example.road_journal.Pojos;

public class SpeedPojo {
    public String comment;
    public String cordinates;
    public String email;
    public String key;
    public String name;
    public String numberplate;
    public String roadname;
    public String speed;
    public String time;

    public SpeedPojo() {
    }

    public SpeedPojo(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        this.name = str;
        this.email = str2;
        this.numberplate = str3;
        this.roadname = str4;
        this.comment = str5;
        this.speed = str6;
        this.cordinates = str7;
        this.key = str8;
        this.time = str9;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNumberplate() {
        return this.numberplate;
    }

    public String getRoadname() {
        return this.roadname;
    }

    public String getComment() {
        return this.comment;
    }

    public String getSpeed() {
        return this.speed;
    }

    public String getCordinates() {
        return this.cordinates;
    }

    public String getKey() {
        return this.key;
    }

    public String getTime() {
        return this.time;
    }
}
