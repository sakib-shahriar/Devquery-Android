package com.psb.devquery.model;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Post implements Serializable {

    private String postOwner,time,location,description,postImg, profileImg,plusVote,minusVote,comment,img,postId,isLikedByMe,isDisLikedByMe;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Post(String postOwner, String time, String location, String description, String postImg, String profileImg, String plusVote,
                String minusVote, String comment, String img, String postId, String isLikedByMe, String isDisLikedByMe) {
        this.postOwner = postOwner;
        this.time = time;
        this.location = location;
        this.description = description;
        this.postImg = postImg;
        this.profileImg = profileImg;
        this.plusVote = plusVote;
        this.minusVote = minusVote;
        this.comment = comment;
        this.img = img;
        this.postId = postId;
        this.isLikedByMe = isLikedByMe;
        this.isDisLikedByMe = isDisLikedByMe;



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null,date2 = null,date3=null;
        try {
            date1 = simpleDateFormat.parse(this.time);
            date2 = Calendar.getInstance().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000*60*60*24));
        int months = days / 30;
        int hours = (int)difference/(1000 * 60 * 60);
        hours = hours - (days * 24);
        int min = (int)(difference/(1000*60)) % 60;

        if(months > 0) this.time = months+" month ago";
        else if(days > 0) this.time = days+" day ago";
        else if(hours > 0) this.time = hours+" hour ago";
        else if(min > 0) this.time = min+" minute ago";
        else this.time = "0 minute ago";

    }

    public String getImg() {
        return img;
    }

    public String getPostOwner() {
        return postOwner;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getPostImg() {
        return postImg;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getPlusVote() {
        return plusVote;
    }

    public String getMinusVote() {
        return minusVote;
    }

    public String getComment() {
        return comment;
    }

    public String getPostId() {
        return postId;
    }

    public String getIsLikedByMe() {
        return isLikedByMe;
    }

    public String getIsDisLikedByMe() {
        return isDisLikedByMe;
    }

    public void setPostOwner(String postOwner) {
        this.postOwner = postOwner;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setPlusVote(String plusVote) {
        this.plusVote = plusVote;
    }

    public void setMinusVote(String minusVote) {
        this.minusVote = minusVote;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setIsLikedByMe(String isLikedByMe) {
        this.isLikedByMe = isLikedByMe;
    }

    public void setIsDisLikedByMe(String isDisLikedByMe) {
        this.isDisLikedByMe = isDisLikedByMe;
    }
}
