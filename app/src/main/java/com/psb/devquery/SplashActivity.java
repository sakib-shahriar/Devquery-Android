package com.psb.devquery;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.psb.devquery.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_ALL = 1;
    SharedPreferences preferences;
    public static final String FILE_NAME="preferenceFile";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};
        if(!hasPermission(SplashActivity.this,PERMISSIONS)) {
            ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        else{
            preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
            if(preferences.getString("loggedin","").equals("yes")) {
                GlobalData.getInstance().firstName = preferences.getString("firstName", "");
                GlobalData.getInstance().lastName = preferences.getString("lastName", "");
                GlobalData.getInstance().userName = preferences.getString("userName", "");
                GlobalData.getInstance().pass = preferences.getString("pass", "");
                GlobalData.getInstance().dateOfJoin = preferences.getString("dateOfJoin", "");
                GlobalData.getInstance().email = preferences.getString("email", "");
                GlobalData.getInstance().profileTag = preferences.getString("profileTag", "");
                GlobalData.getInstance().lastFragment = "home";
                new DownloadTask().execute();

            }
            else {
                Intent goToLogin = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(goToLogin);
            }
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public static boolean hasPermission(Context context, String... permissions){
        if(context != null && permissions != null){
            for(String permission : permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }

        return true;
    }

    class DownloadTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://skbshariar.000webhostapp.com/api3.php?user_name="+GlobalData.getInstance().userName;
            GlobalData.getInstance().post.clear();
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
                            GlobalData.getInstance().post.add(tempPost);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    Collections.reverse(GlobalData.getInstance().post);
                    Intent goHome = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(goHome);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                }
            });
            GlobalData.getInstance().addToRequestQueue(request);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_ALL: {

                if(grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED ){
                    preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
                    if(preferences.getString("loggedin","").equals("yes")){
                        GlobalData.getInstance().firstName = preferences.getString("firstName","");
                        GlobalData.getInstance().lastName = preferences.getString("lastName","");
                        GlobalData.getInstance().userName = preferences.getString("userName","");
                        GlobalData.getInstance().pass = preferences.getString("pass","");
                        GlobalData.getInstance().dateOfJoin = preferences.getString("dateOfJoin","");
                        GlobalData.getInstance().email = preferences.getString("email","");
                        GlobalData.getInstance().profileTag = preferences.getString("profileTag","");
                        new DownloadTask().execute();
                    }
                    else{
                        Intent goToLogin = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(goToLogin);
                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setMessage("Permission Required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                }
                return;
            }
        }
    }
}
