package com.psb.devquery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.psb.devquery.MainActivity;
import com.psb.devquery.R;
import com.psb.devquery.model.Post;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.TheViewHolder>{
    LayoutInflater inflater;
    List<Post> listModel = Collections.EMPTY_LIST;
    Context c;

    TheViewHolder theViewHolder;
    public PostAdapter(List<Post> listModel, Context c) {
        inflater = LayoutInflater.from(c);
        this.listModel = listModel;
        this.c = c;
    }



    public class TheViewHolder extends RecyclerView.ViewHolder{
        TextView postOwner,time,location,description;
        ImageView postImg,profileImg;
        Button plusVote,minusVote,comment;
        public TheViewHolder(View itemView) {
            super(itemView);
            postOwner = itemView.findViewById(R.id.postOwner);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            postImg = itemView.findViewById(R.id.postImg);
            profileImg = itemView.findViewById(R.id.profileImg);
            plusVote = itemView.findViewById(R.id.plusVote);
            minusVote = itemView.findViewById(R.id.minusVote);
            comment = itemView.findViewById(R.id.comment);


            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postInfoListener.onInfo(listModel.get(getAdapterPosition()));
                }
            });


            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postInfoListener.onInfo(listModel.get(getAdapterPosition()));
                    int jhjk = 5;
                }
            });

            postImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postInfoListener.onInfo(listModel.get(getAdapterPosition()));
                }
            });

            plusVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plusVoteListener.onPlusVote(listModel.get(getAdapterPosition()).getPostId(),getAdapterPosition());
                }
            });

            minusVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minusVoteListener.onMinusVote(listModel.get(getAdapterPosition()).getPostId(),getAdapterPosition());
                }
            });

            postOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfoListner.onUserInfo(listModel.get(getAdapterPosition()).getPostOwner());
                }
            });

        }
    }

    public TheViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.post, viewGroup, false);
        TheViewHolder holder = new TheViewHolder(v);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TheViewHolder theViewHolder, int pos) {
        Post post = listModel.get(pos);

        theViewHolder.postOwner.setText(post.getPostOwner());
        theViewHolder.time.setText(post.getTime());
        theViewHolder.location.setText(post.getLocation());
        if(post.getDescription().length() > 200){
            String temp = post.getDescription();
            String str = "";
            for(int i = 0; i < 200; i++){
                str = str + temp.charAt(i);
            }
            str = str+".........see more";
            post.setDescription(str);
        }
        theViewHolder.description.setText(post.getDescription());
        //theViewHolder.postImg.setImageResource(R.drawable.ic_launcher_background);
        if(post.getImg().equals("1")) Picasso.get().load(post.getPostImg()).into(theViewHolder.postImg);
        else theViewHolder.postImg.setVisibility(View.GONE);

        Picasso.get().load(post.getProfileImg()).into(theViewHolder.profileImg);
        //theViewHolder.profileImg.setImageResource(R.drawable.ic_launcher_background);
        theViewHolder.plusVote.setText(post.getPlusVote());
        theViewHolder.minusVote.setText(post.getMinusVote());
        theViewHolder.comment.setText("Comment ("+post.getComment()+")");

        if(post.getIsLikedByMe().equals("yes")){

            theViewHolder.plusVote.setTextColor(Color.parseColor("#408a25"));
        }
        else {
            theViewHolder.plusVote.setTextColor(Color.parseColor("#000000"));
        }
        if(post.getIsDisLikedByMe().equals("yes")){
            theViewHolder.minusVote.setTextColor(Color.parseColor("#408a25"));
            //theViewHolder.minusVote.setBackgroundTintList(ColorStateList.valueOf(R.color.color_primary));
        }
        else {
            theViewHolder.minusVote.setTextColor(Color.parseColor("#000000"));
        }

    }



    public int getItemCount() {
        return listModel.size();
    }


    PostInfoListener postInfoListener;
    public void setOnInfo(PostInfoListener postInfoListener){
        this.postInfoListener = postInfoListener;
        int gh = 2;
    }
    public interface PostInfoListener {
        public void onInfo(Post post);
    }


    PlusVoteListener plusVoteListener;
    public void setOnPlusVote(PlusVoteListener plusVoteListener){
        this.plusVoteListener = plusVoteListener;
    }
    public interface PlusVoteListener{
        void onPlusVote(String postid, int pos);
    }

    MinusVoteListener minusVoteListener;
    public void setOnMinusVote(MinusVoteListener minusVoteListener){
        this.minusVoteListener = minusVoteListener;
    }
    public interface MinusVoteListener{
        void onMinusVote(String postid, int pos);
    }

    UserInfoListner userInfoListner;
    public void setOnUserInfo(UserInfoListner userInfoListner){
        this.userInfoListner = userInfoListner;
    }
    public interface UserInfoListner{
        void onUserInfo(String user);
    }










}
