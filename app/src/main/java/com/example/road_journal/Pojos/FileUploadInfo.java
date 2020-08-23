package com.example.road_journal.Pojos;

import java.util.Date;

public class FileUploadInfo {
    public static final int IMAGE_TYPE = 1;
    public static final int TEXT_TYPE = 0;
    public String city;
    public Date date;
    public String imageName;
    public String imageURL;
    public String incident;
    public String key;
    public String location;
    public String originalPostUser;
    public String original_key;
    public String profileurl;
    public String road;
    public String text;
    public Integer type;
    public String userId;
    public String usernam;
    public String users_email;

    public FileUploadInfo() {
    }

    public FileUploadInfo(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, Integer num, Date date2, String str12, String str13, String str14) {
        this.userId = str;
        this.city = str12;
        this.imageName = str2;
        this.profileurl = str3;
        this.imageURL = str4;
        this.incident = str5;
        this.users_email = str6;
        this.usernam = str7;
        this.location = str8;
        this.text = str9;
        this.road = str10;
        this.originalPostUser = str11;
        this.type = num;
        this.date = date2;
        this.key = str13;
        this.original_key = str14;
    }

    public String getImageName() {
        return this.imageName;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public String getIncident() {
        return this.incident;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUsers_email() {
        return this.users_email;
    }

    public String getUsernam() {
        return this.usernam;
    }

    public String getLocation() {
        return this.location;
    }

    public String getProfileurl() {
        return this.profileurl;
    }

    public String getText() {
        return this.text;
    }

    public String getRoad() {
        return this.road;
    }

    public String getOriginalPostUser() {
        return this.originalPostUser;
    }

    public Integer getType() {
        return this.type;
    }

    public Date getDate() {
        return this.date;
    }

    public String getCity() {
        return this.city;
    }

    public String getKey() {
        return this.key;
    }

    public String getOriginal_key() {
        return this.original_key;
    }
}
