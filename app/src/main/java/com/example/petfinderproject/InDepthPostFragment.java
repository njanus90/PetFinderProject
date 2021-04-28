package com.example.petfinderproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InDepthPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InDepthPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "post";
    private static final String TAG = "SWAG";

    //all variables
    TextView postPetName;
    TextView postPetDetails;
    TextView textView30;
    TextView textView39;
    String name;
    // TODO: Rename and change types of parameters
    private PetPost mPost;

    public InDepthPostFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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

//        //Log.d(TAG, mPost.user);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("users").document(mPost.user.id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                name = task.getResult().get("name").toString();
//                //Log.d(TAG, name);
//            }
//        });
        Log.d(TAG, mPost.user.name);
        //sets all the text views
        //textView39.setText(mPost.user);
        postPetName.setText(mPost.name);
        postPetDetails.setText(mPost.details);
        //redundant code once we change all the posts in firebase
        if(mPost.status.equals("true")) {
            textView30.setText("FOUND");
        } else {
            textView30.setText("LOST");
        }
        return view;
    }
}