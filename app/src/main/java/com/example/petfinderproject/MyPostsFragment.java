package com.example.petfinderproject;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment to list the posts of a user.
 * this is used to see anyones posts not just the current users posts.
 * you can click on the posts to see a more indepth view of them.
 */
public class MyPostsFragment extends Fragment {
    RecyclerView recyclerViewMYPosts;
    LinearLayoutManager layoutManager;
    RecyclerViewAdapterHome adapter;
    TextView textViewAnyPosts;
    FirebaseAuth auth;

    private static final String ARG_USER = "MyPosts";
    private String user;
    ArrayList<PetPost> posts = new ArrayList<>();

    public MyPostsFragment() {
        // Required empty public constructor
    }

    public static MyPostsFragment newInstance(String user) {
        MyPostsFragment fragment = new MyPostsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        auth = FirebaseAuth.getInstance();

        textViewAnyPosts = view.findViewById(R.id.textViewAnyPosts);

        recyclerViewMYPosts = view.findViewById(R.id.recyclerViewMYPosts);

        recyclerViewMYPosts.setHasFixedSize(true);
        //gives the recyclerView a layoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMYPosts.setLayoutManager(layoutManager);

        //makes an RecuclerViewAdapter which we created and sets it to the recyclerView
        adapter = new RecyclerViewAdapterHome(posts, getActivity());
        recyclerViewMYPosts.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postOrder = db.collection("users").document(user).collection("posts");
        postOrder.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                posts.clear();
                if (error == null) {
                    for (QueryDocumentSnapshot doc : value) {
                        //since we can't just get the object we from firestore we get it as a hashmap and convert that into a user object
                        HashMap u = (HashMap) (doc.get("user"));
                        User use = new User(u.get("name").toString(), u.get("id").toString(), u.get("email").toString());
                        //adds a new petPost to the posts array
                        posts.add(new PetPost(doc.get("lost").toString(), doc.get("PetName").toString(), use, doc.get("details").toString(), doc.get("image").toString(), doc.get("lat").toString(), doc.get("lng").toString()));
                    }
                    adapter.notifyDataSetChanged();
                    if (posts.isEmpty()) {
                        textViewAnyPosts.setVisibility(view.VISIBLE);
                    } else {
                        textViewAnyPosts.setVisibility(view.INVISIBLE);
                    }
                }
            }
        });
        return view;
    }
}