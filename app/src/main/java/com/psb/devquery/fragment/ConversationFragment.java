package com.psb.devquery.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.psb.devquery.GlobalData;
import com.psb.devquery.MessageActivity;
import com.psb.devquery.PostActivity;
import com.psb.devquery.R;
import com.psb.devquery.adapter.ConversationAdapter;
import com.psb.devquery.adapter.NotificationAdapter;
import com.psb.devquery.adapter.PostAdapter;
import com.psb.devquery.model.Conversation;
import com.psb.devquery.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {

    RecyclerView rcconvo;
    ConversationAdapter conversationAdapter;
    public static ArrayList<Conversation> conversations = new ArrayList<Conversation>();

    public ConversationFragment() {
        // Required empty public constructor
    }


    View v;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_conversation, container, false);

        rcconvo = v.findViewById(R.id.rcconvo);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcconvo.setLayoutManager(layoutManager);
        rcconvo.setItemAnimator(new DefaultItemAnimator());
        rcconvo.addItemDecoration(new DividerItemDecoration(rcconvo.getContext(), DividerItemDecoration.VERTICAL));

        parseConversation();

        return v;
    }

    private void parseConversation() {
        conversations.clear();
        String url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&action=getconvos";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONArray response) {



                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Conversation tempConversation = new Conversation(object.getString("message"),object.getString("userName"),object.getString("profileImg"),
                                object.getString("conversationId"),object.getString("isRead"),object.getString("time"));
                        conversations.add(tempConversation);
                        //Toast.makeText(getActivity(),conversations.get(i).getProfileImg(),Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                load();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        });
        GlobalData.getInstance().addToRequestQueue(request);
    }

    void load(){
        conversationAdapter = new ConversationAdapter(conversations,getActivity());
        conversationAdapter.setOnInfo(new ConversationAdapter.ConversationInfoListener() {
            @Override
            public void onInfo(Conversation conversation) {

                String url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&convo_id="+conversation.getConversationId()+"&action=makeread";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"something went error",Toast.LENGTH_LONG).show();
                    }

                });

                GlobalData.getInstance().addToRequestQueue(request);

                Intent intent = new Intent(getActivity(),MessageActivity.class);
                intent.putExtra("convoId",conversation.getConversationId());
                intent.putExtra("username",conversation.getUserName());
                startActivity(intent);
                //Toast.makeText(getActivity(),conversation.getUserName(),Toast.LENGTH_LONG).show();
            }
        });
        rcconvo.setAdapter(conversationAdapter);
        //Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
    }

    public void onStart() {
        super.onStart();
        conversationAdapter = new ConversationAdapter(conversations,getActivity());
        rcconvo.swapAdapter(conversationAdapter,false);
    }

}
