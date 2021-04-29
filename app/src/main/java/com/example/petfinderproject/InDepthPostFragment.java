package com.example.petfinderproject;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    TextView textView30;
    TextView textView39;
    ImageView imageView4;
    // instance for firebase storage and StorageReference
    StorageReference ref;
    FirebaseStorage storage;

    private PetPost mPost;

    public InDepthPostFragment() { }

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
        textView30 = view.findViewById(R.id.textView30);
        textView39 = view.findViewById(R.id.textView39);
        imageView4 = view.findViewById(R.id.imageView4);

        //sets all the text views
        textView39.setText(mPost.user.name);
        postPetName.setText(mPost.name);
        postPetDetails.setText(mPost.details);
        textView30.setText(mPost.status);

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
        return view;
    }
}