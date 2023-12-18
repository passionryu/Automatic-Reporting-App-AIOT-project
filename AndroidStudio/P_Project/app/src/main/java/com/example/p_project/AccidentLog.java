package com.example.p_project;

import com.google.android.gms.maps.model.LatLng;

public class AccidentLog {
    private String idToken;
    //private String photo;
    private String date;
    private int speed1, speed2, tilt_z;
    private  double latitude, longitude;

    //public void setPhoto(String photo) {
//        this.photo = photo;
//    }
    public  AccidentLog(RawLog raw, String idToken, double latitude, double longitude){
        this.idToken=idToken;
        this.date= raw.getDate();
        this.speed1=raw.getSpeed1();
        this.speed2= raw.getSpeed2();
        this.tilt_z= raw.getTilt_z();
        this.latitude=latitude;
        this.longitude=longitude;
    }
    public AccidentLog() {
        // 빈(default) 생성자 추가
    }
    public void setSpeed1(int speed1) {
        this.speed1 = speed1;
    }

    public void setSpeed2(int speed2) {
        this.speed2 = speed2;
    }

//    public String getPhoto() {
//        return photo;
//    }

    public int getSpeed1() {
        return speed1;
    }

    public int getSpeed2() {
        return speed2;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIdToken() {
        return idToken;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTilt_z(int tilt_z) {
        this.tilt_z = tilt_z;
    }

    public String getDate() {
        return date;
    }

    public int getTilt_z() {
        return tilt_z;
    }

    public AccidentLog(String date) {
        this.date = date;
    }
}

class RawLog{
    private String date;
    private double latitude, longitude;
    private int Speed1, Speed2, tilt_z;

    public RawLog(){}

    public void setDate(String date) {
        this.date = date;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSpeed1(int speed1) {
        Speed1 = speed1;
    }

    public void setSpeed2(int speed2) {
        Speed2 = speed2;
    }

    public void setTilt_z(int tilt_z) {
        this.tilt_z = tilt_z;
    }

    public String getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getSpeed1() {
        return Speed1;
    }

    public int getSpeed2() {
        return Speed2;
    }

    public int getTilt_z() {
        return tilt_z;
    }
}
