package com.example.petfinderproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/*
* Fragment to handle the creation of an account. the user inputs
* their name, email, and password and when the submit button is clicked
* firebase authentication is used to create a user account.
 */

public class CreateAccountFragment extends Fragment {
    private FirebaseAuth mAuth;

    EditText createEmail, createPassword, createName, confirmPassword;
    Button submitButtonCreate;
    AlertDialog.Builder builder;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    //creates the fragment
    public static CreateAccountFragment newInstance() {
        CreateAccountFragment fragment = new CreateAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        builder = new AlertDialog.Builder(getActivity());

        //finds the elements in the xml we need
        createName = view.findViewById(R.id.createName);
        createEmail = view.findViewById(R.id.createEmail);
        createPassword = view.findViewById(R.id.createPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        submitButtonCreate = view.findViewById(R.id.submitButtonCreate);

        submitButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                //some error checks
                if(createEmail.getText().toString().isEmpty()){
                    builder.setMessage("Email missing") .setTitle("Error").create().show();
                } else if(createPassword.getText().toString().isEmpty()){
                    builder.setMessage("Password missing") .setTitle("Error").create().show();
                } else if(confirmPassword.getText().toString().isEmpty()){
                    builder.setMessage("Confirm Password") .setTitle("Error").create().show();
                }else if(createName.getText().toString().isEmpty()){
                    builder.setMessage("Name missing") .setTitle("Error").create().show();
                } else {
                    //makes the user using firbase authentication
                    mAuth.createUserWithEmailAndPassword(createEmail.getText().toString(), createPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        createUser(createName.getText().toString(), mAuth.getUid(),createEmail.getText().toString());
                                        //this sets the user's profile name to the one they typed in
                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(createName.getText().toString())
                                                .build();
                                        mAuth.getCurrentUser().updateProfile(profile);
                                    } else {
                                        builder.setMessage(task.getException().getMessage()) .setTitle("Error").create().show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }
    //this inputs the new user into firestore
    public void createUser(String name, String id,String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, String> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        db.collection("users").document(id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    //if it works we move to the login fragment
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("demo", "onComplete: Success");
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentLayout, new LoginFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Fail
                    }
                });
    }
}