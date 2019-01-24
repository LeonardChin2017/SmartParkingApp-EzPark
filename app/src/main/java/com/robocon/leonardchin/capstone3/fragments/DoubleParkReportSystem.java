package com.robocon.leonardchin.capstone3.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.Calendar;

import com.robocon.leonardchin.capstone3.fragments.DoubleParkReport;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;


import com.google.firebase.storage.UploadTask;
import com.robocon.leonardchin.capstone3.Manifest;
import com.robocon.leonardchin.capstone3.R;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yinglan.alphatabs.OnTabChangedListner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class DoubleParkReportSystem extends Fragment implements OnMapReadyCallback{
    DatabaseReference ref;
    MapView mapView;
    LatLng position = new LatLng(37.77493, -122.41942);
    String markerText = "San Fransisco";
    String details = "";
    private GoogleMap mMap;
    LatLng myPosition;

    private final int request_Code = 20;
    private String m_Text = "";

    private AlphaTabsIndicator alphaTabsIndicator;

    FirebaseStorage storage;
    StorageReference storageReference;

    File outputFile = new File(Environment.getExternalStorageDirectory(), "location.jpg");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_double_park_report_system, container, false);

        //initialize map
        mapView = (MapView) rootView.findViewById(R.id.mapview3);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        alphaTabsIndicator = (AlphaTabsIndicator) rootView.findViewById(R.id.alphaIndicator1);

        alphaTabsIndicator.getTabView(0);
        alphaTabsIndicator.getTabView(1);
        alphaTabsIndicator.getTabView(2);
        alphaTabsIndicator.getTabView(3);

        alphaTabsIndicator.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                String title = "This car is double park!!!";
                String subTitle = m_Text;
                //Marker
                MarkerOptions markerOpt = new MarkerOptions();
                switch (tabNum){
                    case 0:
                        Toast.makeText(getActivity(), "Take Photo", Toast.LENGTH_SHORT).show();
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, request_Code);
                        break;
                    case  1:
                        Toast.makeText(getActivity(), "Save Car Location", Toast.LENGTH_SHORT).show();
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
                        builder.setTitle("Car Details");
                        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.location_input, (ViewGroup) getView(), false);
                        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                 m_Text = input.getText().toString();

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
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == request_Code && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");

            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);
            String storeFilename = "location_" + partFilename + ".jpg";
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
        CustomInfoWindowDoublePark adapter = new CustomInfoWindowDoublePark(getActivity());
        mMap.setInfoWindowAdapter(adapter);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            myPosition = new LatLng(latitude, longitude);


            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
            mMap.animateCamera(yourLocation);
        }

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getTitle().equals("This car is double park!!!")){
                    Toast.makeText(getActivity(), "Car Location", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // Set a title for alert dialog
                    builder.setTitle("Report double park");

                    // Ask the final question
                    builder.setMessage("Are you sure you want to report this car?");
                    final Date currentTime = Calendar.getInstance().getTime();

                    Date date = new Date();
                    String strDateFormat = "hh:mm:ss a";
                    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                    final String formattedDate= dateFormat.format(date);
                    final DoubleParkReport doubleParkReport = new DoubleParkReport(String.valueOf(myPosition.latitude), String.valueOf(myPosition.longitude), m_Text, formattedDate, "goo.gl/4iXLns");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Report", Toast.LENGTH_SHORT).show();
                            ref.child("DoublePark").push().setValue(myPosition);
                            ref.child("ReportDoublePark").child("location").setValue(myPosition);
                            ref.child("ReportDoublePark").child("details").setValue(m_Text);
                            ref.child("ReportDoublePark").child("time").setValue(formattedDate);
                            ref.child("DoubleParkReportHistory").push().setValue(doubleParkReport);

                            String fileName = "location.jpg";
                            String completePath = Environment.getExternalStorageDirectory() + "/" + fileName;

                            File file = new File(completePath);
                            Uri filePath = Uri.fromFile(file);


//                            StorageReference carRef = storageReference.child("images/pic.jpg");
//                            carRef.putFile(uri);
                            if (filePath != null) {
                                //displaying a progress dialog while upload is going on
                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setTitle("Uploading");
                                progressDialog.show();

                                StorageReference refCar = storageReference.child("Photos/location.jpg");
                                refCar.putFile(filePath)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                //if the upload is successfull
                                                //hiding the progress dialog
                                                progressDialog.dismiss();

                                                //and displaying a success toast
                                                Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                //if the upload is not successfull
                                                //hiding the progress dialog
                                                progressDialog.dismiss();

                                                //and displaying error message
                                                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                //calculating progress percentage
                                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                                //displaying percentage in progress dialog
                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                            }
                                        });
                            }
                            //if there is not any file
                            else {
                                //you can display an error toast
                            }
//                            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
//                            String storeFilename = "/location.jpg";
//                            ref.putFile(outputFile);
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    });


                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
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
