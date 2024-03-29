package com.example.bargainbuddy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
 * Use the {@link SearchForItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchForItemFragment extends Fragment implements InterfaceForRecyclerView{

    private RecyclerView recyclerView;
    private ArrayList<Promotion> promotionArrayList;
    private AdapterForRecyclerView myAdapter;
    private FirebaseDatabase db;
    private DatabaseReference db_reference;
    private String title = "";
    private String category = "";
    private String uid = "";
    private Button goBackButton;
    private ArrayList<String >promotionInFavourite;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchForItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchForItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchForItemFragment newInstance(String param1, String param2) {
        SearchForItemFragment fragment = new SearchForItemFragment();
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

        View view = inflater.inflate(R.layout.fragment_search_for_item, container, false);

        goBackButton = view.findViewById(R.id.go_back_button2);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Bundle bundle = getArguments();
        title = bundle.getString("title");
        category = bundle.getString("category");
        uid = bundle.getString("uid");

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseDatabase.getInstance("https://bargainbuddy-47407-default-rtdb.europe-west1.firebasedatabase.app/");
        promotionArrayList = new ArrayList<Promotion>();
        promotionInFavourite = new ArrayList<String>();
        favouriteList();
        myAdapter = new AdapterForRecyclerView(requireContext(), promotionArrayList, this, promotionInFavourite);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

        // Inflate the layout for this fragment
        return view;
    }

    private void EventChangeListener() {
        db_reference = db.getReference("promotions");
        Query query;
        if (!title.equals("")) {
            query = db_reference.orderByChild("title").equalTo(title);
        } else if (!category.equals("")) {
            query = db_reference.orderByChild("category").equalTo(category);
        } else {
            query = db_reference.orderByChild("userId").equalTo(uid);
        }

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                promotionArrayList.add(snapshot.getValue(Promotion.class));
                myAdapter.notifyDataSetChanged();
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
        transaction.replace(R.id.fragment_search_for_item, fragment);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}