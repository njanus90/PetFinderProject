package com.example.petfinderproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddPetFragment extends Fragment {

    private static final String ARG_USER = "addPost";

    private User user;
    private String lost;

    Switch statusSwitch;
    Button browseButton, addMapButton, addSubmitButton;
    EditText addPetName, addDetails;

    public AddPetFragment() {
        // Required empty public constructor
    }

    public static AddPetFragment newInstance(User user) {
        AddPetFragment fragment = new AddPetFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
        }
        lost = "Lost";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);

        statusSwitch = view.findViewById(R.id.statusSwitch);
        browseButton = view.findViewById(R.id.browseButton);
        addMapButton = view.findViewById(R.id.addMapButton);
        addSubmitButton = view.findViewById(R.id.submitButtonPet);
        addPetName = view.findViewById(R.id.addPetName);
        addDetails = view.findViewById(R.id.addDetails);

        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lost = "Found";
                } else {
                    lost = "Lost";
                }
            }
        });

        addSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this puts the posts into firestore.
                // the commented lines are things we need to finish here
                HashMap<String, Object> fourm = new HashMap<>();
                fourm.put("PetName", addPetName.getText().toString());
                fourm.put("lost",lost.toString());
                //fourm.put("lat",)
                //fourm.put("lng",)
                //fourm.put("location", )
                //fourm.put("image",)
                fourm.put("user",user);
                //fourm.put("UserId", user.id);
                //fourm.put("UserName",user.name);
                fourm.put("details", addDetails.getText().toString());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(user.id).collection("posts").add(fourm);

                //TODO: Make a PetPost object and add this info there then add it to the user's list of posts (arraylist of PetPost objects)
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, HomeFragment.newInstance(user), "MyPosts")
                        .addToBackStack(null)
                        .commit();
                // mListener.fromAddToHome(user);
            }
        });


        return view;
    }
}