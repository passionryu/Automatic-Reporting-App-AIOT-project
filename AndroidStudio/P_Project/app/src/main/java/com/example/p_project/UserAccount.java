package com.example.p_project;

import android.util.Log;

import java.util.ArrayList;

public class UserAccount {
    String idToken;
    String emailID;
    String password;
    String username;
    String phonenumber;
    Boolean call_onoff=true;
    Boolean isAdmin=false;
    ArrayList<String> calling_list=new ArrayList<String>();

    public UserAccount() {

    }

    public void setCalling_list(ArrayList<String> calling_list) {
        this.calling_list = calling_list;
    }

    public ArrayList<String> getCalling_list() {
        return calling_list;
    }

    public void setAdmin(Boolean admin) {

        isAdmin = admin;
    }

    public void setCall_onoff(Boolean call_onoff) {
        this.call_onoff = call_onoff;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public Boolean getCall_onoff() {
        return call_onoff;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getIdToken() {
        return idToken;
    }

    public void printLogCat(){
        Log.d("account",this.idToken);
        Log.d("account",this.emailID);
        Log.d("account",this.password);
        Log.d("account",this.username);
        Log.d("account",this.phonenumber);
        Log.d("account",this.call_onoff.toString());
        Log.d("account",this.calling_list.toString());
    }
}
