package com.robocon.leonardchin.capstone3.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.robocon.leonardchin.capstone3.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    ImageView image;
    Bitmap bMap;

    public CustomInfoWindowGoogleMap(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_contents, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
//        bMap = ((Activity) context).getIntent().getParcelableExtra("image");
        image = (ImageView) view.findViewById(R.id.imageView1);
//        image.setImageBitmap(bMap);
//
//        Bitmap bitmap  = ((Activity) context).getIntent().getExtras().getParcelable("name");
//        image.setImageBitmap(bitmap);

        String storeFilename = "/photo.jpg";
        Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
        image.setImageBitmap(mBitmap);


        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());

        return view;
    }

    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}