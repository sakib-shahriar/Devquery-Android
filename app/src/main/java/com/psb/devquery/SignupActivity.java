package com.psb.devquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class SignupActivity extends AppCompatActivity {

    TextView firstName, lastName, userName, email, profileTag, pass , msg;
    Button submit;
    SharedPreferences preferences;

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName =findViewById(R.id.etFn);
        lastName =findViewById(R.id.etLn);
        userName =findViewById(R.id.etUn);
        email =findViewById(R.id.etEmail);
        profileTag =findViewById(R.id.etProfileTag);
        pass = findViewById(R.id.etpass);
        msg =findViewById(R.id.msg);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstname = firstName.getText().toString();
                final String lastname = lastName.getText().toString();
                final String username = userName.getText().toString();
                final String emailaddress = email.getText().toString();
                final String profiletag = profileTag.getText().toString();
                final String password = pass.getText().toString();
                url = "https://skbshariar.000webhostapp.com/api_signup.php?first_name="+firstname+"&last_name="+lastname+"&user_name="+username+"&email="+emailaddress+"&profile_tag="+profiletag+"&password="+password+"&action=signup";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().equals("yes")){
                            Toast.makeText(getApplicationContext(), "Registered successfully!",Toast.LENGTH_LONG).show();
                            Intent goLogin = new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(goLogin);
                        }
                        else{
                            msg.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                GlobalData.getInstance().addToRequestQueue(request);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goLogin = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(goLogin);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
