package com.example.petfinderproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MyProfileFragment extends Fragment {

    //variables we need
    private static final String ARG_USER = "user";
    private User user;
    TextView textViewName;
    TextView textViewEamail;
    Button buttonPosts;
    private FirebaseAuth mAuth;



    Button editAccountButton;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    //creates the fragment
    public static MyProfileFragment newInstance(User user) {
        MyProfileFragment fragment = new MyProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        //fills the variables
        mAuth = FirebaseAuth.getInstance();
        editAccountButton = view.findViewById(R.id.editAccountButton);
        textViewName = view.findViewById(R.id.textViewName);
        textViewEamail = view.findViewById(R.id.textViewEamail);
        buttonPosts = view.findViewById(R.id.buttonPosts);

        if(user.email.equals(mAuth.getCurrentUser().getEmail())){
            textViewName.setText(mAuth.getCurrentUser().getDisplayName());
        } else {
            textViewName.setText(user.name);
        }
        textViewEamail.setText(user.email);

        //makes the button invisable because we didn't implement it yet
        editAccountButton.setVisibility(view.INVISIBLE);



//        if(user.id.equals(mAuth.getCurrentUser().getUid()) && user.name.equals(mAuth.getCurrentUser().getDisplayName())){
//            //editAccountButton.setVisibility(view.VISIBLE);
//            //if the editAccount button is clicked moves to the Edit account fragment
//            textViewName.setText(mAuth.getCurrentUser().getDisplayName());
////            editAccountButton.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    //TODO: Implement edit account fragment
////                }
////            });
////        } else{
////            //editAccountButton.setVisibility(view.INVISIBLE);
//        }
        buttonPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, MyPostsFragment.newInstance(user.id), "MyPosts")
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }
}