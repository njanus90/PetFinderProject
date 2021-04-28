package com.example.petfinderproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InDepthPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InDepthPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "post";

    TextView postPetName;
    TextView postPetDetails;
    TextView textView30;
    TextView textView39;
    FirebaseAuth auth;
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
        postPetName = view.findViewById(R.id.postPetName);
        postPetDetails = view.findViewById(R.id.postPetDetails);
        textView30 = view.findViewById(R.id.textView30);
        textView39 = view.findViewById(R.id.textView39);
        auth = FirebaseAuth.getInstance();

        //textView39.setText();

        postPetName.setText(mPost.name);
        postPetDetails.setText(mPost.details);
        if(mPost.status.equals("true")) {
            textView30.setText("FOUND");
        } else {
            textView30.setText("LOST");
        }
        return view;
    }
}