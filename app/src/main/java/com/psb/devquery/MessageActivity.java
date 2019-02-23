package com.psb.devquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.psb.devquery.adapter.MessageAdapter;
import com.psb.devquery.fragment.ConversationFragment;
import com.psb.devquery.model.Comment;
import com.psb.devquery.model.Conversation;
import com.psb.devquery.model.Message;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {


    RecyclerView rcmsg;
    MessageAdapter messageAdapter;
    public static ArrayList<Message> messages = new ArrayList<Message>();
    ImageButton send;
    EditText msgbox;
    TextView user;
    String convoId = "",username = "";
    DrawerLayout dl;
    Toolbar toolbar;
    NavigationView nv;
    boolean onPauseFlg = false;
    boolean flg = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        dl = findViewById(R.id.dl);
        send = findViewById(R.id.send);

        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle("devquery");
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        nv = findViewById(R.id.navigation);
        View headerView = nv.getHeaderView(0);
        TextView temp = headerView.findViewById(R.id.nhUserName);
        temp.setText(GlobalData.getInstance().userName);
        ImageView tempImg = headerView.findViewById(R.id.profileImg);
        Picasso.get().load("https://skbshariar.000webhostapp.com/img/"+GlobalData.getInstance().userName+".jpg").into(tempImg);

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
                    Intent goToLogin = new Intent(MessageActivity.this,LoginActivity.class);
                    startActivity(goToLogin);

                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                String msg = msgbox.getText().toString();
                if(!msg.equals("")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date2 = Calendar.getInstance().getTime();
                    String result = simpleDateFormat.format(date2);


                    messages.add(0,new Message(msg, GlobalData.getInstance().userName, result));
                    messageAdapter = new MessageAdapter(messages, getApplicationContext());
                    rcmsg.swapAdapter(messageAdapter, false);
                    msgbox.setText("");

                    for(int i = 0; i < ConversationFragment.conversations.size(); i++){
                        if(ConversationFragment.conversations.get(i).getConversationId().equals(convoId)){
                            ConversationFragment.conversations.get(i).setMessage(msg);
                            ConversationFragment.conversations.get(i).setTime(getFormatedTime(result));
                            ConversationFragment.conversations.get(i).setIsRead("1");

                        }
                    }
                    String url = "https://skbshariar.000webhostapp.com/api2.php?user_name=" + GlobalData.getInstance().userName + "&convo_id=" + convoId + "&msg=" +msg+ "&action=sendmessage";
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "something went error", Toast.LENGTH_LONG).show();
                        }
                    });

                    GlobalData.getInstance().addToRequestQueue(request);

                }

            }
        });

        Intent intent = getIntent();

        convoId = intent.getStringExtra("convoId");
        username = intent.getStringExtra("username");


        rcmsg = findViewById(R.id.rcmsg);

        msgbox = findViewById(R.id.etMsg);
        user = findViewById(R.id.userName);
        user.setText(username);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        rcmsg.setLayoutManager(layoutManager);
        rcmsg.setItemAnimator(new DefaultItemAnimator());
        rcmsg.addItemDecoration(new DividerItemDecoration(rcmsg.getContext(), DividerItemDecoration.VERTICAL));

        parseMessage();
    }

    private void parseMessage() {

        messages.clear();
        String url = "https://skbshariar.000webhostapp.com/api2.php?conversation_id="+convoId+"&action=getmessages";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Message tempMessage = new Message(object.getString("message"),object.getString("userName"),object.getString("time"));
                        messages.add(tempMessage);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                Collections.reverse(messages);
                load();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        });
        GlobalData.getInstance().addToRequestQueue(request);
        load();
    }

    void load(){
        messageAdapter = new MessageAdapter(messages,this);
        if(!flg) rcmsg.setAdapter(messageAdapter);
        else rcmsg.swapAdapter(messageAdapter,false);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String url = "https://skbshariar.000webhostapp.com/api2.php?convo_id="+convoId+"&user_name="+GlobalData.getInstance().userName+"&action=convoupdate";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(response.equals("yes")) load2();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"something went error",Toast.LENGTH_LONG).show();
                    }
                });

                GlobalData.getInstance().addToRequestQueue(request);
                handler.postDelayed(this,1000);

            }
        };

        handler.postDelayed(runnable,1000);
    }

    void load2(){
        flg = true;
        parseMessage();
        flg = false;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getFormatedTime(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null,date2 = null,date3=null;
        try {
            date1 = simpleDateFormat.parse(time);
            date2 = Calendar.getInstance().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000*60*60*24));
        int months = days / 30;
        int hours = (int)difference/(1000 * 60 * 60);
        hours = hours - (days * 24);
        int min = (int)(difference/(1000*60)) % 60;

        if(months > 0) return months+" month ago";
        else if(days > 0) return days+" day ago";
        else if(hours > 0) return hours+" hour ago";
        else if(min > 0) return  min+" minute ago";
        else return "0 minute ago";
    }

}
