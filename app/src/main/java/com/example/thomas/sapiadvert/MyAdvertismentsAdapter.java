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
 * Adapter for the recycler view that holds the advertisment created by the curently logged in user
 */
public class MyAdvertismentsAdapter  extends RecyclerView.Adapter<MyAdvertismentsAdapter.ViewHolder>{

    private List<AdvertismentMy> advertismentList;
    private List<String> advertismentKeyList;
    private Context context;
    private StorageReference firebaseStorage= FirebaseStorage.getInstance().getReference();

    MyAdvertismentsAdapter(List<AdvertismentMy> advertismentList, List<String> advertismentKeyList, Context context) {
        this.advertismentList = advertismentList;
        this.advertismentKeyList = advertismentKeyList;
        this.context = context;
    }

    @Override
    public MyAdvertismentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.myadvertisment_item,parent,false);
        return new MyAdvertismentsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdvertismentsAdapter.ViewHolder holder, int position) {
        AdvertismentMy advertisment = advertismentList.get(position);
        holder.titleText.setText(advertisment.getTitle());
        holder.detailText.setText(advertisment.getDetails());
        Glide.with(context).load(advertisment.getMainPictureUri()).into(holder.mainImageView);
        holder.bind(advertisment,advertismentKeyList.get(position));

    }

    @Override
    public int getItemCount() {
        return advertismentList.size();
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
         * attaching onclicklistener on the advertisments
         * @param ad
         * @param key
         */
        void bind(final AdvertismentMy ad, final String key){

            View.OnClickListener listener= new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(context,AdvertismentModifyActivity.class);
                    myIntent.putExtra("Advertisment",ad);
                    myIntent.putExtra("AdvertismentKey",key);
                    context.startActivity(myIntent);
                }
            };
            titleText.setOnClickListener(listener);
            detailText.setOnClickListener(listener);
            mainImageView.setOnClickListener(listener);
        }
    }
}
