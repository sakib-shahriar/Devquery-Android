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
import com.android.volley.toolbox.StringRequest;

public class ProfileUpdateActivity extends AppCompatActivity {

    TextView firstName, lastName, userName, email, profileTag, pass , msg;
    Button submit;
    SharedPreferences preferences;

    String url = "";
    public static final String FILE_NAME="preferenceFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);

        firstName =findViewById(R.id.etFn);
        lastName =findViewById(R.id.etLn);
        userName =findViewById(R.id.etUn);
        email =findViewById(R.id.etEmail);
        profileTag =findViewById(R.id.etProfileTag);
        pass = findViewById(R.id.etpass);
        msg =findViewById(R.id.msg);
        submit = findViewById(R.id.submit);

        userName.setEnabled(false);

        loadUserInfo();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstname = firstName.getText().toString();
                final String lastname = lastName.getText().toString();
                final String username = userName.getText().toString();
                final String emailaddress = email.getText().toString();
                final String profiletag = profileTag.getText().toString();
                final String password = pass.getText().toString();
                url = "https://skbshariar.000webhostapp.com/api_signup.php?first_name="+firstname+"&last_name="+lastname+"&user_name="+username+"&email="+emailaddress+"&profile_tag="+profiletag+"&password="+password+"&action=userInfoUodate";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().equals("yes")){
                            Toast.makeText(getApplicationContext(), "User info updated successfully!",Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("firstName", firstName.getText().toString());
                            editor.putString("lastName", lastName.getText().toString());
                            editor.putString("pass", pass.getText().toString());
                            editor.putString("email", email.getText().toString());
                            editor.putString("profileTag", profileTag.getText().toString());
                            editor.commit();
                            GlobalData.getInstance().firstName = firstName.getText().toString();
                            GlobalData.getInstance().lastName = lastName.getText().toString();
                            GlobalData.getInstance().pass = pass.getText().toString();
                            GlobalData.getInstance().email = email.getText().toString();
                            GlobalData.getInstance().profileTag = profileTag.getText().toString();
                            //System.out.println(jo.getString("profileTag"));
                            GlobalData.getInstance().onRestartFlg = false;

                            Intent refresh = new Intent(getApplicationContext(),ProfileUpdateActivity.class);
                            startActivity(refresh);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Couldn't update user info",Toast.LENGTH_LONG).show();
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


    private void loadUserInfo(){
        firstName.setText(GlobalData.getInstance().firstName.toString());
        lastName.setText(GlobalData.getInstance().lastName.toString());
        userName.setText(GlobalData.getInstance().userName.toString());
        pass.setText(GlobalData.getInstance().pass.toString());
        email.setText(GlobalData.getInstance().email.toString());
        profileTag.setText(GlobalData.getInstance().profileTag.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
