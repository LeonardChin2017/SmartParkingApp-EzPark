package com.robocon.leonardchin.capstone3.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robocon.leonardchin.capstone3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Epayment extends Fragment {

    private ListView locationListView = null;
    private View popupInputDialogView = null;
    private EditText location1 = null;
    private EditText location2= null;
    private EditText location3 = null;
    private EditText cardetail1= null;
    private EditText cardetail2= null;
    private EditText parkingduration = null;
    private Button set_btn = null;
    private Button cancel_btn= null;
    private Spinner mParkingTimeSpinner = null;
    String cost = "";
    String johor_contact = "33122";


    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_epayment, container, false);

        final TextView location_text1 = (TextView) v.findViewById(R.id.location_text1);
        final TextView location_text2 = (TextView) v.findViewById(R.id.location_text2);
        final TextView location_text3 = (TextView) v.findViewById(R.id.location_text3);

        final TextView car_text1 = (TextView) v.findViewById(R.id.car_detail_text1);
        final TextView car_text2 = (TextView) v.findViewById(R.id.car_detail_text2);

        final TextView time_text1 = (TextView) v.findViewById(R.id.time_detail_text1);

        final TextView cost_text1 = (TextView) v.findViewById(R.id.cost_text1);

        String send_msg = "J"+ time_text1 + "" + car_text1 + ""+ "none";
        final String time_text = time_text1.getText().toString();
        final String car_text = car_text1.getText().toString();

        Button btn = v.findViewById(R.id.id_edit1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit Location", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Parking Location");
                alertDialogBuilder.setCancelable(false);

                // Init popup dialog view and it's ui controls.
                initPopupViewControlsLocation();

                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(popupInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                set_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get user data from popup dialog editeext.
                        String location_input1 = location1.getText().toString();
                        String location_input2 = location2.getText().toString();
                        String location_input3 = location3.getText().toString();

                        location_text1.setText(location_input1);
                        location_text2.setText(location_input2);
                        location_text3.setText(location_input3);

                        alertDialog.cancel();
                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
            }
        });


        v.findViewById(R.id.id_edit2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit Car Detail", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());
                alertDialogBuilder1.setTitle("Car Details");
                alertDialogBuilder1.setCancelable(false);

                // Init popup dialog view and it's ui controls.
                initPopupViewControlsCarDetails();

                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder1.setView(popupInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder1.create();
                alertDialog.show();

                set_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get user data from popup dialog editeext.
                        String car_input1 = cardetail1.getText().toString();
                        String car_input2 = cardetail2.getText().toString();

                        car_text1.setText(car_input1);
                        car_text2.setText(car_input2);

                        alertDialog.cancel();
                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

            }
        });

        v.findViewById(R.id.id_edit3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit Time", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());
                alertDialogBuilder1.setTitle("Car Details");
                alertDialogBuilder1.setCancelable(false);

                // Init popup dialog view and it's ui controls.
                initPopupViewControlsParkingTime();

                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder1.setView(popupInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder1.create();
                alertDialog.show();

                set_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get user data from popup dialog editeext.
                        String time_input1 = parkingduration.getText().toString();

                        time_text1.setText(time_input1);

                        if(time_input1.equals("30 mins")){
                            cost_text1.setText("RM 0.40");
                        }else if(time_input1.equals("1 hrs")){
                            cost_text1.setText("RM 0.60");
                        }else if(time_input1.equals("2 hrs")){
                            cost_text1.setText("RM 1.20");
                        }else if(time_input1.equals("3 hrs")){
                            cost_text1.setText("RM 1.80");
                        }else if(time_input1.equals("4 hrs")){
                            cost_text1.setText("RM 2.40");
                        }else if(time_input1.equals("5 hrs")){
                            cost_text1.setText("RM 3.00");
                        }else if(time_input1.equals("6 hrs")){
                            cost_text1.setText("RM 3.60");
                        }else if(time_input1.equals("7 hrs")){
                            cost_text1.setText("RM 4.20");
                        }else if(time_input1.equals("8 hrs")){
                            cost_text1.setText("RM 4.80");
                        }else{
                            cost_text1.setText("RM 0.00");
                        }

                        alertDialog.cancel();

                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
            }
        });



        v.findViewById(R.id.id_epayment_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Set a title for alert dialog
                builder.setTitle("E-payment");

                // Ask the final question
                builder.setMessage("Are you sure?");
                Toast.makeText(getActivity(), "Pay and Park", Toast.LENGTH_SHORT).show();

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String time = "";
                        if(parkingduration.getText().toString().equals("30 mins")){
                           time = "30";
                        }else if(parkingduration.getText().toString().equals("1 hrs")){
                            time = "1";
                        }else if(parkingduration.getText().toString().equals("2 hrs")){
                            time = "2";
                        }else if(parkingduration.getText().toString().equals("3 hrs")){
                            time = "3";
                        }else if(parkingduration.getText().toString().equals("4 hrs")){
                            time = "4";
                        }else if(parkingduration.getText().toString().equals("5 hrs")){
                            time = "5";
                        }else if(parkingduration.getText().toString().equals("6 hrs")){
                            time = "6";
                        }else if(parkingduration.getText().toString().equals("7 hrs")){
                            time = "7";
                        }else if(parkingduration.getText().toString().equals("8 hrs")){
                            time = "8";
                        }else{
                            time = "0";
                        }
                        try{
                            SmsManager smgr = SmsManager.getDefault();
                            smgr.sendTextMessage(johor_contact,null,"J" + time + " " + cardetail1.getText().toString() + " "+ location3.getText().toString(),null,null);
                            Toast.makeText(getActivity(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();

                            Fragment newFragment = new ParkingTimer();
                            Bundle args = new Bundle();
                            args.putString("park_time", parkingduration.getText().toString());
                            if (args!= null) {
                                newFragment.setArguments(args);
                            }
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_container, newFragment);
                            transaction.addToBackStack(null);

                            // Commit the transaction
                            transaction.commit();
                        }
                        catch (Exception e){
                            Toast.makeText(getActivity(), "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                        }
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

//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();




        });
        return v;

    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initPopupViewControlsLocation()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.pop_out_dialog_location, null);

        // Get user input edittext and button ui controls in the popup dialog.
        location1 = (EditText) popupInputDialogView.findViewById(R.id.location1);
        location2 = (EditText) popupInputDialogView.findViewById(R.id.location2);
        location3 = (EditText) popupInputDialogView.findViewById(R.id.location3);
        set_btn = popupInputDialogView.findViewById(R.id.button_set);
        cancel_btn = popupInputDialogView.findViewById(R.id.button_cancel_user_data);

        // Display values from the main activity list view in user input edittext.
        initEditLocationInPopupDialog();
    }

    /* Get current user data from listview and set them in the popup dialog edittext controls. */
    private void initEditLocationInPopupDialog()
    {
        List<String> locationDataList = getExistUserDataInListView(locationListView);

        if(locationDataList.size() == 3)
        {
            String location_i_1 = locationDataList.get(0);

            String location_i_2 = locationDataList.get(1);

            String location_i_3 = locationDataList.get(2);

            if(location1 != null)
            {
                location1.setText(location_i_1);
            }

            if(location2 != null)
            {
                location2.setText(location_i_2);
            }

            if(location3 != null)
            {
                location3.setText(location_i_3);
            }
        }
    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initPopupViewControlsParkingTime()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.pop_out_dialog_time, null);

        // Get user input edittext and button ui controls in the popup dialog.
        parkingduration = (EditText) popupInputDialogView.findViewById(R.id.parking_time);
        set_btn = popupInputDialogView.findViewById(R.id.button_set);
        cancel_btn = popupInputDialogView.findViewById(R.id.button_cancel_user_data);

        // Display values from the main activity list view in user input edittext.
        initEditParkingTimeInPopupDialog();
    }

    /* Get current user data from listview and set them in the popup dialog edittext controls. */
    private void initEditParkingTimeInPopupDialog()
    {
        List<String> timeDataList = getExistUserDataInListView(locationListView);

        if(timeDataList.size() == 3)
        {
            String time_i_1 = timeDataList.get(0);

            String time_i_2 = timeDataList.get(1);

            if(parkingduration != null)
            {
                parkingduration.setText(time_i_1);
            }
        }
    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initPopupViewControlsCarDetails()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.pop_up_dialog_car, null);

        // Get user input edittext and button ui controls in the popup dialog.
        cardetail1 = (EditText) popupInputDialogView.findViewById(R.id.plate_number);
        cardetail2 = (EditText) popupInputDialogView.findViewById(R.id.car_color);
        set_btn = popupInputDialogView.findViewById(R.id.button_set);
        cancel_btn = popupInputDialogView.findViewById(R.id.button_cancel_user_data);

        // Display values from the main activity list view in user input edittext.
        initEditCarDetailInPopupDialog();
    }

    /* Get current user data from listview and set them in the popup dialog edittext controls. */
    private void initEditCarDetailInPopupDialog()
    {
        List<String> locationDataList = getExistUserDataInListView(locationListView);

        if(locationDataList.size() == 3)
        {
            String car_i_1 = locationDataList.get(0);

            String car_i_2 = locationDataList.get(1);

            if(cardetail1 != null)
            {
                cardetail1.setText(car_i_1);
            }

            if(cardetail2 != null)
            {
                cardetail2.setText(car_i_2);
            }
        }
    }

    /* If user data exist in the listview then retrieve them to a string list. */
    private List<String> getExistUserDataInListView(ListView listView)
    {
        List<String> ret = new ArrayList<String>();

        if(listView != null)
        {
            ListAdapter listAdapter = listView.getAdapter();

            if(listAdapter != null) {

                int itemCount = listAdapter.getCount();

                for (int i = 0; i < itemCount; i++) {
                    Object itemObject = listAdapter.getItem(i);
                    HashMap<String, String> itemMap = (HashMap<String, String>)itemObject;

                    Set<String> keySet = itemMap.keySet();

                    Iterator<String> iterator = keySet.iterator();

                    String key = iterator.next();

                    String value = itemMap.get(key);

                    ret.add(value);
                }
            }
        }

        return ret;
    }
}