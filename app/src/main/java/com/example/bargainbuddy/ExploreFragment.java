package com.example.bargainbuddy;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    private SearchView searchView;
    private CardView FreshFruitsAndVegetableCardView;
    private CardView CookingOilAndGheeCardView;
    private CardView MeatAndFishCardView;
    private CardView BakeryAndSnacksCardView;
    private CardView DairyAndEggsCardView;
    private CardView PastaAndGrainsCardView;
    private CardView BeveragesCardView;
    private CardView OrganicCardView;
    private CardView BasketsCardView;
    private CardView OthersCardView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                goToSearchItems(query, "");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        FreshFruitsAndVegetableCardView = view.findViewById(R.id.FreshFruitsAndVegetableCardView);
        CookingOilAndGheeCardView = view.findViewById(R.id.CookingOilAndGheeCardView);
        MeatAndFishCardView = view.findViewById(R.id.MeatAndFishCardView);
        BakeryAndSnacksCardView = view.findViewById(R.id.BakeryAndSnacksCardView);
        DairyAndEggsCardView = view.findViewById(R.id.DairyAndEggsCardView);
        PastaAndGrainsCardView = view.findViewById(R.id.PastaAndGrainsCardView);
        BeveragesCardView = view.findViewById(R.id.BeveragesCardView);
        OrganicCardView = view.findViewById(R.id.OrganicCardView);
        BasketsCardView = view.findViewById(R.id.BasketsCardView);
        OthersCardView = view.findViewById(R.id.OthersCardView);

        FreshFruitsAndVegetableCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Fresh Fruits and Vegetable");
            }
        });
        CookingOilAndGheeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Cooking Oil and Ghee");
            }
        });
        MeatAndFishCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Meat and Fish");
            }
        });
        BakeryAndSnacksCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Bakery and Snacks");
            }
        });
        DairyAndEggsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Dairy and Eggs");
            }
        });
        PastaAndGrainsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Pasta and Grains");
            }
        });
        BeveragesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Beverages");
            }
        });
        OrganicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Organic");
            }
        });
        BasketsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Baskets");
            }
        });
        OthersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchItems("","Others");
            }
        });


        return view;
    }

    public void goToSearchItems(String title, String category) {
        Fragment fragment = new SearchForItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("category", category);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_explore, fragment);
        transaction.addToBackStack(null);  // Add the transaction to the back stack
        transaction.commit();
    }
}