package com.example.thomas.sapiadvert;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by Szabi on 2017. 12. 24..
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Advertisment> advertismentList;
    private Context context;
    private StorageReference firebaseStorage= FirebaseStorage.getInstance().getReference();
    public MyAdapter(List<Advertisment> advertismentList, Context context) {
        this.advertismentList = advertismentList;
        this.context = context;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.addvertisment_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Advertisment advertisment = advertismentList.get(position);
        holder.titleText.setText(advertisment.getTitle());
        holder.detailText.setText(advertisment.getDetails());
        Glide.with(context).load(advertisment.getProfilePictureUri()).into(holder.profilePictureView);
        Glide.with(context).load(advertisment.getMainPictureUri()).into(holder.mainImageView);
       // DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().
               // child("Users")
                //.child(advertisment.getCreatedBy());

       // StorageReference filepath=firebaseStorage.child(databaseReference);
       // Glide.with(context).load(new StorageReference());
        //Glide.with(context).load).into(profilePictureInput);
      //  holder.profilePictureView

    }

    @Override
    public int getItemCount() {
        return advertismentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;
        public TextView detailText;
        public ImageView profilePictureView;
        public ImageView mainImageView;
        public ViewHolder(View itemView) {
            super(itemView);

            titleText=itemView.findViewById(R.id.titleText);
            detailText=itemView.findViewById(R.id.detailsText);
            profilePictureView=itemView.findViewById(R.id.profilePictureView);
            mainImageView=itemView.findViewById(R.id.mainImageView);
        }
    }
}
