package com.example.bloodbank.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Admins.AdminData;
import com.example.bloodbank.R;
import com.example.bloodbank.Users.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class SingleUserData extends AppCompatActivity {

    private TextView mName,mEmail,mBgrp,mPh,mTime;
    private String name = null;
    private String uid = null;
    private String email =null;
    private String ph = null;
    private String brp = null;
    private String time = null;
    private FirebaseFirestore db;
    private FirebaseUser auth;
    private UserData userData;
    private AlertDialog spotsBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user_data);

        init();

        String data[] = getIntent().getStringArrayExtra("data");
        if(data != null && data.length == 6) {
            setName(data[0]);
            setEmail(data[1]);
            setPh(data[2]);
            setBrp(data[3]);
            setUid(data[4]);
            setTime(data[5]);
        }
        setData();
    }

    private void init() {
        mName = findViewById(R.id.suName);
        mBgrp = findViewById(R.id.suBgrp);
        mEmail = findViewById(R.id.suEmail);
        mPh = findViewById(R.id.suPh);
        mTime = findViewById(R.id.suTime);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance().getCurrentUser();

        spotsBox = new SpotsDialog(this);
    }

    private void setData(){
        mName.setText(getName());
        mEmail.setText(getEmail());
        mTime.setText(getTime());
        mPh.setText(getPh());
        mBgrp.setText(getBrp());
    }

    private void setVerified(){
        UserData userData = new UserData();
        userData.setUser_name(getName());
        userData.setEmail_id(getEmail());
        userData.setBloodGroup(getBrp());
        userData.timestamp = null;
        userData.setUid(getUid());
        setUserData(userData);

        if(getBrp() == null || getBrp().equals("Needs to be Tested"))
            checkBgrp();
        else
            upadateData();

    }

    private void upadateData(){
        spotsBox.show();
        db.collection("All Institute").document(auth.getUid())
                .collection("Requests Received").document(getUid())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("All Institute").document(auth.getUid())
                        .collection("Donated").document()
                        .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("All Institute").document(auth.getUid())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                AdminData adminData = documentSnapshot.toObject(AdminData.class);

                                if(userData.bloodGroup.equals("A+ve") || userData.bloodGroup.equals("A+")){
                                    adminData.setA_plus(adminData.getA_plus()+1);
                                } else if(userData.bloodGroup.equals("B+ve") || userData.bloodGroup.equals("B+")){
                                    adminData.setB_plus(adminData.getB_plus()+1);
                                } else if(userData.bloodGroup.equals("O+ve") || userData.bloodGroup.equals("O+")){
                                    adminData.setO_plus(adminData.getO_plus()+1);
                                } else if(userData.bloodGroup.equals("AB+ve") || userData.bloodGroup.equals("AB+")){
                                    adminData.setAb_plus(adminData.getAb_plus()+1);
                                } else if(userData.bloodGroup.equals("A-ve") || userData.bloodGroup.equals("A-")){
                                    adminData.setA_minus(adminData.getA_minus()+1);
                                } else if(userData.bloodGroup.equals("AB-ve") || userData.bloodGroup.equals("AB-")){
                                    adminData.setAb_minus(adminData.getAb_minus()+1);
                                } else if(userData.bloodGroup.equals("B-ve") || userData.bloodGroup.equals("B-")){
                                    adminData.setB_minus(adminData.getB_minus()+1);
                                } else if(userData.bloodGroup.equals("O-ve") || userData.bloodGroup.equals("O-")){
                                    adminData.setO_minus(adminData.getO_minus()+1);
                                } else {
                                    checkBgrp();
                                    return;
                                }

                                db.collection("All Institute").document(auth.getUid())
                                        .set(adminData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SingleUserData.this, "Donation Request Accepted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SingleUserData.this, "Donation Request failed to Accept", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                });
                spotsBox.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleUserData.this, "Failed\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        spotsBox.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });
    }


    private void checkBgrp(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.edit_box,null);

        TextView heading = (TextView)view.findViewById(R.id.edHeading);
        Button ok = (Button)view.findViewById(R.id.ok);
        Button cancel = (Button)view.findViewById(R.id.cancel);
        heading.setText("Select Appropriate Blood Group");

        final Spinner mBgrp = (Spinner)view.findViewById(R.id.EditBgrp);
        ArrayList<String> bgList = new ArrayList<>();
        bgList.add("Choose Blood Group");
        bgList.add("A+");
        bgList.add("B+");
        bgList.add("AB+");
        bgList.add("O+");
        bgList.add("A-");
        bgList.add("B-");
        bgList.add("AB-");
        bgList.add("O-");
        bgList.add("Needs to be Tested");
        ArrayAdapter<String> a = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,bgList);
        mBgrp.setAdapter(a);
        mBgrp.setVisibility(View.VISIBLE);

        mBgrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Choose Blood Group"))
                    setBrp(adapterView.getItemAtPosition(i).toString());
                else
                    setBrp(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = null;
                    if(getBrp() != null) {
                        temp = getBrp();
                        getUserData().setBloodGroup(temp);
                    }

                if(temp != null) {
                    upadateData();
                    alertDialog.cancel();
                } else
                    Toast.makeText(SingleUserData.this, "Please enter appropriate data", Toast.LENGTH_SHORT).show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getBrp() {
        return brp;
    }

    public void setBrp(String brp) {
        this.brp = brp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void seTitVerified(View view) {
        setVerified();
    }
}
