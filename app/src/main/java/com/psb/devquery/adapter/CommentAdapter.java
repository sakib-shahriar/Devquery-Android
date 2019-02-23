package com.psb.devquery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.psb.devquery.R;
import com.psb.devquery.model.Comment;
import com.psb.devquery.model.Post;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.TheViewHolder>{
    LayoutInflater inflater;
    List<Comment> listModel = Collections.EMPTY_LIST;
    Context c;

    public CommentAdapter(List<Comment> listModel, Context c) {
        inflater = LayoutInflater.from(c);
        this.listModel = listModel;
        this.c = c;
    }



    class TheViewHolder extends RecyclerView.ViewHolder{
        TextView user,comment;
        ImageView profileImg;
        public TheViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            profileImg = itemView.findViewById(R.id.profileImg);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    public TheViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.comment, viewGroup, false);
        TheViewHolder holder = new TheViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TheViewHolder theViewHolder, int pos) {
        Comment comment = listModel.get(pos);
        Picasso.get().load(comment.getProfileImg()).into(theViewHolder.profileImg);
        theViewHolder.comment.setText(comment.getComment());
        theViewHolder.user.setText(comment.getUser());
    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }


}
