package com.example.bargainbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment {

    private Spinner category_spinner;
    private Uri imageUri;
    private Button upload_button, upload_image_button;
    private EditText title_editText, store_editText, promoCode_editText,
            description_editText, previousPrice_editText, newPrice_editText,
            expirationDate_editText, website_editText;
    private FirebaseDatabase db;
    private DatabaseReference db_reference;
    private FirebaseStorage storage;
    private StorageReference storage_reference;
    private ActivityResultLauncher<String> imagePickerLauncher;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
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
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        db = FirebaseDatabase.getInstance("https://bargainbuddy-47407-default-rtdb.europe-west1.firebasedatabase.app/");
        storage = FirebaseStorage.getInstance("gs://bargainbuddy-47407.appspot.com");
        storage_reference = storage.getInstance().getReference("images");
        category_spinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle item selection here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection here
            }
        });

        title_editText = view.findViewById(R.id.title_editText);
        store_editText = view.findViewById(R.id.store_editText);
        promoCode_editText = view.findViewById(R.id.promoCode_editText);
        description_editText = view.findViewById(R.id.description_editText);
        previousPrice_editText = view.findViewById(R.id.previousPrice_editText);
        newPrice_editText = view.findViewById(R.id.newPrice_editText);
        expirationDate_editText = view.findViewById(R.id.expirationDate_editText);
        website_editText = view.findViewById(R.id.editTextTextWebsite2);
        upload_button = view.findViewById(R.id.upload_button);
        upload_image_button = view.findViewById(R.id.upload_image_button);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                imageUri = result;
                Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show();
            }
        });

        upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the ActivityResultLauncher to pick an image
                imagePickerLauncher.launch("image/*");
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = title_editText.getText().toString();
                String store = store_editText.getText().toString();
                String promoCode = promoCode_editText.getText().toString();
                String description = description_editText.getText().toString();
                String category = category_spinner.getSelectedItem().toString();
                String previousPriceText = previousPrice_editText.getText().toString();
                String website = website_editText.getText().toString();
                boolean isCertified;
                Float previousPrice;
                if (TextUtils.isEmpty(previousPriceText)) {
                    previousPrice = (float) -1;
                } else {
                    previousPrice = Float.valueOf(previousPriceText);
                }
                String newPriceText = newPrice_editText.getText().toString();
                Float newPrice;
                if (TextUtils.isEmpty(previousPriceText)) {
                    newPrice = (float) -1;
                } else {
                    newPrice = Float.valueOf(newPriceText);
                }
                String expirationDate =  expirationDate_editText.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(requireContext(), "Enter title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(store)) {
                    Toast.makeText(requireContext(), "Enter store", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(requireContext(), "Enter description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (category.equals("Choose category")) {
                    Toast.makeText(requireContext(), "Choose category", Toast.LENGTH_SHORT).show();
                    return;
                }

                db_reference = db.getReference("promotions");
                DatabaseReference db_reference_push = db_reference.push();
                if (imageUri != null) {
                    String imageName = UUID.randomUUID().toString();
                    StorageReference imageRef = storage_reference.child(imageName);
                    String id = db_reference_push.getKey();
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    Promotion promotion = new Promotion(
                                            id,
                                            title,
                                            store,
                                            promoCode,
                                            description,
                                            category,
                                            previousPrice,
                                            newPrice,
                                            expirationDate,
                                            uri.toString(),
                                            false,
                                            website
                                    );

                                    db_reference_push.setValue(promotion);
                                    Toast.makeText(requireContext(), "Promotion uploaded successfully", Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show());
                } else {
                    String id = db_reference_push.getKey();
                    Promotion promotion = new Promotion(
                            id,
                            title,
                            store,
                            promoCode,
                            description,
                            category,
                            previousPrice,
                            newPrice,
                            expirationDate,
                            "",
                            false,
                            website
                    );
                    db_reference_push.setValue(promotion);
                    Toast.makeText(requireContext(), "Promotion uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

//    public boolean isCertified() {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        String uid = currentUser.getUid();
//        db_reference = db.getReference("users_info").child(uid);
//        db_reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    boolean isCertified = false;
//                    isCertified = dataSnapshot.child("isCertified").getValue(Boolean.class);
//                    return isCertified;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error
//            }
//        });
//        return isCertified;
//    }
}