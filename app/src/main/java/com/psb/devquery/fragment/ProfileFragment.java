package com.psb.devquery.fragment;


import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.psb.devquery.GlobalData;
import com.psb.devquery.PostActivity;
import com.psb.devquery.R;
import com.psb.devquery.adapter.PostAdapter;
import com.psb.devquery.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileFragment extends Fragment {


    RecyclerView rcv;
    PostAdapter postAdapter;
    public static ArrayList<Post> post = new ArrayList<Post>();

    public ProfileFragment() {
        // Required empty public constructor
    }


    View v;
    String url = "https://skbshariar.000webhostapp.com/api4.php?user_name="+GlobalData.getInstance().userName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_profile, container, false);

        rcv = v.findViewById(R.id.rcvPost);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv.setLayoutManager(layoutManager);
        rcv.setItemAnimator(new DefaultItemAnimator());
        rcv.addItemDecoration(new DividerItemDecoration(rcv.getContext(), DividerItemDecoration.VERTICAL));
        parsePost();
        return v;

    }

    public void parsePost() {
        post.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Post tempPost = new Post(object.getString("postOwner"),object.getString("time"),object.getString("location"),object.getString("description"),
                                object.getString("postImg"),object.getString("profileImg"),object.getString("plusVote"),object.getString("minusVote"),object.getString("comment"),
                                object.getString("img"),object.getString("postId"),object.getString("isLikedByMe"),object.getString("isDisLikedByMe"));
                        post.add(tempPost);
                    } catch (JSONException e) {
                        Toast.makeText(getContext(),"something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                Collections.reverse(post);
                load();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        });
        GlobalData.getInstance().addToRequestQueue(request);

    }

    void load(){
        postAdapter = new PostAdapter(post,getActivity());
        postAdapter.setOnInfo(new PostAdapter.PostInfoListener() {
            @Override
            public void onInfo(Post post) {
                Intent intent = new Intent(getActivity(),PostActivity.class);
                intent.putExtra("post",post);
                startActivity(intent);
            }
        });


        postAdapter.setOnPlusVote(new PostAdapter.PlusVoteListener() {
            @Override
            public void onPlusVote(String postid, int pos) {
                View v = rcv.findViewHolderForAdapterPosition(pos).itemView;
                Button b = (Button) v.findViewById(R.id.plusVote);
                for(int i = 0; i < post.size(); i++){
                    if(post.get(i).getPostId().equals(postid)){
                        if(post.get(i).getIsDisLikedByMe().equals("no") && post.get(i).getIsLikedByMe().equals("no")){
                            post.get(i).setIsLikedByMe("yes");
                            int temp = Integer.parseInt(post.get(i).getPlusVote());
                            temp++;
                            post.get(i).setPlusVote(temp+"");
                            b.setText(temp+"");
                            b.setTextColor(Color.parseColor("#408a25"));
                        }
                        else if(post.get(i).getIsDisLikedByMe().equals("no") && post.get(i).getIsLikedByMe().equals("yes")){
                            post.get(i).setIsLikedByMe("no");
                            int temp = Integer.parseInt(post.get(i).getPlusVote());
                            temp--;
                            post.get(i).setPlusVote(temp+"");
                            b.setText(temp+"");
                            b.setTextColor(Color.parseColor("#000000"));
                        }
                        else if(post.get(i).getIsDisLikedByMe().equals("yes") && post.get(i).getIsLikedByMe().equals("no")){
                            post.get(i).setIsLikedByMe("yes");
                            post.get(i).setIsDisLikedByMe("no");

                            int temp = Integer.parseInt(post.get(i).getPlusVote());
                            temp++;
                            post.get(i).setPlusVote(temp+"");
                            b.setText(temp+"");
                            b.setTextColor(Color.parseColor("#408a25"));

                            temp = Integer.parseInt(post.get(i).getMinusVote());
                            temp--;
                            post.get(i).setMinusVote(temp+"");
                            b = (Button) v.findViewById(R.id.minusVote);
                            b.setText(temp+"");
                            b.setTextColor(Color.parseColor("#000000"));

                        }
                    }
                }



                url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&post_id="+postid+"&action=plusvote";
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

                GlobalData.getInstance().addToRequestQueue(request);
            }
        });


        postAdapter.setOnMinusVote(new PostAdapter.MinusVoteListener() {
            @Override
            public void onMinusVote(String postid, int pos) {
                View v = rcv.findViewHolderForAdapterPosition(pos).itemView;
                Button b = (Button) v.findViewById(R.id.minusVote);

                for(int i = 0; i < post.size(); i++){
                    if(post.get(i).getPostId().equals(postid)){
                        if(post.get(i).getIsDisLikedByMe().equals("no") && post.get(i).getIsLikedByMe().equals("no")){
                            post.get(i).setIsDisLikedByMe("yes");
                            int temp = Integer.parseInt(post.get(i).getMinusVote());
                            temp++;
                            post.get(i).setMinusVote(temp+"");
                            b.setText(temp+"");
                            b.setTextColor(Color.parseColor("#408a25"));
                        }
                        else if(post.get(i).getIsDisLikedByMe().equals("yes") && post.get(i).getIsLikedByMe().equals("no")){
                            post.get(i).setIsDisLikedByMe("no");
                            int temp = Integer.parseInt(post.get(i).getMinusVote());
                            temp--;
                            post.get(i).setMinusVote(temp+"");
                            b.setText(temp+"");
                            b.setTextColor(Color.parseColor("#000000"));
                        }
                        else if(post.get(i).getIsDisLikedByMe().equals("no") && post.get(i).getIsLikedByMe().equals("yes")){
                            post.get(i).setIsLikedByMe("no");
                            post.get(i).setIsDisLikedByMe("yes");

                            int temp = Integer.parseInt(post.get(i).getPlusVote());
                            temp--;
                            post.get(i).setPlusVote(temp+"");
                            Button b1 = (Button) v.findViewById(R.id.plusVote);
                            b1.setTextColor(Color.parseColor("#000000"));
                            b1.setText(temp+"");


                            temp = Integer.parseInt(post.get(i).getMinusVote());
                            temp++;
                            post.get(i).setMinusVote(temp+"");
                            b.setText(temp+"");
                            b.setTextColor(Color.parseColor("#408a25"));
                        }
                    }
                }


                url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&post_id="+postid+"&action=minusvote";
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

                GlobalData.getInstance().addToRequestQueue(request);
            }
        });

        rcv.setAdapter(postAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        postAdapter = new PostAdapter(post,getActivity());
        rcv.swapAdapter(postAdapter,false);
    }
}
