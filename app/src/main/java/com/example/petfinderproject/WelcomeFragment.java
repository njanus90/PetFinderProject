package com.example.petfinderproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
/*
* this is the welcome fragment. it is the first thing a user sees when they open the app
* has a nice picture of a dog, the title of our app, and a create users button and a login button.
 */
public class WelcomeFragment extends Fragment {

    FirebaseAuth mAuth;
    //Declares the buttons to be used
    Button homeLoginButton, homeCreateAccountButton;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        //Assigns the login button
        homeLoginButton = view.findViewById(R.id.homeLoginButton);
        homeCreateAccountButton = view.findViewById(R.id.homeCreateAccountButton);

        mAuth = FirebaseAuth.getInstance();

        //Handles the click of the button
        homeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                        .addToBackStack(null)
                        .commit();
            }
        });

        homeCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, CreateAccountFragment.newInstance(), "create-screen")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}