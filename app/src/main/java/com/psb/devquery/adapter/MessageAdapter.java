package com.psb.devquery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psb.devquery.GlobalData;
import com.psb.devquery.R;
import com.psb.devquery.model.Message;
import com.psb.devquery.model.Notification;

import java.util.Collections;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.TheViewHolder> {

    LayoutInflater inflater;
    List<Message> listModel = Collections.EMPTY_LIST;
    Context c;

    public MessageAdapter(List<Message> listModel, Context c){
        inflater = LayoutInflater.from(c);
        this.listModel = listModel;
        this.c = c;
    }


    class TheViewHolder extends RecyclerView.ViewHolder{

        TextView message,time,userName;

        public TheViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.msg);
            userName = itemView.findViewById(R.id.userName);

        }
    }


    @NonNull
    @Override
    public TheViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.message, viewGroup, false);
        TheViewHolder holder = new TheViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TheViewHolder theViewHolder, int pos) {
        Message message = listModel.get(pos);
        if(!message.getUserName().equals(GlobalData.getInstance().userName))
            theViewHolder.userName.setText(message.getUserName());
        else theViewHolder.userName.setText("Me");
        theViewHolder.time.setText(message.getTime());
        theViewHolder.message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }
}
