package com.example.bloodbank.Login_Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.Users.Donate;
import com.example.bloodbank.Users.RequestBlood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class AdminRegister extends AppCompatActivity {

    private static final String KEY = "key";
    private static final String ID = "id";
    private EditText adName, adPw,adConfPw,adEamil,adState,adCity,adCntry;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        init();
    }
    private void init(){
        adName = (EditText)findViewById(R.id.adname);
        adEamil = (EditText)findViewById(R.id.ademail);
        adConfPw = (EditText)findViewById(R.id.adConfRgPw);
        adPw = (EditText)findViewById(R.id.adRgPw);
        adState = (EditText)findViewById(R.id.adstate);
        adCity = (EditText)findViewById(R.id.adcity);
        adCntry = (EditText)findViewById(R.id.adCntry); 
        progressDialog = new ProgressDialog(this);
    }

    public void AdSignUp(View view) {
        final String name,email,pw,cpw,state,city,cntry;
        
        name = adName.getText().toString().trim();
        email = adEamil.getText().toString().trim();
        pw = adPw.getText().toString().trim();
        cpw = adConfPw.getText().toString().trim();
        state = adState.getText().toString().toUpperCase().trim();
        city = adCity.getText().toString().toUpperCase().trim();
        cntry = adCntry.getText().toString().toUpperCase().trim();
        
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter a valid email id",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pw)){
            Toast.makeText(this,"Please enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(cpw) || !cpw.equals(pw) ){
            Toast.makeText(this,"Please confirm same Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter name of the Institute",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(cntry)){
            Toast.makeText(this,"Please enter name of Country",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(state)){
            Toast.makeText(this,"Please enter Name of State",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(city)){
            Toast.makeText(this,"Please enter Name of City",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Creating Account....");
        progressDialog.show();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DocumentReference dr;
                            Map<String,Object> map = new HashMap<>();
                            dr = db.collection("Country").document(cntry);
                            map.put(KEY,cntry);
                            dr.set(map);
                            map.clear();
                            dr = dr.collection("State").document(state);
                            map.put(KEY,state);
                            dr.set(map);
                            map.clear();
                            dr = dr.collection("City").document(city);
                            map.put(KEY,city);
                            dr.set(map);
                            map.clear();
                            dr = dr.collection("Institute Name").document(firebaseAuth.getUid());
                            map.put(KEY,name);
                            map.put(ID,firebaseAuth.getUid());
                            dr.set(map);
                            map.clear();
                            progressDialog.cancel();
                            Toast.makeText(AdminRegister.this, "Registered Sucessfully", Toast.LENGTH_LONG).show();
                        } else{
                            progressDialog.cancel();
                            Toast.makeText(AdminRegister.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Temp(View view) {
        startActivity(new Intent(AdminRegister.this, Donate.class));
    }

    public void fromAdRegToUserLogin(View view) {
        startActivity(new Intent(AdminRegister.this,UserLogin.class));
    }
}
