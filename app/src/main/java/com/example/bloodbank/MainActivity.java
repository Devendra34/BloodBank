package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.bloodbank.Activity.AdminLogin;
import com.example.bloodbank.Activity.AdminRegister;
import com.example.bloodbank.Activity.UserDashboard;
import com.example.bloodbank.Activity.UserProfile;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, UserDashboard.class));
        finish();
    }
}
