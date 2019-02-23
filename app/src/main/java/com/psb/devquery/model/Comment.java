package com.psb.devquery.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String user, comment,profileImg;

    public Comment(String user, String comment, String profileImg) {
        this.user = user;
        this.comment = comment;
        this.profileImg = profileImg;
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
