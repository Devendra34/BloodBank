package com.example.bloodbank.Activity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import dmax.dialog.SpotsDialog;

public class UserLogin extends AppCompatActivity {

    private EditText userId,userPw;
    private FirebaseFirestore db;
    private String email,pw;
    private AlertDialog spotsBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        init();
    }

    public void init(){
        userId = (EditText)findViewById(R.id.userId);
        userPw = (EditText)findViewById(R.id.userPw);
        db = FirebaseFirestore.getInstance();
        spotsBox = new SpotsDialog(this);
    }

    public void userLogin(View view) {

        setEmail(userId.getText().toString().trim());
        setPw(userPw.getText().toString().trim());
        if (areAllFieldsComplete()){
            checkForUserEmail();
        }
    }

    private void checkForUserEmail(){
        spotsBox.show();
        db.collection("All Users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Boolean isFound = false;
                        for(DocumentSnapshot ds: queryDocumentSnapshots){
                            if(getEmail().equals(ds.get("email_id").toString())){
                                siginUser(getEmail(),getPw());
                                isFound = true;
                                break;
                            }
                        }
                        if(!isFound){
                            makeToast("You have not Registerd as a User");
                            spotsBox.cancel();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(spotsBox.isShowing())
                            spotsBox.cancel();
                        makeToast("Process Failed");
                    }
                });

    }


    private void siginUser(String email, String pw) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pw)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        makeToast("User Signed Successfully");
                        if(spotsBox.isShowing())
                            spotsBox.cancel();

                        startActivity(new Intent(UserLogin.this,UserDashboard.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(spotsBox.isShowing())
                            spotsBox.cancel();
                        makeToast(e.getMessage());
                    }
                });
    }

    private boolean areAllFieldsComplete(){
        if(TextUtils.isEmpty(this.email)){
            makeToast("Please Enter an Email id");
            return false;
        }
        if(TextUtils.isEmpty(this.pw)){
            makeToast("Please Enter an Email id");
            return false;
        }
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void goToInstituteLoginActivity(View view) {
        startActivity(new Intent(UserLogin.this,AdminLogin.class));
    }


    public void goToUserRegActivity(View view){

        startActivity(new Intent(UserLogin.this,UserRegister.class));
    }
}
