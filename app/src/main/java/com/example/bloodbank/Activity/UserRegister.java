package com.example.bloodbank.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.Users.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class UserRegister extends AppCompatActivity {

    private EditText eName,ePhno,eEmail,ePassword,eConfPw;
    private Spinner sBloodGrp;
    private FirebaseAuth firebaseAuth;
    public String bgrp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        init();

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
        sBloodGrp.setAdapter(a);

        sBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Choose Blood Group"))
                    bgrp = adapterView.getItemAtPosition(i).toString();
                else
                    bgrp = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void init() {
        eEmail = (EditText)findViewById(R.id.userRegEmail);
        eName = (EditText)findViewById(R.id.username);
        ePhno = (EditText)findViewById(R.id.userPhoneNo);
        ePassword = (EditText)findViewById(R.id.userRgPw);
        eConfPw= (EditText)findViewById(R.id.userConfRgPw);
        sBloodGrp = (Spinner)findViewById(R.id.bgrp);
    }

    public void registerUser(View view) {

        final AlertDialog alertDialog = new SpotsDialog(UserRegister.this);
        alertDialog.show();

        final String name,phone_No,email,password,confPw,bgrp;
        name = eName.getText().toString().trim();
        email = eEmail.getText().toString().trim();
        phone_No = ePhno.getText().toString().trim();
        password = ePassword.getText().toString().trim();
        confPw = eConfPw.getText().toString().trim();
        bgrp = this.bgrp;
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }
        if(TextUtils.isEmpty(phone_No)){
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }
        if(TextUtils.isEmpty(confPw) || !password.equals(confPw)){
            Toast.makeText(this, "Please confirm same password", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter email address", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }
        if(TextUtils.isEmpty(bgrp)){
            Toast.makeText(this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }
        if(TextUtils.isEmpty(confPw) || !password.equals(confPw)){
            Toast.makeText(this, "Please confirm same password", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserData userData = new UserData(name,phone_No,email,firebaseAuth.getUid(),bgrp);
                        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("All Users").document(firebaseAuth.getUid()).set(userData)
                                .addOnSuccessListener( new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserRegister.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                        startActivity(new Intent(UserRegister.this, UserDashboard.class));
                                    }
                                })
                                .addOnFailureListener( new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserRegister.this, "Registration Failed !!", Toast.LENGTH_SHORT).show();
                                        firebaseAuth.getCurrentUser().delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                alertDialog.cancel();
                                                Toast.makeText(UserRegister.this, "Recreate Account", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserRegister.this, "Registration Unsucceessfull\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                });

    }
}
