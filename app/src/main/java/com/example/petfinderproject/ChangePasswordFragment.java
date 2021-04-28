package com.example.petfinderproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangePasswordFragment extends Fragment {

    EditText confirmEmail, newChangePassword, confirmChangePassword;
    Button submitButtonChange;
    private FirebaseAuth mAuth;


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
        submitButtonChange = view.findViewById(R.id.submitButtonChange);
        mAuth = FirebaseAuth.getInstance();

        submitButtonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //sends an email to the user prompting them to change their password
                FirebaseAuth.getInstance().sendPasswordResetEmail(confirmEmail.getText().toString());
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                        .addToBackStack(null)
                        .commit();
            }
        });


       return view;
    }
}