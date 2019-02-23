package com.psb.devquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextView userName,pass,msg,goToSignup;
    Button submit;
    SharedPreferences preferences;

    String url = "";
    public static final String FILE_NAME="preferenceFile";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName =findViewById(R.id.etUn);
        pass = findViewById(R.id.etpass);
        msg =findViewById(R.id.msg);
        goToSignup = findViewById(R.id.goToSignup);
        submit = findViewById(R.id.submit);
        preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = userName.getText().toString();
                final String password = pass.getText().toString();
                url = "https://skbshariar.000webhostapp.com/api22.php?user_name="+username+"&password="+password+"&action=loginvalidation";

//                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
//                });
//
//                GlobalData.getInstance().addToRequestQueue(request);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            loadPref(response);
                            Intent goHome = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(goHome);
                        }else{
                            msg.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                    }
                });

                GlobalData.getInstance().addToRequestQueue(request);

            }
        });
        goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSignup = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(goSignup);
            }
        });
    }

    void loadPref(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",userName.getText().toString());
        editor.putString("pass",pass.getText().toString());
        editor.putString("loggedin","yes");
        editor.commit();
        GlobalData.getInstance().userName = userName.getText().toString();
        GlobalData.getInstance().pass = pass.getText().toString();
        GlobalData.getInstance().onRestartFlg = false;
    }

    void loadPref(JSONObject jo){
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString("firstName", jo.getString("firstName"));
            editor.putString("lastName", jo.getString("lastName"));
            editor.putString("userName", jo.getString("userName"));
            editor.putString("pass", pass.getText().toString());
            editor.putString("dateOfJoin", jo.getString("dateOfJoin"));
            editor.putString("email", jo.getString("email"));
            editor.putString("profileTag", jo.getString("profileTag"));
            editor.putString("loggedin", "yes");
            editor.commit();
            GlobalData.getInstance().firstName = jo.getString("firstName");
            GlobalData.getInstance().lastName = jo.getString("lastName");
            GlobalData.getInstance().userName = jo.getString("userName");
            GlobalData.getInstance().pass = pass.getText().toString();
            GlobalData.getInstance().dateOfJoin = jo.getString("dateOfJoin");
            GlobalData.getInstance().email = jo.getString("email");
            GlobalData.getInstance().profileTag = jo.getString("profileTag");
            //System.out.println(jo.getString("profileTag"));
            GlobalData.getInstance().onRestartFlg = false;
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
