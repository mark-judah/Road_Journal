package com.example.road_journal.Pojos;

import java.util.Date;

public class Comments {
    public String comment;
    public String commentkey;
    public Date date;
    public String key;
    public String replyprofilepic;
    public String replyusername;
    public String uid;
    public String uploadusername;

    public Comments() {
    }

    public Comments(String str, String str2, String str3, String str4, String str5, String str6, Date date2, String str7) {
        this.replyusername = str;
        this.uploadusername = str2;
        this.replyprofilepic = str3;
        this.comment = str4;
        this.key = str5;
        this.uid = str6;
        this.date = date2;
        this.commentkey = str7;
    }

    public String getReplyusername() {
        return this.replyusername;
    }

    public String getUploadusername() {
        return this.uploadusername;
    }

    public String getReplyprofilepic() {
        return this.replyprofilepic;
    }

    public String getComment() {
        return this.comment;
    }

    public String getKey() {
        return this.key;
    }

    public String getUid() {
        return this.uid;
    }

    public Date getDate() {
        return this.date;
    }

    public String getCommentkey() {
        return this.commentkey;
    }
}
