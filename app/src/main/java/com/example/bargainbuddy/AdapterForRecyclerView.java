package com.example.bargainbuddy;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class AdapterForRecyclerView extends RecyclerView.Adapter<AdapterForRecyclerView.MyViewHolder> {

    Context context;
    ArrayList<Promotion> promotionArrayList;

    public AdapterForRecyclerView(Context context, ArrayList<Promotion> promotionArrayList) {
        this.context = context;
        this.promotionArrayList = promotionArrayList;
    }

    @NonNull
    @Override
    public AdapterForRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_promo, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForRecyclerView.MyViewHolder holder, int position) {

        Promotion promo = promotionArrayList.get(position);

        if (!promo.getImageURI().equals(""))
        {
            Picasso.get().load(promo.getImageURI()).into(holder.image);
        }
        holder.title.setText(promo.getTitle());
        holder.store.setText(promo.getStore());
        holder.previousPrice.setText(String.valueOf(promo.getPreviousPrice()));
        holder.newPrice.setText(String.valueOf(promo.getNewPrice()));

    }

    @Override
    public int getItemCount() {
        return promotionArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, store, previousPrice, newPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageItem);
            title = itemView.findViewById(R.id.title);
            store = itemView.findViewById(R.id.store);
            previousPrice = itemView.findViewById(R.id.previousPrice);
            newPrice = itemView.findViewById(R.id.newPrice);
        }
    }
}
