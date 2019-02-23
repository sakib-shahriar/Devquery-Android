package com.psb.devquery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.psb.devquery.adapter.PostAdapter;
import com.psb.devquery.fragment.ConversationFragment;
import com.psb.devquery.fragment.HomeFragment;
import com.psb.devquery.fragment.NotificationFragment;
import com.psb.devquery.fragment.PostFragment;
import com.psb.devquery.model.Post;
import com.psb.devquery.service.PostService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout dl;
    NavigationView nv;
    Menu menu;
    RecyclerView rcv;
    PostAdapter postAdapter;
    ArrayList<Post> post = new ArrayList<Post>();
    public static BottomNavigationView bnv;
    boolean onPauseFlg = false;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.app_bar);
        bnv = findViewById(R.id.main_nav);
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

//        Intent intent = new Intent(MainActivity.this,PostService.class);
//        startService(intent);




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
                    Intent goToLogin = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(goToLogin);

                }
                else if(menuItem.getItemId() == R.id.profile){
                    Intent goToProfile = new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(goToProfile);
                }
                return false;
            }
        });

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.post) {
                    GlobalData.getInstance().lastFragment = "post";
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr, new PostFragment()).commit();
                }
                else if(menuItem.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr, new HomeFragment()).commit();
                    GlobalData.getInstance().lastFragment = "home";
                }
                else if(menuItem.getItemId() == R.id.notification){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr, new NotificationFragment()).commit();
                    GlobalData.getInstance().lastFragment = "notification";
                }
                else if(menuItem.getItemId() == R.id.message){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr, new ConversationFragment()).commit();
                    GlobalData.getInstance().lastFragment = "conversation";
                }
                return true;
            }
        });


        getSupportFragmentManager().beginTransaction().add(R.id.fr,new HomeFragment()).commit();


    }

    @Override
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
        if(id == R.id.search)
            Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();


        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
        if(onPauseFlg) finish();
    }

    protected void onRestart() {
        super.onRestart();
        if(GlobalData.getInstance().onRestartFlg) finish();
    }

    public void onBackPressed() {




        if(!GlobalData.getInstance().lastFragment.equals("home")){
            //Toast.makeText(this,GlobalData.getInstance().cnt+"",Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().add(R.id.fr, new HomeFragment()).commit();
            GlobalData.getInstance().lastFragment = "home";
            bnv.setSelectedItemId(R.id.home);
        }
        else{
            super.onBackPressed();
        }



    }
}
