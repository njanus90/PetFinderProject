package com.example.petfinderproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

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

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public class AddPetFragment extends Fragment {

    private static final String ARG_USER = "addPost";
    private static int RESULT_LOAD_IMAGE = 1;

    private User user;
    private String lost;


    Switch statusSwitch;
    Button browseButton, addMapButton, addSubmitButton;
    EditText addPetName, addDetails;
    ImageView imageView3;
    Uri selectedImage;
    String picturePath;

    public AddPetFragment() {
        // Required empty public constructor
    }

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
        lost = "Lost";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);

        //finds the needed view items
        statusSwitch = view.findViewById(R.id.statusSwitch);
        browseButton = view.findViewById(R.id.browseButton);
        addMapButton = view.findViewById(R.id.addMapButton);
        addSubmitButton = view.findViewById(R.id.submitButtonPet);
        addPetName = view.findViewById(R.id.addPetName);
        addDetails = view.findViewById(R.id.addDetails);
        imageView3 = view.findViewById(R.id.imageView3);

        //this allows the user to select a picture from their device
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    // the commented lines are things we need to finish here
                    HashMap<String, Object> fourm = new HashMap<>();
                    fourm.put("PetName", addPetName.getText().toString());
                    fourm.put("lost", lost.toString());
                    //these are things we will need later
                    //fourm.put("lat",)
                    //fourm.put("lng",)
                    //fourm.put("location", )
                    fourm.put("image", selectedImage);
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
            selectedImage = data.getData();

            //picturePath = getPath( getActivity( ).getApplicationContext( ), selectedImage );
            //Log.d("SWAG", "OnResult" + picturePath);
            Picasso.get().load(selectedImage).into(imageView3);
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
}