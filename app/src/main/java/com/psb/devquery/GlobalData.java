package com.psb.devquery;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.psb.devquery.model.Post;

import java.util.ArrayList;

public class GlobalData extends Application {
    public static final String TAG = GlobalData.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static GlobalData mInstance;

    public String userName = "";
    public String pass = "";
    public String firstName = "";
    public String lastName = "";
    public String dateOfJoin = "";
    public String email = "";
    public String profileTag = "";
    public boolean onRestartFlg = false, firstLoad = true;
    public String lastFragment = "";
    public ArrayList<Post> post = new ArrayList<Post>();


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized GlobalData getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
