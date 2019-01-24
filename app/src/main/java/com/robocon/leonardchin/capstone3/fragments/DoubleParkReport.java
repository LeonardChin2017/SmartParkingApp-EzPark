package com.robocon.leonardchin.capstone3.fragments;

public class DoubleParkReport {

    public String latitude;
    public String longitude;
    public String car_plate;
    public String time;
    public String photoUrl;

    public DoubleParkReport(String latitude, String longitude, String car_plate, String time, String photoUrl) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.car_plate = car_plate;
        this.time = time;
        this.photoUrl = photoUrl;
    }

    public DoubleParkReport() {

    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setcarPlate(String car_plate) {
        this.car_plate = car_plate;
    }

    public String getCar_plate() {
        return car_plate;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}