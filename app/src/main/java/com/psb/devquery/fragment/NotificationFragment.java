package com.psb.devquery.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.psb.devquery.GlobalData;
import com.psb.devquery.PostActivity;
import com.psb.devquery.R;
import com.psb.devquery.adapter.NotificationAdapter;
import com.psb.devquery.adapter.PostAdapter;
import com.psb.devquery.model.Notification;
import com.psb.devquery.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    RecyclerView rcn;
    NotificationAdapter notificationAdapter;
    public static ArrayList<Notification> notifications = new ArrayList<Notification>();

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v= inflater.inflate(R.layout.fragment_notification, container, false);

        rcn = v.findViewById(R.id.rcnotf);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcn.setLayoutManager(layoutManager);
        rcn.setItemAnimator(new DefaultItemAnimator());
        rcn.addItemDecoration(new DividerItemDecoration(rcn.getContext(), DividerItemDecoration.VERTICAL));

        parseNotification();
        return v;
    }

    private void parseNotification() {
        notifications.clear();
        String url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&action=getnotifications";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Notification tempNotification = new Notification(object.getString("userName"),object.getString("notification"),object.getString("profileImg"),
                                object.getString("time"),object.getString("postId"),object.getString("isRead"),object.getString("notfId"));
                        notifications.add(tempNotification);

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                Collections.reverse(notifications);
                load();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        });
        GlobalData.getInstance().addToRequestQueue(request);
    }

    void load(){
        notificationAdapter = new NotificationAdapter(notifications,getActivity());
        notificationAdapter.setOnInfo(new NotificationAdapter.NotificationInfoListener() {
            @Override
            public void onInfo(Notification notification) {
                String postId = notification.getPostId();
                ArrayList<Post> posts = new ArrayList<Post>();
                posts = GlobalData.getInstance().post;
                Post targetPost = null;
                for(int i = 0; i < posts.size(); i++){
                    if(posts.get(i).getPostId().equals(postId)){
                        targetPost = posts.get(i);
                        break;
                    }
                }



                String url = "https://skbshariar.000webhostapp.com/api2.php?notfId="+notification.getNotfId()+"&action=notf";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"something went error",Toast.LENGTH_LONG).show();
                    }
                });

                notification.setIsRead("1");

                GlobalData.getInstance().addToRequestQueue(request);

                Intent intent = new Intent(getActivity(),PostActivity.class);
                intent.putExtra("post",targetPost);
                startActivity(intent);

            }
        });
        rcn.setAdapter(notificationAdapter);
        //Toast.makeText(getActivity(),notifications.size()+"success",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationAdapter = new NotificationAdapter(notifications,getActivity());
        rcn.swapAdapter(notificationAdapter,false);
    }
}
