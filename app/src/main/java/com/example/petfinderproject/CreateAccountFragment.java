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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateAccountFragment extends Fragment {
    private FirebaseAuth mAuth;

    EditText createEmail, createPassword, createName, confirmPassword;
    Button submitButtonCreate;
    AlertDialog.Builder builder;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

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

        createName = view.findViewById(R.id.createName);
        createEmail = view.findViewById(R.id.createEmail);
        createPassword = view.findViewById(R.id.createPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        submitButtonCreate = view.findViewById(R.id.submitButtonCreate);

        submitButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                if(createEmail.getText().toString().isEmpty()){
                    builder.setMessage("Email missing") .setTitle("Error").create().show();
                } else if(createPassword.getText().toString().isEmpty()){
                    builder.setMessage("Password missing") .setTitle("Error").create().show();
                } else if(createName.getText().toString().isEmpty()){
                    builder.setMessage("Name missing") .setTitle("Error").create().show();
                } else {
                    mAuth.createUserWithEmailAndPassword(createEmail.getText().toString(), createPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        createUser(createName.getText().toString(), mAuth.getUid());
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

    public void createUser(String name, String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, String> user = new HashMap<>();
        user.put("name", name);

        db.collection("users").document(id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("demo", "onComplete: Success");
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentLayout, new LoginFragment())
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

    //TODO:Remove after above is updated
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateAccountFragment.create) {
            mListener = (CreateAccountFragment.create)context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    CreateAccountFragment.create mListener;
    public interface create {
        public void fromCreateToLogin();
    }
}