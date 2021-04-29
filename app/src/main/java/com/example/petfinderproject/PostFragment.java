package com.example.petfinderproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
  * this fragment is here to hold a recyclerview of all the curren't user's posts
  * so its like a place holder and doesn't do anything because it doesn't need to
 */

public class PostFragment extends Fragment {

    private static final String ARG_USER = "user";

    private String user;



    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(String user) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getString(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }
}