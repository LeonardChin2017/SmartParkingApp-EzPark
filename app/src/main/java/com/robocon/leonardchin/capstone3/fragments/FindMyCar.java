package com.robocon.leonardchin.capstone3.fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.Toast;

import com.robocon.leonardchin.capstone3.Manifest;
import com.robocon.leonardchin.capstone3.R;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yinglan.alphatabs.OnTabChangedListner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.robocon.leonardchin.capstone3.fragments.CustomInfoWindowGoogleMap;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class FindMyCar extends Fragment implements OnMapReadyCallback{

    MapView mapView;

    LatLng position = new LatLng(37.77493, -122.41942);
    String markerText = "San Fransisco";

    private final int request_Code = 20;
    String mCurrentPhotoPath;
    Uri mImageCaptureUri;
    Intent intent ;

    private String m_Text = "";

    private GoogleMap mMap;
    LatLng myPosition;
    private AlphaTabsIndicator alphaTabsIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_my_car, container, false);

        //initialize map
        mapView = (MapView) rootView.findViewById(R.id.mapview2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        alphaTabsIndicator = (AlphaTabsIndicator) rootView.findViewById(R.id.alphaIndicator);

        alphaTabsIndicator.getTabView(0);
        alphaTabsIndicator.getTabView(1);
        alphaTabsIndicator.getTabView(2);
        alphaTabsIndicator.getTabView(3);

        alphaTabsIndicator.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                String title = "Your Car is here !!!";
                String subTitle = m_Text;
                //Marker
                MarkerOptions markerOpt = new MarkerOptions();
                switch (tabNum){
                    case 0:
                        Toast.makeText(getActivity(), "Take Photo", Toast.LENGTH_SHORT).show();
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, request_Code);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
//                                "image" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//                        try
//                        {
//                            startActivityForResult(intent, request_Code);
//                        }
//                        catch (ActivityNotFoundException e)
//                        {
//                            e.printStackTrace();
//                        }

//                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(photoCaptureIntent, requestCode);

//                        // create Intent to take a picture and return control to the calling application
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
//
//                         // start the image capture Intent
//                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
////                        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////                        startActivityForResult(intent, 7);
                        break;
                    case  1:
                        Toast.makeText(getActivity(), "Save Car Location", Toast.LENGTH_SHORT).show();
//                        mMap.addMarker(new MarkerOptions().position(myPosition).title("Your Car Location"));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 19));
                        markerOpt.position(myPosition)
                                .title(title)
                                .snippet(subTitle);
                        mMap.addMarker(markerOpt).showInfoWindow();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "Remove Car Location", Toast.LENGTH_SHORT).show();
                        if (mMap != null){
                            mMap.clear();
                        }
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "Detail of Parking Location", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Car Location");
                        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.location_input, (ViewGroup) getView(), false);
                        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                m_Text = input.getText().toString();
//                                Intent myIntent = new Intent(getActivity(),CustomInfoWindowGoogleMap.class);
//                                myIntent.putExtra("mytext",m_Text);
//                                startActivity(myIntent);

                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                        break;
                    default:
                        break;
                }
            }
        });

        return rootView;
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap thumbnail = null;
//        if (requestCode == requestCode) {
//            if (resultCode == RESULT_OK) {
//                thumbnail = (Bitmap) data.getExtras().get("data");
//                Intent i = new Intent(getActivity(), com.robocon.leonardchin.capstone3.fragments.CustomInfoWindowGoogleMap.class);
//                i.putExtra("name", thumbnail);
//                startActivity(i);
//            }
//        }
//        if (resultCode != RESULT_OK) return;
//        switch (requestCode) {
//            case request_Code:
//                Bitmap photo = null;
//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    photo = extras.getParcelable("data");
//                }
//                try {
//                    //error: not an enclosing class (this to class)
//                    Intent i = new Intent(getActivity(), CustomInfoWindowGoogleMap.class);
//                    i.putExtra("image", photo);
//                    startActivity(i);
//                }catch (ActivityNotFoundException e)
//                {
//                    e.printStackTrace();
//                }
//                break;
//        }

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == request_Code && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");

            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);

            // display the image from SD Card to ImageView Control
            String storeFilename = "photo_" + partFilename + ".jpg";
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        //Set Custom InfoWindow Adapter
        CustomInfoWindowGoogleMap adapter = new CustomInfoWindowGoogleMap(getActivity());
        mMap.setInfoWindowAdapter(adapter);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            myPosition = new LatLng(latitude, longitude);


            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
            mMap.animateCamera(yourLocation);
        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(1.555027, 103.646549);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        // Add a marker in tw
//        LatLng tw = new LatLng(1.5558057, 103.645838);
//        mMap.addMarker(new MarkerOptions().position(tw).title("Marker in 台灣"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(tw));

//        // Adding and showing marker when the map is touched
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng arg0) {
//                mMap.clear();
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(arg0);
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
//                Marker marker = mMap.addMarker(markerOptions);
//                marker.showInfoWindow();
//            }
//        });


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getTitle().equals("Your Car is here !!!")){
                    Toast.makeText(getActivity(), "Car Location", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}

