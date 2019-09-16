package com.example.bloodbank.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank.Login_Signup.UserLogin;
import com.example.bloodbank.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Donate extends AppCompatActivity {


    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY = "key";
    private static final String ID = "id";
    private Spinner sCntry,sState,sCity,sInstiName;
    private String toInstituteId = null;
    ArrayList<String> idlist = null;
    ArrayList<String> instituteList = null;
    private String state,country,city,instituteName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    sCity.setEnabled(false);
                    sInstiName.setEnabled(false);
                    setCity(null);
                    setInstituteName(null);
                    setState(null);
                }
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
                    sInstiName.setEnabled(false);
                    sCity.setEnabled(true);
                    setCity(null);
                    setInstituteName(null);
                }
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
                    Toast.makeText(Donate.this, id, Toast.LENGTH_SHORT).show();
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
    }

    public void setCountrySpinner(){
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
                    }
                });
        sCntry.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cntrylist));

    }

    public void setStateSpinner(String a) {
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
                    }
                });
        sState.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,stateList));
    }

    public void setCitySpinner(String a) {
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
                    }
                });
        sCity.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cityList));
    }

    public void setInstituteSpinner(String a) {

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
        if(FirebaseAuth.getInstance().getCurrentUser() != null && this.toInstituteId != null){
            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("All Users").document(FirebaseAuth.getInstance().getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                UserData userData = documentSnapshot.toObject(UserData.class);
                                DocumentReference documentReference = firebaseFirestore.collection("All Institute").document(toInstituteId);
                                Map<String, Object> map = new HashMap<>();
                                map.put(ID, toInstituteId);
                                documentReference.set(map);
                                documentReference.collection("Requests Received").document(FirebaseAuth.getInstance().getUid()).set(userData);
                                Toast.makeText(Donate.this, "done sending", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Donate.this, "Cant find user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Donate.this, "user data failure", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            startActivity(new Intent(Donate.this, UserLogin.class));
            Toast.makeText(this, "Please Login account", Toast.LENGTH_SHORT).show();
        }
    }
}