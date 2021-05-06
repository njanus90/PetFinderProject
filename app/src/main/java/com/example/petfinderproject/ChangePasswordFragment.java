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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

/*
 * this fragment handles the changing of the password.
 * the user inputs their email and an email is sent to them prompting them to change their password
 * once the submit buttion is clicked
 */

public class ChangePasswordFragment extends Fragment {

    EditText confirmEmail;
    Button submitButtonChange;
    private FirebaseAuth mAuth;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    //makes the fragment
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

       //finds the elements we need
        confirmEmail = view.findViewById(R.id.confirmEmail);
        submitButtonChange = view.findViewById(R.id.submitButtonChange);
        mAuth = FirebaseAuth.getInstance();

        //when the password change button is clicked
        submitButtonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sends an email to the user prompting them to change their password
                mAuth.sendPasswordResetEmail(confirmEmail.getText().toString());
                //goes back to the login fragment
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                        .addToBackStack(null)
                        .commit();
            }
        });
       return view;
    }
}