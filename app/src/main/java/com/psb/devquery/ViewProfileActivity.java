package com.psb.devquery;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewProfileActivity extends AppCompatActivity {

    TextView userName, profileTag, fullnameTv, usernameTv, profiletagTv, emailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View hView =  navigationView.getHeaderView(0);
//
//        userName = hView.findViewById(R.id.user_name);
//        profileTag = hView.findViewById(R.id.profile_tag);
//
//        fullnameTv = findViewById(R.id.user_profile_full_name);
//        usernameTv = findViewById(R.id.user_profile_user_name);
//        profiletagTv = findViewById(R.id.user_profile_tag);
//        emailTv = findViewById(R.id.user_profile_email_address);

        //final String postOwner = getIntent().getStringExtra("postOwner");

        //userName.setText(postOwner);
//        Toast.makeText(getApplicationContext(),postOwner,Toast.LENGTH_LONG).show();
//
//        final String url = "https://skbshariar.000webhostapp.com/api22.php?user_name="+postOwner+"&action=viewProfile";
//
//                /*StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(response.toString().equals("yes")){
//                            loadPref();
//                            Intent goHome = new Intent(LoginActivity.this,MainActivity.class);
//                            startActivity(goHome);
//
//                        }
//                        else{
//                            msg.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });*/
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onResponse(JSONObject response) {
//                if(response!=null){
//                    loadInfo(response);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
//            }
//        });
//
//        GlobalData.getInstance().addToRequestQueue(request);
    }

//    private void loadInfo(JSONObject jo){
//        try {
//            fullnameTv.setText(jo.getString("firstName")+" "+jo.getString("lastName"));
//            usernameTv.setText(jo.getString("userName"));
//            profiletagTv.setText(jo.getString("profileTag"));
//            emailTv.setText(jo.getString("email"));
//
//            profileTag.setText(jo.getString("profileTag"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
