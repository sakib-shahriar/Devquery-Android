package com.psb.devquery.model;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Notification implements Serializable {

    private String userName, notification, profileImg, time, postId, isRead, notfId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Notification(String userName, String notification, String profileImg, String time, String postId, String isRead, String notfId) {
        this.userName = userName;
        this.notification = notification;
        this.profileImg = profileImg;
        this.time = time;
        this.postId = postId;
        this.isRead = isRead;
        this.notfId = notfId;


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

    public String getUserName() {
        return userName;
    }

    public String getNotification() {
        return notification;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getTime() {
        return time;
    }

    public String getPostId() {
        return postId;
    }

    public String getIsRead() {
        return isRead;
    }

    public String getNotfId() {
        return notfId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public void setNotfId(String notfId) {
        this.notfId = notfId;
    }
}
