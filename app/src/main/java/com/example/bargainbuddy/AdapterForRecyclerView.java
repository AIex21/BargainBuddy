package com.example.bargainbuddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdapterForRecyclerView extends RecyclerView.Adapter<AdapterForRecyclerView.MyViewHolder> {

    private Context context;
    private ArrayList<Promotion> promotionArrayList;
    private ArrayList<String> promotionInFavourite;
    private final InterfaceForRecyclerView interfaceForRecyclerView;
    private String uid;

    public AdapterForRecyclerView(Context context, ArrayList<Promotion> promotionArrayList, InterfaceForRecyclerView interfaceForRecyclerView, ArrayList<String> promotionInFavourite) {
        this.context = context;
        this.promotionArrayList = promotionArrayList;
        this.interfaceForRecyclerView = interfaceForRecyclerView;
        this.promotionInFavourite = promotionInFavourite;
    }

    @NonNull
    @Override
    public AdapterForRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_promo, parent, false);

        return new MyViewHolder(v, interfaceForRecyclerView, promotionArrayList, promotionInFavourite);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForRecyclerView.MyViewHolder holder, int position) {

        Promotion promo = promotionArrayList.get(position);

        if (!promo.getImageURI().equals("")) {
            Picasso.get().load(promo.getImageURI()).into(holder.image);
        }
        holder.title.setText(promo.getTitle());
        holder.store.setText(promo.getStore());
        holder.previousPrice.setText(String.valueOf(promo.getPreviousPrice()));
        holder.newPrice.setText(String.valueOf(promo.getNewPrice()));

        if (promotionInFavourite.contains(promo.getId())) {
            holder.addToFavButton.setEnabled(false);
            holder.addToFavButton.setBackgroundResource(R.drawable.round_grey_button);
        }

        if (promo.getCertified() == 0) {
            holder.checkImageView.setVisibility(View.INVISIBLE);
        } else {
            holder.checkImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return promotionArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ArrayList<Promotion> promotionArrayList;
        ArrayList<String> promotionInFavourite;
        ImageView image, checkImageView;
        TextView title, store, previousPrice, newPrice;
        Button addToFavButton;

        public MyViewHolder(@NonNull View itemView, InterfaceForRecyclerView interfaceForRecyclerView,
                            ArrayList<Promotion> promotionArrayList, ArrayList<String> promotionInFavourite) {
            super(itemView);
            this.promotionArrayList = promotionArrayList; // Initialize the promotionArrayList field
            this.promotionInFavourite = promotionInFavourite;
            image = itemView.findViewById(R.id.imageItem);
            title = itemView.findViewById(R.id.title);
            store = itemView.findViewById(R.id.store);
            previousPrice = itemView.findViewById(R.id.previousPrice);
            newPrice = itemView.findViewById(R.id.newPrice);
            addToFavButton = itemView.findViewById(R.id.add_to_fav_button2);
            checkImageView = itemView.findViewById(R.id.checkImageView);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String uid = currentUser.getUid();

            FirebaseDatabase db = FirebaseDatabase.getInstance("https://bargainbuddy-47407-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference db_reference = db.getReference("favourite");

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

            addToFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String newPromotionID = promotionArrayList.get(getAdapterPosition()).getId();

                    Query query = db_reference.orderByChild("uid").equalTo(uid);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    // Iterate through each child and update the desired node
                                    String key = childSnapshot.getKey();
                                    if (key != null) {
                                        db_reference.child(key).child("promotionsID").push().setValue(newPromotionID);
                                    }
                                }
                            }
                            else {
                                List<String> promotionsIDList = new ArrayList<>();
                                promotionsIDList.add(newPromotionID);
                                Favourite favourite = new Favourite(uid, promotionsIDList);
                                db_reference.push().setValue(favourite);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle errors
                        }
                    });
                    addToFavButton.setEnabled(false);
                    addToFavButton.setBackgroundResource(R.drawable.round_grey_button);
                }
            });
        }
    }

}
