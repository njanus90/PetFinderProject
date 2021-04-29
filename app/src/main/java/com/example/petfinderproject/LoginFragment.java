package com.example.petfinderproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {
    //variables we need
    private FirebaseAuth mAuth;
    Button loginButton, forgotPassword;
    EditText loginEmail, loginPassword;


    public LoginFragment() {// Required empty public constructor
         }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //fills the variables
        loginButton = view.findViewById(R.id.loginButton);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);

        //when the login button is clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                //does error checking to see if all the fields are correct
                if(loginEmail.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if(loginPassword.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Made it", Toast.LENGTH_SHORT);
                    //uses firebase authentication to log in
                    mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(getContext(), "Made it", Toast.LENGTH_SHORT);
                                    if (task.isSuccessful()) {
                                        //Logged in successfully
                                        Log.d("demo", "onComplete: Success");
                                        //moves to the HomeFragment with the currently logged in user attached
                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentLayout, HomeFragment.newInstance(new User(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getUid())), "home-screen")
                                                .addToBackStack(null)
                                                .commit();
                                    } else {
                                        //Log in fail
                                        Log.d("demo", "onComplete: Fail");
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                }
            }
        });

        //if user clicks forgot password it moves to forgot password fragment
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, new ChangePasswordFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}