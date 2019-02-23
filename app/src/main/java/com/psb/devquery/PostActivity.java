package com.psb.devquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.psb.devquery.adapter.CommentAdapter;
import com.psb.devquery.fragment.HomeFragment;
import com.psb.devquery.model.Comment;
import com.psb.devquery.model.Post;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class PostActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout dl;
    NavigationView nv;
    ImageView profileImg,postImg;
    TextView postOwner,time,location,description;
    Button plusVote,minusVote,comment,editBtn;
    ImageButton sendComment;
    EditText makeComment;
    RecyclerView rvc;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments = new ArrayList<Comment>();
    String url;
    boolean onPauseFlg = false;
    Post post;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        rvc = findViewById(R.id.rvc);
        profileImg = findViewById(R.id.profileImg);
        postImg = findViewById(R.id.postImg);
        postOwner = findViewById(R.id.postOwner);
        time = findViewById(R.id.time);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        plusVote = findViewById(R.id.plusVote);
        minusVote = findViewById(R.id.minusVote);
        comment = findViewById(R.id.comment);
        makeComment = findViewById(R.id.etComment);
        sendComment = findViewById(R.id.sendComment);
        editBtn = findViewById(R.id.edit_btn);


        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        dl = (DrawerLayout) findViewById(R.id.dl);
        nv = findViewById(R.id.navigation);
        View headerView = nv.getHeaderView(0);
        TextView temp = headerView.findViewById(R.id.nhUserName);
        temp.setText(GlobalData.getInstance().userName);
        ImageView tempImg = headerView.findViewById(R.id.profileImg);
        Picasso.get().load("https://skbshariar.000webhostapp.com/img/"+GlobalData.getInstance().userName+".jpg").into(tempImg);
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");

        if(post.getIsLikedByMe().equals("yes")) plusVote.setTextColor(Color.parseColor("#408a25"));
        if(post.getIsDisLikedByMe().equals("yes")) minusVote.setTextColor(Color.parseColor("#408a25"));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editPost = new Intent(getApplicationContext(),EditPostActivity.class);
                //System.out.println(post.getPostId());
                editPost.putExtra("post",post);
                editPost.putExtra("postid",post.getPostId());
                editPost.putExtra("description", post.getDescription());
                startActivity(editPost);
                finish();
            }
        });


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.logout){
                    String FILE_NAME = "preferenceFile";
                    SharedPreferences preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userName","");
                    editor.putString("pass","");
                    editor.putString("loggedin","no");
                    editor.commit();
                    GlobalData.getInstance().userName ="";
                    GlobalData.getInstance().pass = "";
                    onPauseFlg = true;
                    GlobalData.getInstance().onRestartFlg = true;
                    Intent goToLogin = new Intent(PostActivity.this,LoginActivity.class);
                    startActivity(goToLogin);

                }
                return false;
            }
        });


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmt = makeComment.getText().toString();
                if(!cmt.equals("")){
                    url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&post_id="+post.getPostId()+"&comment="
                            +cmt+"&action=makecomment";
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"something went error",Toast.LENGTH_LONG).show();
                        }
                    });

                    GlobalData.getInstance().addToRequestQueue(request);
                    String link = "https://skbshariar.000webhostapp.com/img/"+GlobalData.getInstance().userName+".jpg";
                    comments.add(new Comment(GlobalData.getInstance().userName,cmt,link));
                    commentAdapter = new CommentAdapter(comments,getApplicationContext());
                    rvc.swapAdapter(commentAdapter,false);
                    int temp = Integer.parseInt(post.getComment()) + 1;
                    comment.setText(temp+"");
                    makeComment.setText("");
                }
            }
        });

        plusVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = 0;
                for(int i = 0; i < HomeFragment.post.size(); i++) if(HomeFragment.post.get(i).getPostId().equals(post.getPostId())) index = i;

                if(post.getIsDisLikedByMe().equals("no") && post.getIsLikedByMe().equals("no")){
                    HomeFragment.post.get(index).setIsLikedByMe("yes");
                    int temp = Integer.parseInt(post.getPlusVote());
                    temp++;
                    HomeFragment.post.get(index).setPlusVote(temp+"");
                    plusVote.setText(temp+"");
                    plusVote.setTextColor(Color.parseColor("#408a25"));
                }
                else if(post.getIsDisLikedByMe().equals("no") && post.getIsLikedByMe().equals("yes")){
                    HomeFragment.post.get(index).setIsLikedByMe("no");
                    int temp = Integer.parseInt(post.getPlusVote());
                    temp--;
                    plusVote.setText(temp+"");
                    HomeFragment.post.get(index).setPlusVote(temp+"");
                    plusVote.setTextColor(Color.parseColor("#000000"));
                }
                else if(post.getIsDisLikedByMe().equals("yes") && post.getIsLikedByMe().equals("no")){
                    HomeFragment.post.get(index).setIsLikedByMe("yes");
                    HomeFragment.post.get(index).setIsDisLikedByMe("no");
                    int temp = Integer.parseInt(post.getPlusVote());
                    temp++;
                    plusVote.setText(temp+"");
                    HomeFragment.post.get(index).setPlusVote(temp+"");
                    temp = Integer.parseInt(post.getMinusVote());
                    temp--;
                    minusVote.setText(temp+"");
                    HomeFragment.post.get(index).setMinusVote(temp+"");
                    plusVote.setTextColor(Color.parseColor("#408a25"));
                    minusVote.setTextColor(Color.parseColor("#000000"));
                }

                url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&post_id="+post.getPostId()+"&action=plusvote";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"something went error",Toast.LENGTH_LONG).show();
                    }
                });

                GlobalData.getInstance().addToRequestQueue(request);
            }


        });



        minusVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = 0;
                for(int i = 0; i < HomeFragment.post.size(); i++) if(HomeFragment.post.get(i).getPostId().equals(post.getPostId())) index = i;

                if(post.getIsDisLikedByMe().equals("no") && post.getIsLikedByMe().equals("no")){
                    HomeFragment.post.get(index).setIsDisLikedByMe("yes");
                    int temp = Integer.parseInt(post.getMinusVote());
                    temp++;
                    HomeFragment.post.get(index).setMinusVote(temp+"");
                    minusVote.setText(temp+"");
                    minusVote.setTextColor(Color.parseColor("#408a25"));
                }
                else if(post.getIsDisLikedByMe().equals("yes") && post.getIsLikedByMe().equals("no")){
                    HomeFragment.post.get(index).setIsDisLikedByMe("no");
                    int temp = Integer.parseInt(post.getMinusVote());
                    temp--;
                    HomeFragment.post.get(index).setMinusVote(temp+"");
                    minusVote.setText(temp+"");
                    minusVote.setTextColor(Color.parseColor("#000000"));
                }
                else if(post.getIsDisLikedByMe().equals("no") && post.getIsLikedByMe().equals("yes")){
                    HomeFragment.post.get(index).setIsLikedByMe("no");
                    HomeFragment.post.get(index).setIsDisLikedByMe("yes");

                    int temp = Integer.parseInt(post.getPlusVote());
                    temp--;
                    HomeFragment.post.get(index).setPlusVote(temp+"");
                    plusVote.setText(temp+"");


                    temp = Integer.parseInt(post.getMinusVote());
                    temp++;
                    HomeFragment.post.get(index).setMinusVote(temp+"");
                    minusVote.setText(temp+"");
                    plusVote.setTextColor(Color.parseColor("#000000"));
                    minusVote.setTextColor(Color.parseColor("#408a25"));

                }

                url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&post_id="+post.getPostId()+"&action=minusvote";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"something went error",Toast.LENGTH_LONG).show();
                    }
                });

                GlobalData.getInstance().addToRequestQueue(request);
            }
        });

        postOwner.setText(post.getPostOwner());
        time.setText(post.getTime());
        location.setText(post.getLocation());
        description.setText(post.getDescription());
        if(post.getImg().equals("1")) Picasso.get().load(post.getPostImg()).into(postImg);
        else postImg.setVisibility(View.GONE);

        Picasso.get().load(post.getProfileImg()).into(profileImg);
        plusVote.setText(post.getPlusVote());
        minusVote.setText(post.getMinusVote());
        comment.setText("Comment ("+post.getComment()+")");



        rvc = findViewById(R.id.rvc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvc.setLayoutManager(layoutManager);
        rvc.setItemAnimator(new DefaultItemAnimator());
        rvc.addItemDecoration(new DividerItemDecoration(rvc.getContext(), DividerItemDecoration.VERTICAL));
        url = "https://skbshariar.000webhostapp.com/api2.php?post_id="+post.getPostId()+"&action=loadcomments";
        parseComment();
    }

    private void parseComment(){
        comments.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Comment tempComment = new Comment(object.getString("user"),object.getString("comment"),object.getString("profileImg"));
                        comments.add(tempComment);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                load();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        });
        GlobalData.getInstance().addToRequestQueue(request);
    }

    void load(){
        commentAdapter = new CommentAdapter(comments,this);
        rvc.setAdapter(commentAdapter);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            dl.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
        if(onPauseFlg) finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(GlobalData.getInstance().onRestartFlg) finish();
    }
}
