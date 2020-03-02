package com.example.bloodbank.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodbank.Adapters.ILoadMore;
import com.example.bloodbank.Adapters.VerifiedRequestsAdapter;
import com.example.bloodbank.Admins.AdminData;
import com.example.bloodbank.R;
import com.example.bloodbank.Users.UserData;
import com.example.bloodbank.ViewHolders.VerifiedRequestsHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class VerifiedRequests extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, ILoadMore {

    private FirebaseFirestore db = null;
    private CollectionReference instituteCollectionRef = null;
    private View mParentLayout;
    private ArrayList<UserData> userDataList = null;
    private RecyclerView rv = null;
    private VerifiedRequestsAdapter adapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirestorePagingOptions options;
    private FirestorePagingAdapter<UserData,VerifiedRequestsHolder> firestoreAdapter = null;
    private AlertDialog spotsBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_requests);

        spotsBox = new SpotsDialog(this);
        rv = findViewById(R.id.rrrv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mParentLayout = findViewById(android.R.id.content);
        userDataList = new ArrayList<>();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setUpFirebase();

        this.firestoreAdapter = setFirestoreAdapter();

    }

    public FirestorePagingAdapter setFirestoreAdapter() {

        Query query = instituteCollectionRef
                .orderBy("timestamp", Query.Direction.ASCENDING);


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<UserData> options = new FirestorePagingOptions.Builder<UserData>()
                .setLifecycleOwner(this)
                .setQuery(query,config,UserData.class)
                .build();

        this.options = options;

            FirestorePagingAdapter<UserData, VerifiedRequestsHolder> firestoreAdapter =
                    new FirestorePagingAdapter<UserData, VerifiedRequestsHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull VerifiedRequestsHolder holder, final int position, @NonNull final UserData model) {
                            holder.rrEmail.setText(model.getEmail_id());
                            holder.rrText.setText(model.getUser_name());
                            if(model.timestamp !=null)
                                holder.rrDt.setText(model.timestamp.toString());
                            holder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(VerifiedRequests.this,SingleUserData.class);
                                    intent.putExtra("data",new String[]{
                                            model.getUser_name(),model.getEmail_id(),
                                            model.getPhone_No(),model.getBloodGroup(),
                                            model.getUid(),model.timestamp.toString()
                                    });
                                    startActivity(intent);
                                }
                            });

                        }

                        @NonNull
                        @Override
                        public VerifiedRequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            return new VerifiedRequestsHolder(LayoutInflater.from(VerifiedRequests.this).inflate(R.layout.received_requests_holder,parent,false));
                        }
                    };

        rv.setAdapter(firestoreAdapter);
            return firestoreAdapter;

    }

    private void setUpFirebase(){
        db = FirebaseFirestore.getInstance();
        instituteCollectionRef = db.collection("All Institute").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Requests Received");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(firestoreAdapter != null){
            //firestoreAdapter.notifyDataSetChanged();
            setFirestoreAdapter().notifyDataSetChanged();
            Toast.makeText(this, "Remused", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(false);
        setFirestoreAdapter().notifyDataSetChanged();
        makeSnackBarMessage("Refreshing");
    }


    private void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        firestoreAdapter.notifyDataSetChanged();
    }
}
