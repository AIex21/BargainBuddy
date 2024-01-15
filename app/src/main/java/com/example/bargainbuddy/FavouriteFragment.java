package com.example.bargainbuddy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment implements InterfaceForRecyclerView{

    private RecyclerView recyclerView;
    private ArrayList<Promotion> promotionArrayList;
    private AdapterForRecyclerView myAdapter;
    private FirebaseDatabase db;
    private DatabaseReference db_reference;
    private ArrayList<String> promotionInFavourite;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseDatabase.getInstance("https://bargainbuddy-47407-default-rtdb.europe-west1.firebasedatabase.app/");
        promotionArrayList = new ArrayList<Promotion>();
        promotionInFavourite = new ArrayList<String>();
        favouriteList();
        myAdapter = new AdapterForRecyclerView(requireContext(), promotionArrayList, FavouriteFragment.this, promotionInFavourite);
        recyclerView.setAdapter(myAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    private void EventChangeListener() {
        db_reference = db.getReference("promotions");

        db_reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Promotion promotion = snapshot.getValue(Promotion.class);
                if (promotionInFavourite.contains(promotion.getId())) {
                    promotionArrayList.add(promotion);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Fragment fragment = new ItemViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("promotion", promotionArrayList.get(position));  // Replace "myObject" with a key of your choice
        bundle.putStringArrayList("favourite", promotionInFavourite);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_favourite, fragment);
        transaction.addToBackStack(null);  // Add the transaction to the back stack
        transaction.commit();
    }

    @Override
    public void favouriteList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://bargainbuddy-47407-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference db_reference = db.getReference("favourite");

        Query queryGetList = db_reference.orderByChild("uid").equalTo(uid);
        queryGetList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        DataSnapshot promotionsNode = childSnapshot.child("promotionsID");

                        if (promotionsNode.exists()) {
                            // The promotionsID node exists for the user
                            // Get the list of promotion IDs
                            Iterable<DataSnapshot> children = promotionsNode.getChildren();

                            for (DataSnapshot child : children) {
                                String promotionID = child.getValue(String.class);
                                promotionInFavourite.add(promotionID);
                            }
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
                EventChangeListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}