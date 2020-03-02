package com.example.bloodbank.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.Users.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class UserProfile extends AppCompatActivity {

    TextView name,ph,email,bg;
    private UserData userData = null;
    AlertDialog dialogspots;
    public String editBgrp = null;
    private Spinner mBgrp;
    private FirebaseFirestore db;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        db = FirebaseFirestore.getInstance();
        dialogspots = new SpotsDialog(this);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            checkForUserEmail(auth.getCurrentUser().getUid());
        } else {
            makeToast("Public User Sign in Required");
            finish();
        }
        name = (TextView)findViewById(R.id.pf_name);
        ph = (TextView)findViewById(R.id.pf_ph);
        email = (TextView)findViewById(R.id.pf_email);
        bg = (TextView)findViewById(R.id.pf_bg);
        setUserData(null);

        getData();
    }

    private void getData(){
        final AlertDialog dialogspots = new SpotsDialog(this);
        dialogspots.show();
        FirebaseFirestore.getInstance()
                .collection("All Users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            UserData userData1 = task.getResult().toObject(UserData.class);
                            setData(userData1);
                            setUserData(userData1);
                        }
                        dialogspots.cancel();
                    }
                });
    }

    private void setData(UserData userData){
        if(userData != null) {
            name.setText(userData.getUser_name());
            ph.setText(userData.getPhone_No());
            email.setText(userData.getEmail_id());
            bg.setText(userData.bloodGroup);
        }
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    private void updateData(UserData userData){


        final AlertDialog dialogspots = new SpotsDialog(this);
        dialogspots.show();

        db.collection("All Users").document(FirebaseAuth.getInstance().getUid()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserProfile.this,"Updated",Toast.LENGTH_SHORT).show();

                        dialogspots.cancel();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this,"Failed to upload",Toast.LENGTH_SHORT).show();

                        dialogspots.cancel();
                    }
                });

        getData();
    }

    public void editData(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.edit_box,null);

        TextView heading = (TextView)view.findViewById(R.id.edHeading);
        final EditText inText = (EditText)view.findViewById(R.id.edText);
        Button ok = (Button)view.findViewById(R.id.ok);
        Button cancel = (Button)view.findViewById(R.id.cancel);
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

        mBgrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Choose Blood Group"))
                    setEditBgrp(adapterView.getItemAtPosition(i).toString());
                else
                    setEditBgrp(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);


        inText.setVisibility(View.VISIBLE);

        if(v.getId() == R.id.edit_Name){
            inText.setText(getUserData().getUser_name());
            heading.setText("Enter Name");
        }
        else if(v.getId() == R.id.edit_Ph){
            inText.setText(getUserData().getPhone_No());
            heading.setText("Enter Phone Number");
        }
        else {
            inText.setVisibility(View.GONE);
            mBgrp.setVisibility(View.VISIBLE);
            heading.setText("Enter Blood Group");
        }

        inText.selectAll();
        inText.requestFocus();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                String temp = null;

                if(v.getId() == R.id.edit_Name){
                    temp = inText.getText().toString();
                    getUserData().setUser_name(temp);
                }
                else if(v.getId() == R.id.edit_Ph){
                    temp = inText.getText().toString();
                    getUserData().setPhone_No(temp);
                }
                else {
                    if(getEditBgrp() != null) {
                        temp = getEditBgrp();
                        getUserData().setBloodGroup(temp);
                    }
                }
                if(temp != null) {
                    updateData(getUserData());
                    alertDialog.cancel();
                } else
                    Toast.makeText(UserProfile.this, "Please enter appropriate data", Toast.LENGTH_SHORT).show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                if(v.getId() == R.id.edit_Name){

                }
            }
        });

        alertDialog.show();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public String getEditBgrp() {
        return editBgrp;
    }

    public void setEditBgrp(String editBgrp) {
        this.editBgrp = editBgrp;
    }
    private void checkForUserEmail(final String uid){
        dialogspots.show();
        db.collection("All Users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Boolean isFound = false;
                        for(DocumentSnapshot ds: queryDocumentSnapshots){
                            if(uid.equals(ds.get("uid").toString())){
                                isFound = true;
                                break;
                            }
                        }
                        if(!isFound){
                            makeToast("Public User Sign in Required");
                            dialogspots.cancel();
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(dialogspots.isShowing())
                            dialogspots.cancel();
                        makeToast("Process Failed");
                    }
                });

    }
    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
