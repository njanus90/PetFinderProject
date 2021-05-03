package com.example.petfinderproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;

/*
* Fragment to add a post to firestore. It associates the post with
* the current user that is logged on and allows the user some options.
* the user can determine if their pet is lost or found, post a picure of their pet, provide
* the pet name, provide last known location of the pet, and finally add any additional details about
* the post.
 */

public class AddPetFragment extends Fragment {

    private static final String ARG_USER = "addPost";
    private static int RESULT_LOAD_IMAGE = 1;

    private User user;
    private String lost;

    private FirebaseAuth mAuth;
    Switch statusSwitch;
    Button browseButton, addMapButton, addSubmitButton;
    EditText addPetName, addDetails;
    ImageView imageView3;

    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;

    public AddPetFragment() {
        // Required empty public constructor
    }
    //helps make a new AddPetFragment
    public static AddPetFragment newInstance(User user) {
        AddPetFragment fragment = new AddPetFragment();
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
        //this is here incase the user doens't touch the switch and
        // because that would mean their pet is lost
        lost = "Lost";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);
        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle(mAuth.getCurrentUser().getDisplayName());

        //finds the needed view items
        statusSwitch = view.findViewById(R.id.statusSwitch);
        browseButton = view.findViewById(R.id.browseButton);
        addSubmitButton = view.findViewById(R.id.submitButtonPet);
        addPetName = view.findViewById(R.id.addPetName);
        addDetails = view.findViewById(R.id.addDetails);
        imageView3 = view.findViewById(R.id.imageView3);

        //this allows the user to select a picture from their device
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                //this is to allow us to look at the files in our gallary
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }

        });


        //this determines the status of the switch
        //if the switch is on the pet is Found
        // if not the pet is lost
        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lost = "Found";
                } else {
                    lost = "Lost";
                }
            }
        });
        addSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks if the user has an image selected
                if (imageView3.getDrawable() == null) {
                    Toast.makeText(getContext(), "Please Choose a Picture", Toast.LENGTH_LONG).show();
                } else {
                    //this puts the posts into firestore.
                    HashMap<String, Object> fourm = new HashMap<>();
                    fourm.put("PetName", addPetName.getText().toString());
                    fourm.put("lost", lost);
                    //these are things we will need later
                    //fourm.put("lat",)
                    //fourm.put("lng",)
                    //fourm.put("location", )
                    fourm.put("image", ref.toString());
                    fourm.put("user", user);
                    fourm.put("details", addDetails.getText().toString());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(user.id).collection("posts").add(fourm);

                    //moves to the home fragment
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentLayout, HomeFragment.newInstance(user), "MyPosts")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;

    }

    //this handles the image. it basically jsut gets the image uri from when we get back from
    // looking at images to upload
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Context context = getContext();
            filePath = data.getData();

            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                context.getContentResolver(),
                                filePath);
                imageView3.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            // not really nessesary but it looks nice
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //defining the storage reference so we can know where we put the image in the storage
            ref = storageReference.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                // again not nessesary but it looks nice and didn't take long to implement
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }
}
//    public static String getPath( Context context, Uri uri ) {
//        String result = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
//                result = cursor.getString(column_index);
//            }
//            cursor.close();
//        }
//        if (result == null) {
//            result = "Not found";
//        }
//        return result;
//    }
