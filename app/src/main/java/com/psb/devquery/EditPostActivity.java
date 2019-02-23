package com.psb.devquery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.psb.devquery.fragment.HomeFragment;
import com.psb.devquery.model.Post;

public class EditPostActivity extends AppCompatActivity {

    Button makePost;
    EditText post;
    Bitmap bitmap;
    public String url;
    boolean imgFlg = false;
    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        post = findViewById(R.id.post);
        makePost = findViewById(R.id.makePost);

        final String postId = getIntent().getStringExtra("postid");
        //System.out.println(postId);
        post.setText(getIntent().getStringExtra("description"));


        makePost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+postId+"&des="+post.getText()+"&action=editPost";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        //loadPostId(response);
                        //Toast.makeText(getActivity(),"response"+ response,Toast.LENGTH_LONG).show();
                        if(response.equals("yes")){
                            int index = 0;
                            for(int i = 0; i < HomeFragment.post.size(); i++) if(HomeFragment.post.get(i).getPostId().equals(postId)) index = i;
                            HomeFragment.post.get(index).setDescription(post.getText().toString());
                            Intent goToLogin = new Intent(getApplicationContext(),PostActivity.class);
                            Post post1 = (Post) getIntent().getSerializableExtra("post");
                            post1.setDescription(post.getText().toString());
                            goToLogin.putExtra("post",post1);
                            startActivity(goToLogin);
                            Toast.makeText(getApplicationContext(),"post updated successfully",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
                GlobalData.getInstance().addToRequestQueue(request);

                //else Toast.makeText(getActivity(),"not ok"+ postId,Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
