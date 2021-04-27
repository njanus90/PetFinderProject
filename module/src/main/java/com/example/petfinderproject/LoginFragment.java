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

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;

    Button loginButton, forgotPassword;
    EditText loginEmail, loginPassword;


    public LoginFragment() {
        // Required empty public constructor
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

        loginButton = view.findViewById(R.id.loginButton);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                if(loginEmail.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if(loginPassword.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Made it", Toast.LENGTH_SHORT);
                    mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(getContext(), "Made it", Toast.LENGTH_SHORT);
                                    if (task.isSuccessful()) {
                                        //Logged in successfully
                                        Log.d("demo", "onComplete: Success");
                                        //Takes user to main fragment when successfully logged in
                                        //mListener.fromLoginToHome(mAuth.getCurrentUser().getDisplayName());
                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentLayout, HomeFragment.newInstance(mAuth.getUid()), "home-screen")
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

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement forgot password fragments

                //mListener.fromLoginToChangePassword();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, new ChangePasswordFragment())
                        .commit();

            }
        });

        return view;
    }

    //TODO: Make sure all of these are changed above and then remove the excess code from MainActivity
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.login) {
            mListener = (LoginFragment.login)context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    LoginFragment.login mListener;
    public interface login {
        public void fromLoginToHome(String user);
        public void fromLoginToChangePassword();
    }
}