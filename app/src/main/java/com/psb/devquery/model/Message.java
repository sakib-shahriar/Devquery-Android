package com.psb.devquery.model;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Message implements Serializable {

    public String message,userName,time;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Message(String message, String userName, String time) {
        this.message = message;
        this.userName = userName;
        this.time = time;

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

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
