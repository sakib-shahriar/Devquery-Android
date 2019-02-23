package com.psb.devquery;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.psb.devquery.adapter.PostAdapter;
import com.psb.devquery.fragment.ProfileFragment;
import com.psb.devquery.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileActivity extends AppCompatActivity {

    TextView profileUserName, profileTag;
    DrawerLayout dl;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);

        profileUserName = findViewById(R.id.profileUserName);
        profileTag = findViewById(R.id.profileTag);
        fab = findViewById(R.id.fab);

        profileUserName.setText(GlobalData.getInstance().userName+" ("+GlobalData.getInstance().profileTag+")");
        profileTag.setText(GlobalData.getInstance().email);
        getSupportFragmentManager().beginTransaction().add(R.id.fr,new ProfileFragment()).commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goProfileUpdate = new Intent(getApplicationContext(),ProfileUpdateActivity.class);
                startActivity(goProfileUpdate);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(GlobalData.getInstance().onRestartFlg) finish();
    }

}
