package com.example.petfinderproject;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InDepthPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InDepthPostFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "post";
    private static final String TAG = "SWAG";

    //all variables
    TextView postPetName;
    TextView postPetDetails;
    TextView textViewLostFound;
    ImageView imageView4;
    Button buttonUser;
    Button postMapButton;
    StorageReference ref;
    FirebaseStorage storage;
    private PetPost mPost;

    public InDepthPostFragment() { //required empty constructor
        }

    //the following two methods make it so the fragment can take in a post paramater
    public static InDepthPostFragment newInstance(PetPost post) {
        InDepthPostFragment fragment = new InDepthPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPost = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_in_depth_post, container, false);

        //finds all the required components
        postPetName = view.findViewById(R.id.postPetName);
        postPetDetails = view.findViewById(R.id.postPetDetails);
        textViewLostFound = view.findViewById(R.id.textViewLostFound);
        buttonUser = view.findViewById(R.id.buttonUser);
        imageView4 = view.findViewById(R.id.imageView4);
        postMapButton = view.findViewById(R.id.postMapButton);

        //sets all the text views
        buttonUser.setText(mPost.user.name);
        postPetName.setText(mPost.name);
        postPetDetails.setText(mPost.details);
        textViewLostFound.setText(mPost.status);

        //gets the storage instance
        storage = FirebaseStorage.getInstance();
        // Reference to an image file in Cloud Storage using the url of the image
        ref = storage.getReferenceFromUrl(mPost.petPic);

        //downloads the URL of the image and puts it in the required imageview using picasso
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView4);
            }
        });

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(mPost.user), "Profile")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //if the map button is clicked it moves to the map fragment
        postMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, new MapsFragment(),"maps")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}