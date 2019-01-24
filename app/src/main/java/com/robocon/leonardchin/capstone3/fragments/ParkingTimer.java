package com.robocon.leonardchin.capstone3.fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.robocon.leonardchin.capstone3.R;
import com.gao.jiefly.nubiatimer.Timer;

import static android.content.Context.ALARM_SERVICE;

public class ParkingTimer extends Fragment{

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static ParkingTimer inst;
    private TextView alarmTextView;
    private int milisecondtotal;

    private int hour;
    private int min;
    private int second;

    public static ParkingTimer instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_parking_timer,container,false);
        final Timer timer    = v.findViewById(R.id.id_timer);
//        final EditText timeH = v.findViewById(R.id.id_time_hour_et);
//        final EditText timeM = v.findViewById(R.id.id_time_min__et);
//        final EditText timeS = v.findViewById(R.id.id_time_second_et);
//
//        final EditText timeH2 = v.findViewById(R.id.id_time_hour_et2);
//        final EditText timeM2 = v.findViewById(R.id.id_time_min__et2);
//        final EditText timeS2 = v.findViewById(R.id.id_time_second_et2);

        assert timer != null;

        String value = "0";
        try {
            value = getArguments().getString("park_time");
        }catch(Exception e){
            e.printStackTrace();
        }
        if (value.equals("30 mins")) {
            timer.setTime(0, 30, 0);
            timer.startTimer();
        } else if (value.equals("1 hrs")) {
            timer.setTime(1, 0, 0);
            timer.startTimer();
        } else if (value.equals("2 hrs")) {
            timer.setTime(2, 0, 0);
            timer.startTimer();
        } else if (value.equals("3 hrs")) {
            timer.setTime(3, 0, 0);
            timer.startTimer();
        } else if (value.equals("4 hrs")) {
            timer.setTime(4, 0, 0);
            timer.startTimer();
        } else if (value.equals("5 hrs")) {
            timer.setTime(5, 0, 0);
            timer.startTimer();
        } else if (value.equals("6 hrs")) {
            timer.setTime(6, 0, 0);
            timer.startTimer();
        } else if (value.equals("7 hrs")) {
            timer.setTime(7, 0, 0);
            timer.startTimer();
        } else if (value.equals("8 hrs")) {
            timer.setTime(8, 0, 0);
            timer.startTimer();
        }


        timer.setOnTimeUpListener(new Timer.OnTimeUpListener() {
            @Override
            public void onTimeUp() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getActivity(), "time up", Toast.LENGTH_SHORT).show();
                        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
                        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
                        alarmManager.set(AlarmManager.RTC, 1, pendingIntent);
                    }
                });
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, hour);
//                calendar.set(Calendar.MINUTE, min);
//                calendar.set(Calendar.SECOND, second);

//                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        Button btn = v.findViewById(R.id.id_stop_time_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stopTimer();
            }
        });
        v.findViewById(R.id.id_start_time_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.startTimer();
            }
        });

        v.findViewById(R.id.id_reset_time_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.resetTimer();
            }
        });


//        v.findViewById(R.id.id_set_time_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int hour = timeH.getText() == null || timeH.length()==0 ? 0 : Integer.valueOf(timeH.getText().toString());
//                int min = timeM.getText() == null || timeM.length()==0 ? 0 : Integer.valueOf(timeM.getText().toString());
//                int second = timeS.getText() == null || timeS.length()==0 ? 0 : Integer.valueOf(timeS.getText().toString());
//
//                timer.setTime(hour, min, second);
//            }
//        });

//        v.findViewById(R.id.id_set_time_btn2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hour = timeH2.getText() == null || timeH2.length()==0 ? 0 : Integer.valueOf(timeH2.getText().toString());
//                min = timeM2.getText() == null || timeM2.length()==0 ? 0 : Integer.valueOf(timeM2.getText().toString());
//                second = timeS2.getText() == null || timeS2.length()==0 ? 0 : Integer.valueOf(timeS2.getText().toString());
//
//                timer.setTime(hour, min, second);
//            }
//        });

        return v;
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}

