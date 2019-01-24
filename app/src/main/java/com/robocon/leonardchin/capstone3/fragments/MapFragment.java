package com.robocon.leonardchin.capstone3.fragments;


import android.app.Fragment;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;

import com.robocon.leonardchin.capstone3.R;

public class MapFragment extends Fragment {


    MapView mapView;
    private GoogleMap mMap;
    DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        ref = FirebaseDatabase.getInstance().getReference("S04");
        //initialize map
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String parkspace = "0";
                for (DataSnapshot park : dataSnapshot.getChildren()) {
                    parkspace = park.getValue().toString();
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.parkicon);
                    LatLng utms04 = new LatLng(1.555027, 103.646549);
                    mMap.addMarker(new MarkerOptions()
                            .position(utms04)
                            .title("UTM SO4-S05 (Fee : RM 0 / Hour)")
                            .snippet("Free Parking Slot: " + parkspace)
                            .icon(icon)
                    );
                    float zoomLevel = 16.0f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(utms04, zoomLevel));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.i("DEBUG", "onMapReady");
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng utms10 = new LatLng(1.5558057, 103.645838);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.parkicon);
                mMap.addMarker(new MarkerOptions()
                        .position(utms10)
                        .title("UTM S10-S11 (Fee : RM 0 / Hour)")
                        .snippet("Free Parking Slot: 0")
                        .icon(icon)
                );
                LatLng utms43 = new LatLng(1.555246, 103.648303);
                mMap.addMarker(new MarkerOptions()
                        .position(utms43)
                        .title("UTM UTSB (Fee : RM 0 / Hour)")
                        .snippet("Free Parking Slot: 0")
                        .icon(icon)
                );

                LatLng utmk10 = new LatLng(1.560978, 103.649345);
                mMap.addMarker(new MarkerOptions()
                        .position(utmk10)
                        .title("UTM K10 (Fee : RM 0 / Hour)")
                        .snippet("Free Parking Slot: 0")
                        .icon(icon)
                );

                LatLng utmp19a = new LatLng(1.558635, 103.640698);
                mMap.addMarker(new MarkerOptions()
                        .position(utmp19a)
                        .title("UTM P19a (Fee : RM 0 / Hour)")
                        .snippet("Free Parking Slot: 0")
                        .icon(icon)
                );

            }

        });
        return rootView;
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
