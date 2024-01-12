package com.example.bargainbuddy;

import android.content.Context;
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
    private final InterfaceForRecyclerView interfaceForRecyclerView;

    public AdapterForRecyclerView(Context context, ArrayList<Promotion> promotionArrayList, InterfaceForRecyclerView interfaceForRecyclerView) {
        this.context = context;
        this.promotionArrayList = promotionArrayList;
        this.interfaceForRecyclerView = interfaceForRecyclerView;
    }

    @NonNull
    @Override
    public AdapterForRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_promo, parent, false);

        return new MyViewHolder(v, interfaceForRecyclerView);
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

        public MyViewHolder(@NonNull View itemView, InterfaceForRecyclerView interfaceForRecyclerView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageItem);
            title = itemView.findViewById(R.id.title);
            store = itemView.findViewById(R.id.store);
            previousPrice = itemView.findViewById(R.id.previousPrice);
            newPrice = itemView.findViewById(R.id.newPrice);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (interfaceForRecyclerView != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            interfaceForRecyclerView.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
