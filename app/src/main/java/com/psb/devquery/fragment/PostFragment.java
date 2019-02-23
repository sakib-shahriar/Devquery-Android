package com.psb.devquery.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.psb.devquery.GlobalData;
import com.psb.devquery.LoginActivity;
import com.psb.devquery.MainActivity;
import com.psb.devquery.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {


    public PostFragment() {
        // Required empty public constructor
    }

    Button makePost;
    EditText post;
    ImageView photoGal, photoCam,image;
    Bitmap bitmap;
    public String url;
    boolean imgFlg = false;
    public String postId;
    TextView userName;
    ImageView profileImg;



    View v;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_post, container, false);
        post = v.findViewById(R.id.post);
        makePost = v.findViewById(R.id.makePost);
        photoGal = v.findViewById(R.id.photoGal);
        photoCam = v.findViewById(R.id.photoCam);
        image = v.findViewById(R.id.image);
        userName = v.findViewById(R.id.userName);
        profileImg = v.findViewById(R.id.profileImg);
        Picasso.get().load("https://skbshariar.000webhostapp.com/img/"+GlobalData.getInstance().userName+".jpg").into(profileImg);
        userName.setText(GlobalData.getInstance().userName);

        photoGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Create image"),999);
            }
        });

        photoCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(MainActivity.context.getPackageManager()) != null)
                    startActivityForResult(intent,2);
            }
        });


        makePost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                url = "https://skbshariar.000webhostapp.com/api2.php?user_name="+GlobalData.getInstance().userName+"&des="+post.getText()+"&action=post";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        loadPostId(response);

                        //Toast.makeText(getActivity(),"response"+ response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
                GlobalData.getInstance().addToRequestQueue(request);

                //else Toast.makeText(getActivity(),"not ok"+ postId,Toast.LENGTH_LONG).show();

            }
        });











        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 999 && resultCode == MainActivity.RESULT_OK && data != null){
            try {
                Uri filepath = data.getData();
                imgFlg =true;
                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.context.getContentResolver(),filepath);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == 2 && resultCode == MainActivity.RESULT_OK && data != null){
            Uri filepath = data.getData();
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            image.setImageBitmap(bitmap);
            imgFlg =true;

        }

    }

    public String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }

    public void loadPostId(String p_id){
        this.postId = p_id;
        if(imgFlg){
            //Toast.makeText(getActivity(),"ok"+ postId,Toast.LENGTH_LONG).show();
            url = "https://skbshariar.000webhostapp.com/upload.php";
            StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("image",imageToString(bitmap));
                    params.put("postid",postId);
                    imgFlg =false;
                    return params;
                }
            };

            GlobalData.getInstance().addToRequestQueue(request1);
        }
        Toast.makeText(getActivity(),"Post Successful",Toast.LENGTH_LONG).show();
        image.setImageResource(0);
        post.setText("");

    }

}
