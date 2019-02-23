package com.psb.devquery;

import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class test extends AppCompatActivity {


    ImageView imageView;
    Button button;
    TextView tv;
    SharedPreferences preferences;
    public static final String FILE_NAME="preferenceFile";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        tv = findViewById(R.id.tv);
//        preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("userName","sakibaaaaaaaaaaaaaa");
//        editor.putString("pass","1234");
//        editor.putString("loggedin","yes");
//        editor.commit();
//
//        tv.setText(preferences.getString("userName",""));

    }
}
