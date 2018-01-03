package com.example.thomas.sapiadvert;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 *Adapter for the recycler view in Main activity that hold the advertisements
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Advertisement> advertisementList;
    private Context context;
    private StorageReference firebaseStorage= FirebaseStorage.getInstance().getReference();
    public MyAdapter(List<Advertisement> advertisementList, Context context) {
        this.advertisementList = advertisementList;
        this.context = context;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Advertisement advertisement = advertisementList.get(position);
        holder.titleText.setText(advertisement.getTitle());
        holder.detailText.setText(advertisement.getDetails());
        Glide.with(context).load(advertisement.getProfilePictureUri()).into(holder.profilePictureView);
        Glide.with(context).load(advertisement.getMainPictureUri()).into(holder.mainImageView);
        holder.bind(advertisement);

    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
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

        /**
         * Attaching onclick listener on profile picture and on the rest of the view
         * @param ad advertisement
         */
        public void bind(final Advertisement ad){
            profilePictureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,ViewProfileActivity.class);
                    intent.putExtra("UserID",ad.getCreatedBy());
                    context.startActivity(intent);
                }
            });
            View.OnClickListener listener= new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(context,AdvertisementReadActivity.class);
                    myIntent.putExtra("Advertisement",ad);
                    context.startActivity(myIntent);
                }
            };
            titleText.setOnClickListener(listener);
            detailText.setOnClickListener(listener);
            mainImageView.setOnClickListener(listener);

        }
    }
}
