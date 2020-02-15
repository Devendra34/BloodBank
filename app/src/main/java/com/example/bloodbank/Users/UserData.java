package com.example.bloodbank.Users;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserData {
    public String user_name = null;
    public String phone_No = null;
    public String email_id = null;
    public @ServerTimestamp Date timestamp;
    public String uid = null;
    public String bloodGroup = null;
    public UserData(){

    }

    public UserData(String user_name, String phone_No, String email_id, String uid, String bloodGroup) {
        this.user_name = user_name;
        this.phone_No = phone_No;
        this.email_id = email_id;
        this.uid = uid;
        this.bloodGroup = bloodGroup;
    }

    protected UserData(Parcel in) {
        user_name = in.readString();
        phone_No = in.readString();
        email_id = in.readString();
        uid = in.readString();
        bloodGroup = in.readString();
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone_No() {
        return phone_No;
    }

    public void setPhone_No(String phone_No) {
        this.phone_No = phone_No;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
