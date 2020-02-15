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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.Users.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Donate extends AppCompatActivity {


    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY = "key";
    private static final String ID = "id";
    private Spinner sCntry,sState,sCity,sInstiName;
    private String toInstituteId = null;
    ArrayList<String> idlist = null;
    ArrayList<String> instituteList = null;
    private String state,country,city,instituteName;
    private View mParentLayout;
    private AlertDialog spotsBox;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        init();
        // To get names of country
        setCountrySpinner();

       sCntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().equals("Choose Country")) {
                    setCountry(adapterView.getItemAtPosition(i).toString());
                    setStateSpinner(adapterView.getItemAtPosition(i).toString());
                    sState.setEnabled(true);
                } else{
                    sState.setEnabled(false);
                    setCountry(null);
                }
                sState.setSelection(0);
                setState(null);

                sCity.setEnabled(false);
                sCity.setSelection(0);
                setCity(null);

                setInstituteName(null);
                sInstiName.setEnabled(false);
                sInstiName.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Choose State")) {
                    setState(adapterView.getItemAtPosition(i).toString());
                    setCitySpinner(adapterView.getItemAtPosition(i).toString());
                    sCity.setEnabled(true);
                } else {
                    sCity.setEnabled(false);
                    setState(null);
                }
                sCity.setSelection(0);
                setCity(null);

                setInstituteName(null);
                sInstiName.setEnabled(false);
                sInstiName.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Choose City")) {
                    setCity(adapterView.getItemAtPosition(i).toString());
                    setInstituteSpinner(adapterView.getItemAtPosition(i).toString());
                    sInstiName.setEnabled(true);
                }
                else {
                    setInstituteName(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sInstiName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Choose Institute")) {
                    setInstituteName(adapterView.getItemAtPosition(i).toString());
                    String id = idlist.get(instituteList.indexOf(adapterView.getItemAtPosition(i).toString()));
                    saveInstituteId(id);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void init(){
        sCntry = (Spinner)findViewById(R.id.d_cntry);
        sState = (Spinner)findViewById(R.id.d_state);
        sCity = (Spinner)findViewById(R.id.d_city);
        sInstiName = (Spinner)findViewById(R.id.d_InstituteName);
        mParentLayout = findViewById(android.R.id.content);
        spotsBox = new SpotsDialog(this);

    }

    public void setCountrySpinner(){
        spotsBox.show();
        CollectionReference collectionReference = db.collection("Country");
        final ArrayList<String> cntrylist = new ArrayList<>();
        cntrylist.add("Choose Country");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            cntrylist.add(documentSnapshot.getString(KEY));
                        }
                        spotsBox.cancel();
                    }
                });
        sCntry.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cntrylist));

    }

    public void setStateSpinner(String a) {
        spotsBox.show();
        final ArrayList<String> stateList = new ArrayList<>();
        stateList.add("Choose State");
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        CollectionReference collectionReference = db.collection("Country").document(country).collection("State");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            stateList.add(documentSnapshot.getString(KEY));
                        }
                        spotsBox.cancel();
                    }
                });
        sState.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,stateList));

    }

    public void setCitySpinner(String a) {
        spotsBox.show();
        final ArrayList<String> cityList = new ArrayList<>();
        cityList.add("Choose City");
        CollectionReference collectionReference = db.collection("Country").document(country).collection("State")
            .document(state).collection("City");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            cityList.add(documentSnapshot.getString(KEY));
                        }
                        spotsBox.cancel();
                    }
                });
        sCity.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cityList));

    }

    public void setInstituteSpinner(String a) {
        spotsBox.show();
        final ArrayList<String> idlist = new ArrayList<>();
        final ArrayList<String> instituteList = new ArrayList<>();
        instituteList.add("Choose Institute");
        idlist.add("x");
        CollectionReference collectionReference = db.collection("Country").document(country).collection("State")
                .document(state).collection("City").document(city).collection("Institute Name");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            instituteList.add(documentSnapshot.getString(KEY));
                            idlist.add(documentSnapshot.getString(ID));
                            Toast.makeText(Donate.this, documentSnapshot.getString(ID), Toast.LENGTH_SHORT).show();
                        }
                        spotsBox.cancel();
                    }
                });
        this.idlist = idlist;
        this.instituteList = instituteList;
        sInstiName.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,instituteList));

    }

    public void saveInstituteId(String id){
        this.toInstituteId = id;
    }
    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public void sendData(View view) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            if(checkAllFields()) {
                spotsBox.show();
                final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("All Users").document(FirebaseAuth.getInstance().getUid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    UserData userData = documentSnapshot.toObject(UserData.class);
                                    userData.timestamp = null;
                                    firebaseFirestore.collection("All Institute").document(toInstituteId).
                                    collection("Requests Received").document(FirebaseAuth.getInstance().getUid()).set(userData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    makeSnackBarMessage("Request to Donate Blood has been sent.");
                                                    if(spotsBox.isShowing())
                                                        spotsBox.cancel();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            makeSnackBarMessage(e.getMessage());
                                            if(spotsBox.isShowing())
                                                spotsBox.cancel();
                                        }
                                    });
                                } else {
                                    Toast.makeText(Donate.this, "Cant find user data", Toast.LENGTH_SHORT).show();
                                    if(spotsBox.isShowing())
                                        spotsBox.cancel();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Donate.this, "user data failure", Toast.LENGTH_SHORT).show();
                                if(spotsBox.isShowing())
                                    spotsBox.cancel();
                            }
                        });
            }
        } else {
            startActivity(new Intent(Donate.this, UserLogin.class));
            Toast.makeText(this, "Please Login account", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean checkAllFields(){
        if(TextUtils.isEmpty(this.country)){
            makeSnackBarMessage("Please Select a Country");
            return false;
        }else if(TextUtils.isEmpty(this.state)){
            makeSnackBarMessage("Please Select a State");
            return false;
        }else if(TextUtils.isEmpty(this.city)){
            makeSnackBarMessage("Please Select a City");
            return false;
        }else if(TextUtils.isEmpty(this.instituteName)) {
            makeSnackBarMessage("Please Select an Institute");
            return false;
        } else
            return true;
    }

    public void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}