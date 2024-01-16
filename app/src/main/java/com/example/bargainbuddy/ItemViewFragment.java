package com.example.bargainbuddy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemViewFragment newInstance(String param1, String param2) {
        ItemViewFragment fragment = new ItemViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_view, container, false);
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        Promotion promotion = bundle.getParcelable("promotion");
        ArrayList<String> promotionInFavourite = bundle.getStringArrayList("favourite");

        ImageView imageView = view.findViewById(R.id.imageView3);
        ImageView checkImageView = view.findViewById(R.id.checkImageView2);
        TextView title_textView = view.findViewById(R.id.textViewTitle);
        TextView store_textView = view.findViewById(R.id.textViewStore);
        TextView previousPrice_textView = view.findViewById(R.id.textViewPreviousPrice);
        TextView newPrice_textView = view.findViewById(R.id.textViewNewPrice);
        TextView description_textView = view.findViewById(R.id.textViewDescription);
        TextView promoCode_textView = view.findViewById(R.id.textViewPromoCode);
        TextView expirationDate_textView = view.findViewById(R.id.textViewExpirationDate);
        Button addToFavButton = view.findViewById(R.id.add_to_fav_button);

        if (promotionInFavourite.contains(promotion.getId())) {
            addToFavButton.setEnabled(false);
            addToFavButton.setBackgroundResource(R.drawable.round_grey_button);
        }

        if (promotion.getCertified() == 0) {
            checkImageView.setVisibility(View.INVISIBLE);
        } else {
            checkImageView.setVisibility(View.VISIBLE);
        }

        if (!promotion.getImageURI().equals(""))
        {
            Picasso.get().load(promotion.getImageURI()).into(imageView);
        }
        title_textView.setText(promotion.getTitle());
        store_textView.setText(promotion.getStore());
        previousPrice_textView.setText(String.valueOf(promotion.getPreviousPrice()));
        newPrice_textView.setText(String.valueOf(promotion.getNewPrice()));
        description_textView.setText(promotion.getDescription());
        promoCode_textView.setText(promotion.getPromoCode());
        expirationDate_textView.setText(promotion.getExpirationDate());

        Button goBackButton = view.findViewById(R.id.go_back_button);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the ForYouFragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String uid = currentUser.getUid();
                FirebaseDatabase db = FirebaseDatabase.getInstance("https://bargainbuddy-47407-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference db_reference = db.getReference("favourite");

                Query query = db_reference.orderByChild("uid").equalTo(uid);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                // Iterate through each child and update the desired node
                                String key = childSnapshot.getKey();
                                if (key != null) {
                                    db_reference.child(key).child("promotionsID").push().setValue(promotion.getId());
                                }
                            }
                        }
                        else {
                            List<String> promotionsIDList = new ArrayList<>();
                            promotionsIDList.add(promotion.getId());
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
        return view;
    }
}