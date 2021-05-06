package com.example.petfinderproject;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/*
 * This fragment handles the Edit Account button
 * It allows the user to change their name, password, and email.
 * also updates the posts to have the newly updated user.
 * It also gets the user's current location in the form of lat and lng
 * If you are using an emulator the default location is google hq
 */
public class EditAccountFragment extends Fragment {

    //variables we need
    private FirebaseAuth mAuth;
    EditText createName;
    EditText createEmail;
    EditText createPassword;
    EditText confirmPassword;
    Button submitButtonCreate;
    Boolean nexFrag;



    public EditAccountFragment() {
        // Required empty public constructor
    }


    public static EditAccountFragment newInstance() {
        EditAccountFragment fragment = new EditAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);
        //fills the variables
        createName = view.findViewById(R.id.createName);
        createEmail = view.findViewById(R.id.createEmail);
        createPassword = view.findViewById(R.id.createPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        submitButtonCreate = view.findViewById(R.id.submitButtonCreate);
        nexFrag = true;

        mAuth = FirebaseAuth.getInstance();
        submitButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if createPassword field is not empty and comfirmPassword is
                if(!createPassword.getText().toString().isEmpty() && confirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please confirm password", Toast.LENGTH_SHORT).show();
                }
                //else if createPassword Field is empty and confirmPassrod isn't
                else if(createPassword.getText().toString().isEmpty() && !confirmPassword.getText().toString().isEmpty() ){
                    Toast.makeText(getContext(), "Please fill out new password", Toast.LENGTH_SHORT).show();
                }
                //updates the user's password if the fields are full and if there are no errors
                //in changing the password it changes the name and email if those fields are filled
                else if (!createPassword.getText().toString().isEmpty() && !confirmPassword.getText().toString().isEmpty()) {
                    if (confirmPassword.getText().toString().equals(createPassword.getText().toString())) {
                        NameEmail();
                        mAuth.getInstance().getCurrentUser().updatePassword(createPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                                // this is to make sure only one fragment is created if we are changing the
                                // more than one account field
                                if(nexFrag == true){
                                    nexFrag = false;
                                    //goes back to the login fragment by just creating a new user the fields filled with mAuth if atleast one thing was changed
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(new User(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail())), "Home-screen")
                                            .addToBackStack(null)
                                            .commit();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }
                //if neither password fields are filled it just does does the name and email
                else{
                    NameEmail();
                }

            }
        });
        return view;
    }
    // this is just a function we can call to handle the changing of the name and email
    public void NameEmail(){

        //updates the user's name if there is something in the field
        if (!createName.getText().toString().isEmpty()) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(createName.getText().toString())
                    .build();
            mAuth.getCurrentUser().updateProfile(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //sets the title of the activity after the display name is changed
                    getActivity().setTitle(createName.getText().toString());
                    Toast.makeText(getContext(), "Name Changed", Toast.LENGTH_SHORT).show();
                    updatePosts();
                    // this is to make sure only one fragment is created if we are changing the
                    // more than one account field
                    if(nexFrag == true){
                        nexFrag = false;
                        //goes back to the profile fragment
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(new User(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail())), "Home-screen")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }
        //updstes the user's email if the field is full
        if (!createEmail.getText().toString().isEmpty()) {
            mAuth.getCurrentUser().updateEmail(createEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Email Changed", Toast.LENGTH_SHORT).show();
                    updatePosts();
                    // this is to make sure only one fragment is created if we are changing the
                    // more than one account field
                    if(nexFrag == true){
                        nexFrag = false;
                        //goes back to the profile fragment
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(new User(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail())), "Home-screen")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }
    }
    //updates all the users posts to have the new user fields if the user updates their account
    public void updatePosts(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postOrder = db.collection("users")
                .document(mAuth.getCurrentUser().getUid()).collection("posts");
        postOrder.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    for (QueryDocumentSnapshot doc : value) {
                        db.collection("users").document(mAuth.getCurrentUser().getUid())
                                .collection("posts").document(doc.getId())
                                .update("user",new User(mAuth.getCurrentUser().getDisplayName()
                                        ,mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail()));
                       }
                }
            }
        });
    }
}