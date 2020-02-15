package com.example.bloodbank.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
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

public class AdminLogin extends AppCompatActivity {

    private EditText mEmail,mPw;
    private FirebaseFirestore db;
    private String email,pw;
    private AlertDialog spotsBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        init();
    }

    public void adminSignIn(View view) {

        hideSoftKeyboard();

        setEmail(mEmail.getText().toString().trim());
        setPw(mPw.getText().toString().trim());

        if(areAllFieldsComplete()){
            checkForAdminEmail();
        }
    }

    private void init(){
        mEmail = (EditText)findViewById(R.id.adId);
        mPw = (EditText)findViewById(R.id.adPw);
        db = FirebaseFirestore.getInstance();
        spotsBox = new SpotsDialog(this);
    }

    private void checkForAdminEmail(){
        spotsBox.show();
        db.collection("All Institute")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Boolean isFound = false;
                             for(DocumentSnapshot ds: queryDocumentSnapshots) {
                                 if (ds.get("instituteEmailId") != null) {
                                     if (getEmail().equals(ds.get("instituteEmailId").toString())) {

                                         //Admin Found
                                         siginAdmin(getEmail(), getPw());
                                         isFound = true;
                                         break;
                                     }
                                 }
                             }
                        if (!isFound) {
                            makeToast("You have not Registerd as Admin");
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

    private void siginAdmin(String email, String pw) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pw)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        makeToast("Admin Signed Successfully");
                        if(spotsBox.isShowing())
                            spotsBox.cancel();

                        startActivity(new Intent(AdminLogin.this,UserDashboard.class));
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
            makeToast("Please Enter Password");
            return false;
        }
        return true;
    }
    void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    public void goToUserLogin(View view) {
        startActivity(new Intent(AdminLogin.this,UserLogin.class));
        finish();
    }

    public void goToAdminReg(View view) {
        startActivity(new Intent(AdminLogin.this,AdminRegister.class));
        finish();
    }


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}