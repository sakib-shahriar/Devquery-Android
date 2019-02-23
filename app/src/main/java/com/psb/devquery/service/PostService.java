package com.psb.devquery.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.LogRecord;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.psb.devquery.GlobalData;
import com.psb.devquery.MainActivity;
import com.psb.devquery.R;
import com.psb.devquery.adapter.PostAdapter;
import com.psb.devquery.fragment.HomeFragment;
import com.psb.devquery.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostService extends Service {
    public PostService() {
    }
    int cnt = 0;
    Handler handler;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MenuItem menuItem = MainActivity.bnv.getMenu().getItem(0);

                String url = "https://skbshariar.000webhostapp.com/api3.php?user_name="+GlobalData.getInstance().userName;
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Post> post = new ArrayList<Post>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Post tempPost = new Post(object.getString("postOwner"),object.getString("time"),object.getString("location"),object.getString("description"),
                                        object.getString("postImg"),object.getString("profileImg"),object.getString("plusVote"),object.getString("minusVote"),object.getString("comment"),
                                        object.getString("img"),object.getString("postId"),object.getString("isLikedByMe"),object.getString("isDisLikedByMe"));
                                post.add(tempPost);
                                RecyclerView rcv = HomeFragment.rcv;
                                PostAdapter postAdapter = new PostAdapter(post, getApplicationContext());
                                rcv.swapAdapter(postAdapter,false);
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                        Collections.reverse(post);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
                GlobalData.getInstance().addToRequestQueue(request);
                handler.postDelayed(this,60000);

            }
        };

        handler.postDelayed(runnable,60000);



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
