package com.example.bloodbank.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank.Admins.AdminData;
import com.example.bloodbank.R;

import com.example.bloodbank.ViewHolders.VerifiedRequestsHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestBlood extends AppCompatActivity {

    private Spinner mBgrp;
    private RecyclerView rv;
    private FirebaseFirestore db;
    private FirebaseUser auth;
    private Map<String,String> map;
    private String bgrpCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        init();



        mBgrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = adapterView.getItemAtPosition(i).toString();
                if(!temp.equals("Choose Blood Group")){
                    setBgrpCode(map.get(temp));
                } else {
                    setBgrpCode(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private void init(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance().getCurrentUser();

        rv = (RecyclerView)findViewById(R.id.rrv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mBgrp = (Spinner)findViewById(R.id.r_bgrp);

        map = new HashMap<>();
        map.put("A+","a_plus");
        map.put("B+","b_plus");
        map.put("AB+","ab_plus");
        map.put("O+","o_plus");
        map.put("A-","a_minus");
        map.put("B-","b_minus");
        map.put("AB-","ab_minus");
        map.put("O-","o_minus");

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
        ArrayAdapter<String> a = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,bgList);
        mBgrp.setAdapter(a);

    }
    private void getListofbBanks(String bgrpCode){


        Query query = db.collection("All Institute")
                .whereGreaterThanOrEqualTo(bgrpCode,1);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<AdminData> options = new FirestorePagingOptions.Builder<AdminData>()
                .setLifecycleOwner(this)
                .setQuery(query,config,AdminData.class)
                .build();
        FirestorePagingAdapter<AdminData, VerifiedRequestsHolder> firestoreAdapter =
                new FirestorePagingAdapter<AdminData, VerifiedRequestsHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VerifiedRequestsHolder holder, final int position, @NonNull final AdminData model) {
                        holder.rrEmail.setText(model.getInstituteName());
                        holder.rrText.setText(model.getCity()+"," +model.getState()+","+model.getCountry());
                        holder.profileImg.setVisibility(View.GONE);
                        holder.rrDt.setVisibility(View.GONE);
                    }

                    @NonNull
                    @Override
                    public VerifiedRequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new VerifiedRequestsHolder(LayoutInflater.from(RequestBlood.this).inflate(R.layout.received_requests_holder,parent,false));
                    }
                };
        rv.setAdapter(firestoreAdapter);
    }

    public String getBgrpCode() {
        return bgrpCode;
    }

    public void setBgrpCode(String bgrpCode) {
        this.bgrpCode = bgrpCode;
    }

    public void searchBb(View view) {
        if(getBgrpCode() != null){
            getListofbBanks(getBgrpCode());
        } else
            Toast.makeText(this, "Please select Blood Group", Toast.LENGTH_SHORT).show();
    }
}
