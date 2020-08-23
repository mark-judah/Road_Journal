package com.example.road_journal.Pojos;

public class SaccoPortalPojo {
    public int image;
    public String saconame;
    public String title;
    public String value;

    public SaccoPortalPojo(String str, int i, String str2, String str3) {
        this.value = str;
        this.title = str2;
        this.image = i;
        this.saconame = str3;
    }

    public String getValue() {
        return this.value;
    }

    public String getTitle() {
        return this.title;
    }

    public int getImage() {
        return this.image;
    }

    public String getSaconame() {
        return this.saconame;
    }
}
