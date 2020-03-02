package com.example.bloodbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Activity.VerifiedRequests;
import com.example.bloodbank.R;
import com.example.bloodbank.Users.UserData;
import com.example.bloodbank.ViewHolders.LoadingViewHolder;
import com.example.bloodbank.ViewHolders.VerifiedRequestsHolder;

import java.util.ArrayList;

public class VerifiedRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<UserData> list;
    private final int VIEW_TYPE_ITEM = 0,VIEW_TYPE_LOADING = 1;
    private ILoadMore iLoadMore;
    boolean isLoading;
    int visibleThreshold = 5;
    int lastVisibleItem,totalItemCount;

    public VerifiedRequestsAdapter(RecyclerView rv, final Activity activity, ArrayList<UserData> list) {
        this.activity = activity;
        this.list = list;
//
//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)rv.getLayoutManager();
//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                totalItemCount = linearLayoutManager.getItemCount();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//
//                if(!isLoading || totalItemCount<=(lastVisibleItem+visibleThreshold)){
//
//                    if(iLoadMore != null)
//                        iLoadMore.onLoadMore();
//
//                }
//                isLoading = true;
//
//            }
//        });
    }

    @Override
    public int getItemViewType(int position){
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM)
            return new VerifiedRequestsHolder(LayoutInflater.from(activity).inflate(R.layout.received_requests_holder,parent,false));
        else
            return new LoadingViewHolder(LayoutInflater.from(activity).inflate(R.layout.loading,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof VerifiedRequestsHolder){
            VerifiedRequestsHolder requestsHolder = (VerifiedRequestsHolder)holder;
            requestsHolder.rrEmail.setText(list.get(position).getEmail_id());
            requestsHolder.rrText.setText(list.get(position).getUser_name());
            if(list.get(position).timestamp !=null)
            requestsHolder.rrDt.setText(list.get(position).timestamp.toString());
        } else if (holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingHolder = (LoadingViewHolder)holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setLoaded(){
        isLoading = false;
    }
    public void setLoadMore(ILoadMore iLoadMore){
        this.iLoadMore = iLoadMore;
    }

}
