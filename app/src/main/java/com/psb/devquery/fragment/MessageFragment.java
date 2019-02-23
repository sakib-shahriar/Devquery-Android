package com.psb.devquery.fragment;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
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
import com.psb.devquery.GlobalData;
import com.psb.devquery.R;
import com.psb.devquery.adapter.ConversationAdapter;
import com.psb.devquery.adapter.MessageAdapter;
import com.psb.devquery.model.Conversation;
import com.psb.devquery.model.Message;
import com.psb.devquery.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    RecyclerView rcmsg;
    MessageAdapter messageAdapter;
    public static ArrayList<Message> messages = new ArrayList<Message>();


    public MessageFragment() {
        // Required empty public constructor
    }


    View v;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_message, container, false);


        rcmsg = v.findViewById(R.id.rcmsg);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcmsg.setLayoutManager(layoutManager);
        rcmsg.setItemAnimator(new DefaultItemAnimator());
        rcmsg.addItemDecoration(new DividerItemDecoration(rcmsg.getContext(), DividerItemDecoration.VERTICAL));

        parseMessage();


        return v;
    }

    private void parseMessage() {

        messages.clear();
        String url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&action=getnotifications";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Message tempMessage = new Message(object.getString("msg"),object.getString("userName"),object.getString("time"));
                        messages.add(tempMessage);

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                Collections.reverse(messages);
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
        messageAdapter = new MessageAdapter(messages,getActivity());
        rcmsg.setAdapter(messageAdapter);
    }

}
