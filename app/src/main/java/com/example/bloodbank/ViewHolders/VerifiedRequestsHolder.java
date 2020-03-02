package com.example.bloodbank.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;

public class VerifiedRequestsHolder extends RecyclerView.ViewHolder {
    public TextView rrEmail,rrText,rrDt;
    public ImageView profileImg;
    public CardView cardView;

    public VerifiedRequestsHolder(@NonNull View itemView) {
        super(itemView);
        profileImg = itemView.findViewById(R.id.rrIcon);
        rrEmail = itemView.findViewById(R.id.rrEmail);
        rrText = itemView.findViewById(R.id.rrText);
        rrDt = itemView.findViewById(R.id.rrDt);
        cardView = itemView.findViewById(R.id.cardView1);
    }
}
