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
import com.psb.devquery.model.Notification;
import com.psb.devquery.model.Post;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.TheViewHolder> {

    LayoutInflater inflater;
    List<Notification> listModel = Collections.EMPTY_LIST;
    Context c;

    public NotificationAdapter(List<Notification> listModel, Context c){
        inflater = LayoutInflater.from(c);
        this.listModel = listModel;
        this.c = c;
    }



    class TheViewHolder extends RecyclerView.ViewHolder{

        ImageView profileImg;
        TextView notification,time;
        RelativeLayout relativeLayout;

        public TheViewHolder(View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImg);
            notification = itemView.findViewById(R.id.notification);
            time = itemView.findViewById(R.id.time);
            relativeLayout = itemView.findViewById(R.id.notf);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationInfoListener.onInfo(listModel.get(getAdapterPosition()));;
                }
            });

        }
    }

    @NonNull
    @Override
    public TheViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.notification, viewGroup, false);
        TheViewHolder holder = new TheViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TheViewHolder theViewHolder, int pos) {
        Notification notification = listModel.get(pos);

        theViewHolder.time.setText(notification.getTime());
        theViewHolder.notification.setText(notification.getNotification());
        Picasso.get().load(notification.getProfileImg()).into(theViewHolder.profileImg);

        if(notification.getIsRead().equals("0")) theViewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#d5deef"));
        else theViewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#f4f4f4"));



    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }

    NotificationInfoListener notificationInfoListener;

    public void setOnInfo(NotificationInfoListener notificationInfoListener){
        this.notificationInfoListener = notificationInfoListener;
    }
    public interface NotificationInfoListener {
        void onInfo(Notification notification);
    }
}
