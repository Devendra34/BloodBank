package com.example.bloodbank.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
    }

    public void goToDonate(View view) {
        startActivity(new Intent(UserDashboard.this,Donate.class));
    }

    public UserDashboard() {
    }

    public void updateProfile(View view) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            startActivity(new Intent(UserDashboard.this,UserProfile.class));
        else
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
    }
    public void goToUserLogin(View view){
        startActivity(new Intent(this,UserLogin.class));
    }
    public void goToUserReg(View view){
        startActivity(new Intent(this,UserRegister.class));
    }
    public void goToAdminLogin(View view){
        startActivity(new Intent(this,AdminLogin.class));
    }
    public void goToAdminReg(View view){
        startActivity(new Intent(this,AdminRegister.class));
    }
    public void goToRequestBlood(View view){
        startActivity(new Intent(this,RequestBlood.class));
    }

    public void goToVerifiedRequests(View view) {
        startActivity(new Intent(this,VerifiedRequests.class));
    }
}
