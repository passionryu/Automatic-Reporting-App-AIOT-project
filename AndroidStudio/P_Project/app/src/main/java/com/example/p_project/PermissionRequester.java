package com.example.p_project;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

public class PermissionRequester {

    // 위치 정보 접근 권한 요청 코드
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // SMS 권한 요청 코드
    public static final int SMS_PERMISSION_REQUEST_CODE = 1002;

    private Activity activity;
    public static class granted{
        public static boolean sms,location;
    }

    public PermissionRequester(Activity activity) {
        this.activity = activity;
    }

    // 위치 정보 접근 권한 요청
    public void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우 권한 요청
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    // SMS 권한 요청
    public void requestSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우 권한 요청
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.SEND_SMS},
                        SMS_PERMISSION_REQUEST_CODE);
            }
        }
    }

    // 위치 정보 및 SMS 권한 한 번에 요청
    public void requestLocationAndSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 권한이 없는 경우 권한 요청
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.SEND_SMS
                        },
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
