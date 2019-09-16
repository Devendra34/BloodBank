package com.example.bloodbank.Login_Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.Users.UserDashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class UserLogin extends AppCompatActivity {

    private EditText userId,userPw;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        init();
    }

    public void init(){
        userId = (EditText)findViewById(R.id.userId);
        userPw = (EditText)findViewById(R.id.userPw);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void userLogin(View view) {
        final AlertDialog alertDialog = new SpotsDialog(this);
        alertDialog.show();
        String id,pw;
        id = userId.getText().toString().trim();
        pw = userPw.getText().toString().trim();
        if(TextUtils.isEmpty(id)){
            Toast.makeText(this, "Please enter email id", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }
        if(TextUtils.isEmpty(pw)){
            Toast.makeText(this, "Please enter email id", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(id,pw).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                alertDialog.cancel();
                Toast.makeText(UserLogin.this, "Signed in Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserLogin.this, UserDashboard.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.cancel();
                Toast.makeText(UserLogin.this, "SignIn Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToInstituteLoginActivity(View view) {
        startActivity(new Intent(UserLogin.this,AdminLogin.class));
    }


    public void goToUserRegActivity(View view){

        startActivity(new Intent(UserLogin.this,UserRegister.class));
    }
}
