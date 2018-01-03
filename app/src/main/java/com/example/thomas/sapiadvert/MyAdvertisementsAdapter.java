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
 * Adapter for the recycler view that holds the advertisement created by the currently logged in user
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */
public class MyAdvertisementsAdapter extends RecyclerView.Adapter<MyAdvertisementsAdapter.ViewHolder>{

    private List<AdvertisementMy> advertisementList;
    private List<String> advertisementKeyList;
    private Context context;
    private StorageReference firebaseStorage= FirebaseStorage.getInstance().getReference();

    MyAdvertisementsAdapter(List<AdvertisementMy> advertisementList, List<String> advertisementKeyList, Context context) {
        this.advertisementList = advertisementList;
        this.advertisementKeyList = advertisementKeyList;
        this.context = context;
    }

    @Override
    public MyAdvertisementsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.myadvertisement_item,parent,false);
        return new MyAdvertisementsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdvertisementsAdapter.ViewHolder holder, int position) {
        AdvertisementMy advertisement = advertisementList.get(position);
        holder.titleText.setText(advertisement.getTitle());
        holder.detailText.setText(advertisement.getDetails());
        Glide.with(context).load(advertisement.getMainPictureUri()).into(holder.mainImageView);
        holder.bind(advertisement,advertisementKeyList.get(position));

    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView detailText;
        ImageView mainImageView;
        ViewHolder(View itemView) {
            super(itemView);

            titleText=itemView.findViewById(R.id.my_ad_titleTextView);
            detailText=itemView.findViewById(R.id.my_ad_detailsTextView);
            mainImageView=itemView.findViewById(R.id.my_ad_mainImageImageView);

        }

        /**
         * attaching onClickListener on the advertisements
         * @param ad advertisement
         * @param key advertisement key
         */
        void bind(final AdvertisementMy ad, final String key){

            View.OnClickListener listener= new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(context,AdvertisementModifyActivity.class);
                    myIntent.putExtra("Advertisement",ad);
                    myIntent.putExtra("AdvertisementKey",key);
                    context.startActivity(myIntent);
                }
            };
            titleText.setOnClickListener(listener);
            detailText.setOnClickListener(listener);
            mainImageView.setOnClickListener(listener);
        }
    }
}
