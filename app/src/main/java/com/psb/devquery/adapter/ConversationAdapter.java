package com.psb.devquery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psb.devquery.R;
import com.psb.devquery.model.Conversation;
import com.psb.devquery.model.Notification;
import com.psb.devquery.model.Post;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.TheViewHolder> {

    LayoutInflater inflater;
    List<Conversation> listModel = Collections.EMPTY_LIST;
    Context c;

    public ConversationAdapter(List<Conversation> listModel, Context c){
        inflater = LayoutInflater.from(c);
        this.listModel = listModel;
        this.c = c;
    }



    public class TheViewHolder extends RecyclerView.ViewHolder{

        ImageView profileImg;
        TextView message,time,userName;
        RelativeLayout relativeLayout;
        public TheViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImg);
            userName = itemView.findViewById(R.id.userName);
            message = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
            relativeLayout = itemView.findViewById(R.id.message);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conversationInfoListener.onInfo(listModel.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public TheViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.conversation, viewGroup, false);
        TheViewHolder holder = new TheViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TheViewHolder theViewHolder, int pos) {
        Conversation conversation = listModel.get(pos);
        theViewHolder.userName.setText(conversation.getUserName());
        theViewHolder.message.setText(conversation.getMessage());
        theViewHolder.time.setText(conversation.getTime());
        Picasso.get().load(conversation.getProfileImg()).into(theViewHolder.profileImg);
        if(conversation.getIsRead().equals("0")) theViewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#d5deef"));
        else theViewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#f4f4f4"));
    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }


    ConversationInfoListener conversationInfoListener;

    public void setOnInfo(ConversationInfoListener conversationInfoListener){
        this.conversationInfoListener = conversationInfoListener;
    }
    public interface ConversationInfoListener {
        void onInfo(Conversation conversation);
    }
}
