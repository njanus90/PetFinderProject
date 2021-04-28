package com.example.petfinderproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordFragment extends Fragment {

    EditText confirmEmail, newChangePassword, confirmChangePassword;
    Button submitButtonChange;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        confirmEmail = view.findViewById(R.id.confirmEmail);
        newChangePassword = view.findViewById(R.id.newChangePassword);
        confirmChangePassword = view.findViewById(R.id.confirmChangePassword);
        submitButtonChange = view.findViewById(R.id.submitButtonChange);

        submitButtonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement a password change
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                        .addToBackStack(null)
                        .commit();
            }
        });


       return view;
    }
}